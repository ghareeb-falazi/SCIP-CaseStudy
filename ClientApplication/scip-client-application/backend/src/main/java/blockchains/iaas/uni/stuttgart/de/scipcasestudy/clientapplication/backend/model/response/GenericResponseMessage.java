package blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.model.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GenericResponseMessage {
    private String correlationIdentifier;
    private List<Parameter> parameters;
    private String isoTimestamp;
    private List<Occurrence> occurrences;
    private Integer errorCode;
    private String errorMessage;
    private String transactionHash;
    private Double reachedDoC;
}
