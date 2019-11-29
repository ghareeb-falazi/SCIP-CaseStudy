package blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.scipclient;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.exceptions.BalException;
import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.exceptions.ExceptionCode;
import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.model.request.InvocationRequestMessage;
import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.model.request.SubscriptionRequestMessage;
import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.model.response.InvokeResponse;
import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.model.response.SubscribeResponse;
import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.utils.JsonRpcIdGenerator;
import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.utils.correlation.AsyncRequestCorrelationManager;
import com.github.arteam.simplejsonrpc.client.JsonRpcClient;
import com.github.arteam.simplejsonrpc.client.Transport;
import com.github.arteam.simplejsonrpc.client.exception.JsonRpcException;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import org.apache.commons.codec.Charsets;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class ScipClient {
    private static Logger log = LoggerFactory.getLogger(ScipClient.class);
    private static ScipClient instance;
    private ExecutorService executorService;

    private ScipClient() {

    }

    public static ScipClient getInstance() {
        if (instance == null) {
            instance = new ScipClient();
        }

        return instance;
    }

    private ExecutorService getExecutorService() {
        if (executorService == null || executorService.isShutdown() || executorService.isTerminated()) {
            this.executorService = Executors.newCachedThreadPool();
        }

        return this.executorService;
    }

    public CompletableFuture<InvokeResponse> invoke(String scl, InvocationRequestMessage request) {
        log.info("Sending Invoke request message to gateway at: {}", scl);
        CompletableFuture<InvokeResponse> result = new CompletableFuture<>();
        AsyncRequestCorrelationManager.getInstance().addEntry(request.getCorrelationIdentifier(), finalResponse -> {
            if (finalResponse.getErrorCode() != null) {
                BalException exception = new BalException(finalResponse.getErrorMessage(), finalResponse.getErrorCode());
                result.completeExceptionally(exception);
            } else {
                InvokeResponse normalResponse = InvokeResponse
                        .builder()
                        .parameters(finalResponse.getParameters())
                        .correlationIdentifier(finalResponse.getCorrelationIdentifier())
                        .build();
                result.complete(normalResponse);
            }
        });
        this.getExecutorService().submit(() -> {
            this.performInvoke(scl, request);
        });

        return result.whenComplete((val, error) -> {
            AsyncRequestCorrelationManager.getInstance().removeEntry(request.getCorrelationIdentifier());
        });
    }

    public Observable<SubscribeResponse> subscribe(String scl, SubscriptionRequestMessage request) {
        log.info("Sending Subscribe request message to gateway at: {}", scl);
        PublishSubject<SubscribeResponse> observable = PublishSubject.create();
        AsyncRequestCorrelationManager.getInstance().addEntry(request.getCorrelationIdentifier(), finalResponse -> {
            if (finalResponse.getErrorCode() != null) {
                BalException exception = new BalException(finalResponse.getErrorMessage(), finalResponse.getErrorCode());
                observable.onError(exception);
            } else {
                SubscribeResponse normalResponse = SubscribeResponse
                        .builder()
                        .parameters(finalResponse.getParameters())
                        .correlationIdentifier(finalResponse.getCorrelationIdentifier())
                        .isoTimestamp(finalResponse.getIsoTimestamp())
                        .build();
                observable.onNext(normalResponse);
            }
        });
        this.getExecutorService().submit(() -> {
            this.performSubscribe(scl, request);
        });

        return observable.doFinally(() -> {
            AsyncRequestCorrelationManager.getInstance().removeEntry(request.getCorrelationIdentifier());
        });
    }

    private void performInvoke(String scl, InvocationRequestMessage request) throws BalException {
        try {
            final String METHOD_NAME = "Invoke";
            JsonRpcClient client = new JsonRpcClient(createNewTransport(scl));

            Object response = client.createRequest()
                    .method(METHOD_NAME)
                    .id(JsonRpcIdGenerator.getInstance().getNextId())
                    .param("correlationIdentifier", request.getCorrelationIdentifier())
                    .param("inputs", request.getInputs())
                    .param("outputs", request.getOutputs())
                    .param("callbackUrl", request.getCallbackUrl())
                    .param("signature", request.getSignature())
                    .param("timeout", request.getTimeout())
                    .param("doc", request.getRequiredConfidence())
                    .param("functionIdentifier", request.getFunctionIdentifier())
                    .execute();

            log.info("Received: {} from gateway at {}", response, scl);
        } catch (JsonRpcException e) {
            throw new BalException(e.getErrorMessage().getMessage(), e.getErrorMessage().getCode());
        } catch (Exception e) {
            throw new BalException("Unable to invoke smart contract function. Reason: " + e.getMessage(), ExceptionCode.InvocationError);
        }
    }

    private void performSubscribe(String scl, SubscriptionRequestMessage request) throws BalException {
        try {
            final String METHOD_NAME = "Subscribe";
            JsonRpcClient client = new JsonRpcClient(createNewTransport(scl));

            Object response = client.createRequest()
                    .method(METHOD_NAME)
                    .id(JsonRpcIdGenerator.getInstance().getNextId())
                    .param("correlationIdentifier", request.getCorrelationIdentifier())
                    .param("parameters", request.getParameters())
                    .param("callbackUrl", request.getCallbackUrl())
                    .param("eventIdentifier", request.getEventIdentifier())
                    .param("filter", request.getFilter())
                    .param("doc", request.getDegreeOfConfidence())
                    .execute();

            log.info("Received: {} from gateway at {}", response, scl);
        } catch (JsonRpcException e) {
            throw new BalException(e.getErrorMessage().getMessage(), e.getErrorMessage().getCode());
        } catch (Exception e) {
            throw new BalException("Unable to subscribe to events. Reason: " + e.getMessage(), ExceptionCode.InvocationError);
        }
    }

    private Transport createNewTransport(String scl) {
        return new Transport() {
            CloseableHttpClient httpClient = HttpClients.createDefault();

            @NotNull
            @Override
            public String pass(@NotNull String request) throws IOException {
                // Used Apache HttpClient 4.3.1 as an example
                HttpPost post = new HttpPost(scl);
                post.setEntity(new StringEntity(request, Charsets.UTF_8));
                post.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
                try (CloseableHttpResponse httpResponse = httpClient.execute(post)) {
                    return EntityUtils.toString(httpResponse.getEntity(), Charsets.UTF_8);
                }
            }
        };
    }
}
