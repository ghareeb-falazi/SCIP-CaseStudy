package blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.restapi;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import javax.json.Json;
import javax.json.JsonObject;

import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.model.request.InvocationRequestMessage;
import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.model.response.InvokeResponse;
import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.model.response.Parameter;
import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.scipclient.ScipClient;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class EmsStateController {
    private static Logger log = LoggerFactory.getLogger(EmsStateController.class);
    private final String sclEms;
    private final String TYPE_STRING = "{\"type\": \"string\"}";
    private final String callbackUrl;

    public EmsStateController() {
        sclEms = UrlProvider.getInstance().getEmsBalUrl() + "/webapi?blockchain=fabric&blockchain-id=fabric-0&address=mychannel/ems";
        callbackUrl = UrlProvider.getInstance().getCallbackUrl();
    }

    @CrossOrigin
    @RequestMapping(value = "/query", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public EmsState queryState() {
        log.info("Querying EMS State...");
        InvocationRequestMessage requestMessage = InvocationRequestMessage
                .builder()
                .functionIdentifier("queryState")
                .callbackUrl(callbackUrl)
                .correlationIdentifier(RandomStringUtils.random(10))
                .inputs(Collections.emptyList())
                .outputs(Collections.singletonList(Parameter.builder().name("result").type(TYPE_STRING).build()))
                .signature("")
                .build();

        try {
            InvokeResponse result = ScipClient.getInstance().invoke(sclEms, requestMessage).exceptionally(e -> null).get();

            if (result == null || result.getParameters() == null || result.getParameters().size() != 1) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The invocation failed!");
            }

            String toParse = result.getParameters().get(0).getValue();
            JsonObject jObject = Json.createReader(new ByteArrayInputStream(toParse.getBytes())).readObject();
            EmsState emsState = new EmsState();
            emsState.setPowerPlantAmount(jObject.getString("powerPlantAmount"));
            emsState.setPowerPlantBalanceEur(jObject.getString("powerPlantBalanceEur"));
            emsState.setPowerPlantPrice(jObject.getString("powerPlantPrice"));
            emsState.setPowerProviderAmount(jObject.getString("powerProviderAmount"));
            emsState.setPowerProviderBalanceEur(jObject.getString("powerProviderBalanceEur"));
            emsState.setPowerProviderPrice(jObject.getString("powerProviderPrice"));

            return emsState;
        } catch (InterruptedException | ExecutionException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
