package blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.model.response;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public abstract class AbstractScipResponse {
    private String correlationIdentifier;
}
