package blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.model.request;

import java.util.List;

import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.model.response.Parameter;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
public class SubscriptionRequestMessage extends AsyncScipRequestMessage {
    protected String functionIdentifier;
    protected String eventIdentifier;
    protected Double degreeOfConfidence;
    protected String filter;
    protected List<Parameter> parameters;
}
