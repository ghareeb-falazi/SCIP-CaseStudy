package blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.model.request;

import java.util.List;

import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.model.response.Parameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InvocationRequestMessage extends ScipRequestMessage {
    private String functionIdentifier;
    private List<Parameter> inputs;
    private List<Parameter> outputs;
    private double requiredConfidence;
    private String callbackUrl;
    private long timeout;
    private String correlationIdentifier;
    private String signature;
}
