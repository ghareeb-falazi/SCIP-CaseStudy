package blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.jsonrpc;

import java.util.List;

import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.model.response.Occurrence;
import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.model.response.Parameter;
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
    public void ReceiveCallback(
            @JsonRpcParam("correlationIdentifier") String correlationIdentifier,
            @JsonRpcOptional @JsonRpcParam("parameters") List<Parameter> parameters,
            @JsonRpcOptional @JsonRpcParam("timestamp") String isoTimestamp,
            @JsonRpcOptional @JsonRpcParam("occurrences") List<Occurrence> occurrences,
            @JsonRpcOptional @JsonRpcParam("errorCode") int errorCode,
            @JsonRpcOptional @JsonRpcParam("errorMessage") int errorMessage,
            @JsonRpcOptional @JsonRpcParam("transactionHash") String transactionHash,
            @JsonRpcOptional @JsonRpcParam("reachedDoC") double reachedDoC
    ) {
        log.info("ReceiveCallback was called!");
    }
}
