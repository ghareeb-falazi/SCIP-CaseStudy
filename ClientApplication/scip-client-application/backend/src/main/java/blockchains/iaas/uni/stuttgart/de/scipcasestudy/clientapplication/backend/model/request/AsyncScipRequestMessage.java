package blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
public abstract class AsyncScipRequestMessage extends ScipRequestMessage {
    private String correlationIdentifier;
    private String callbackUrl;
}
