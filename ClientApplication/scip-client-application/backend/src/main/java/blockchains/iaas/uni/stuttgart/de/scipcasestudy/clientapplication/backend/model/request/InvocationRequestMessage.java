package blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.model.request;

import java.util.List;

import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.model.response.Parameter;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class InvocationRequestMessage extends AsyncScipRequestMessage {
    private String functionIdentifier;
    private List<Parameter> inputs;
    private List<Parameter> outputs;
    private double requiredConfidence;
    private long timeout;
    private String signature;
}
