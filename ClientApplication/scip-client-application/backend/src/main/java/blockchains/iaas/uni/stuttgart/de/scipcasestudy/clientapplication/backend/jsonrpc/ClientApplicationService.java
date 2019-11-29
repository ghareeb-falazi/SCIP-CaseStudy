package blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.jsonrpc;

import java.util.List;

import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.model.response.GenericResponseMessage;
import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.model.response.Occurrence;
import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.model.response.Parameter;
import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.utils.correlation.AsyncRequestCorrelationManager;
import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcMethod;
import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcOptional;
import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcParam;
import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonRpcService
public class ClientApplicationService {
    private Logger log = LoggerFactory.getLogger(ClientApplicationService.class);

    @JsonRpcMethod
    public void ReceiveResponse(
            @JsonRpcParam("correlationIdentifier") String correlationIdentifier,
            @JsonRpcOptional @JsonRpcParam("parameters") List<Parameter> parameters,
            @JsonRpcOptional @JsonRpcParam("timestamp") String isoTimestamp,
            @JsonRpcOptional @JsonRpcParam("occurrences") List<Occurrence> occurrences,
            @JsonRpcOptional @JsonRpcParam("errorCode") Integer errorCode,
            @JsonRpcOptional @JsonRpcParam("errorMessage") String errorMessage,
            @JsonRpcOptional @JsonRpcParam("transactionHash") String transactionHash,
            @JsonRpcOptional @JsonRpcParam("reachedDoC") Double reachedDoC
    ) {
        log.info("ReceiveCallback was called!");
        GenericResponseMessage response = GenericResponseMessage
                .builder()
                .correlationIdentifier(correlationIdentifier)
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .isoTimestamp(isoTimestamp)
                .occurrences(occurrences)
                .parameters(parameters)
                .reachedDoC(reachedDoC)
                .transactionHash(transactionHash)
                .build();
        AsyncRequestCorrelationManager.getInstance().deliverResult(correlationIdentifier, response);
    }
}
