package blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.scipclient;

import java.util.concurrent.CompletableFuture;

public class ScipClient {
    private static ScipClient instance;

    private ScipClient() {

    }

    public static ScipClient getInstance() {
        if (instance == null) {
            instance = new ScipClient();
        }

        return instance;
    }

    public <T> CompletableFuture<? extends T> invoke() {

    }
}
