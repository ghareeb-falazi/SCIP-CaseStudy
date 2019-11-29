package blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.model.response;

import java.util.List;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class SubscribeResponse extends AbstractScipResponse {
    private List<Parameter> parameters;
    private String isoTimestamp;
}
