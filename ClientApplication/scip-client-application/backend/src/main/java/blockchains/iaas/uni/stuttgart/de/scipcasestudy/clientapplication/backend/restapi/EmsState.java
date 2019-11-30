package blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.restapi;

import lombok.Data;

@Data
public class EmsState {
    private String powerProviderPrice;
    private String powerProviderAmount;
    private String powerProviderBalanceEur;
    private String powerPlantPrice;
    private String powerPlantAmount;
    private String powerPlantBalanceEur;
}
