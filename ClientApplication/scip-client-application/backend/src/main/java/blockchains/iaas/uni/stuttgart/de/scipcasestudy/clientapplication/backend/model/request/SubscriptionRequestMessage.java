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
    private String functionIdentifier;
    private String eventIdentifier;
    private Double degreeOfConfidence;
    private String filter;
    private List<Parameter> parameters;
}
