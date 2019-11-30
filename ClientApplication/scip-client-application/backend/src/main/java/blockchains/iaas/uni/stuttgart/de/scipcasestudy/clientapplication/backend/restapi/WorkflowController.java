package blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.restapi;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.model.request.InvocationRequestMessage;
import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.model.request.SubscriptionRequestMessage;
import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.model.response.LogEntry;
import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.model.response.Parameter;
import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.scipclient.ScipClient;
import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.utils.LogsHandler;
import io.reactivex.disposables.Disposable;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WorkflowController {
    private static Logger log = LoggerFactory.getLogger(WorkflowController.class);
    private final String sclEms = "http://localhost:8090/webapi?blockchain=fabric&blockchain-id=fabric-0&address=mychannel/ems";
    private final String sclDigest = "http://localhost:8090/webapi?blockchain=ethereum&blockchain-id=eth-0&address=0x75f17644EAEb3cC6511764a6F1138F14B3e33D0f";
    private final String callbackUrl = "http://localhost:8080/";
    private final String TYPE_FABRIC_INT = "{\"type\": \"integer\"}";
    private final String TYPE_STRING = "{\"type\": \"string\"}";
    private final String TYPE_ETHEREUM_BYTES = "{\n" +
            "  \"type\": \"array\",\n" +
            "  \"items\": {\n" +
            "        \"type\": \"string\",\n" +
            "        \"pattern\": \"^[a-fA-F0-9]{2}$\"\n" +
            "   }\n" +
            "}";

    @CrossOrigin
    @RequestMapping(value = "workflow", method = RequestMethod.POST)
    public String runWorkflow() {
        SubscriptionRequestMessage subsReq = this.getSubscribeRequestMessage();
        printMessage("======= Starting Workflow ========");
        printMessage("======= Subscribing... ========");
        printMessage(" Sending the following request message to " + sclEms + " : " + subsReq.toString());
        Disposable subscriptionDisposable = ScipClient.getInstance().subscribe(sclEms, subsReq)
                .subscribe(
                        result -> {
                            printMessage("Received a subscription callback with the payload: " + result.toString());
                            InvocationRequestMessage buyInBulkRequestMessage = getBuyInBulkRequestMessage();
                            InvocationRequestMessage changeRetailPriceMessage = getChangeRetailPriceRequestMessage();
                            InvocationRequestMessage storeDigest1Message = getStoreDigestRequestMessage(buyInBulkRequestMessage);
                            InvocationRequestMessage storeDigest2Message = getStoreDigestRequestMessage(changeRetailPriceMessage);
                            printMessage("======= Buying in bulk... ========");
                            printMessage(" Sending the following request message to " + sclEms + " : " + buyInBulkRequestMessage.toString());
                            ScipClient.getInstance()
                                    .invoke(sclEms, buyInBulkRequestMessage)
                                    .thenCompose(invoke1Response -> {
                                        printMessage("Received an invocation confirmation callback with payload: " + invoke1Response.toString());
                                        printMessage("======= Changing retail price... ========");
                                        printMessage(" Sending the following request message to " + sclEms + " : " + changeRetailPriceMessage.toString());
                                        return ScipClient.getInstance().invoke(sclEms, changeRetailPriceMessage);
                                    })
                                    .thenCompose(invoke2Response -> {
                                        printMessage("Received an invocation confirmation callback with payload: " + invoke2Response.toString());
                                        printMessage("======= Storing Digest 1... ========");
                                        printMessage(" Sending the following request message to " + sclDigest + " : " + storeDigest1Message.toString());
                                        return ScipClient.getInstance().invoke(sclDigest, storeDigest1Message);
                                    })
                                    .thenCompose(invoke3Response -> {
                                        printMessage("Received an invocation confirmation callback with payload: " + invoke3Response.toString());
                                        printMessage("======= Storing Digest 2... ========");
                                        printMessage(" Sending the following request message to " + sclDigest + " : " + storeDigest2Message.toString());
                                        return ScipClient.getInstance().invoke(sclDigest, storeDigest2Message);
                                    })
                                    .thenAccept(finalResult -> {
                                        printMessage("Received an invocation confirmation callback with payload: " + finalResult.toString());
                                        printMessage("======= Done!!! ========");
                                        LogsHandler.getInstance().recordDoneSignal();
                                    })
                                    .exceptionally(e -> {
                                        printError(e.getMessage());
                                        return null;
                                    });
                        },
                        error -> printError(error.getMessage()));

        return "Workflow started!";
    }

    @CrossOrigin
    @RequestMapping(value = "workflow/query", method = RequestMethod.GET)
    public List<LogEntry> queryState() {
        return LogsHandler.getInstance().getEntries();
    }

    private SubscriptionRequestMessage getSubscribeRequestMessage() {
        return SubscriptionRequestMessage.builder()
                .degreeOfConfidence(99.99)
                .eventIdentifier("priceChanged")
                .parameters(Collections.singletonList(Parameter.builder()
                        .name("newPrice")
                        .type(TYPE_FABRIC_INT)
                        .build()))
                .correlationIdentifier("subscription-msg-id")
                .callbackUrl(callbackUrl)
                .filter("newPrice <= 500")
                .build();
    }

    private InvocationRequestMessage getBuyInBulkRequestMessage() {
        return InvocationRequestMessage.builder()
                .inputs(Collections.singletonList(Parameter
                        .builder()
                        .name("amount")
                        .type(TYPE_FABRIC_INT)
                        .value("1000")
                        .build()))
                .outputs(Collections.emptyList())
                .functionIdentifier("buyInBulk")
                .callbackUrl(callbackUrl)
                .correlationIdentifier("buy-in-bulk-msg-id")
                .signature("")
                .build();
    }

    private InvocationRequestMessage getChangeRetailPriceRequestMessage() {
        return InvocationRequestMessage.builder()
                .inputs(Collections.singletonList(Parameter
                        .builder()
                        .name("newPrice")
                        .type(TYPE_FABRIC_INT)
                        .value("900")
                        .build()))
                .outputs(Collections.emptyList())
                .functionIdentifier("changeRetailPrice")
                .callbackUrl(callbackUrl)
                .correlationIdentifier("buy-in-bulk-msg-id")
                .signature("")
                .build();
    }

    private InvocationRequestMessage getStoreDigestRequestMessage(InvocationRequestMessage previousRequest) {
        String digest16 = Hex.encodeHexString(DigestUtils.sha256(previousRequest.toString()));

        return InvocationRequestMessage.builder()
                .inputs(Arrays.asList(
                        Parameter.builder()
                                .name("corrId")
                                .type(TYPE_STRING)
                                .value(previousRequest.getCorrelationIdentifier())
                                .build(),
                        Parameter.builder()
                                .name("digest")
                                .type(TYPE_ETHEREUM_BYTES)
                                .value(digest16).build()
                ))
                .outputs(Collections.emptyList())
                .functionIdentifier("addDigest")
                .callbackUrl(callbackUrl)
                .correlationIdentifier("store-digest-for-" + previousRequest.getCorrelationIdentifier())
                .requiredConfidence(60)
                .signature("")
                .build();
    }

    private void printMessage(String message) {
        log.info(message);
        LogsHandler.getInstance().addMessage(message);
    }

    private void printError(String message) {
        log.error(message);
        LogsHandler.getInstance().addError(message);
    }
}
