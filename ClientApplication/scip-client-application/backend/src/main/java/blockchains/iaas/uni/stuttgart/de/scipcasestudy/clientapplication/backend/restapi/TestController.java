package blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.restapi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.scipclient.ScipClient;
import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.model.request.InvocationRequestMessage;
import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.model.response.InvokeResponse;
import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.model.response.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "")
public class TestController {
    private static Logger log = LoggerFactory.getLogger(RootController.class);

    private String getCallbackUrl() {
        return "http://localhost:8080/";
    }

    @RequestMapping(value = "test/invoke", method = RequestMethod.POST)
    public String testInvocation() throws ExecutionException, InterruptedException {
        final String scl = "http://localhost:8090/webapi?blockchain=fabric&blockchain-id=fabric-0&address=mychannel/ems";
        InvocationRequestMessage requestMessage = InvocationRequestMessage.builder()
                .inputs(new ArrayList<>())
                .outputs(Collections.singletonList(Parameter.builder().name("Result").type("{\"type\": \"string\"}").build()))
                .functionIdentifier("queryState")
                .callbackUrl(getCallbackUrl())
                .correlationIdentifier("abc")
                .signature("")
                .build();
        InvokeResponse response = ScipClient.getInstance().invoke(scl, requestMessage).exceptionally(e -> {
                    log.error(e.getMessage(), e);
                    return null;
                }
        ).get();

        if (response != null) {
            log.info("received response with {} parameters", response.getParameters().size());

            if (response.getParameters().size() > 0) {
                log.info("parameters[0]: {}", response.getParameters().get(0).getValue());
                return response.getParameters().get(0).getValue();
            }
        }

        return "Test successful!";
    }
}
