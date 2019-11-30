package blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.model.response;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
