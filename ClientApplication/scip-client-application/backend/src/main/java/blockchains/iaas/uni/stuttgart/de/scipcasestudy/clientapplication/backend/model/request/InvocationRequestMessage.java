package blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.model.request;

import java.util.List;

import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.model.response.Parameter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class InvocationRequestMessage extends AsyncScipRequestMessage {
    protected String functionIdentifier;
    protected List<Parameter> inputs;
    protected List<Parameter> outputs;
    protected double requiredConfidence;
    protected long timeout;
    protected String signature;


}
