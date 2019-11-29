package blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.utils.correlation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.model.response.GenericResponseMessage;

public class AsyncRequestCorrelationManager {
    private static AsyncRequestCorrelationManager instance;
    private Map<String, Consumer<GenericResponseMessage>> responseProviders;

    private AsyncRequestCorrelationManager() {
        this.responseProviders = new HashMap<>();
    }

    public static AsyncRequestCorrelationManager getInstance() {
        if (instance == null) {
            instance = new AsyncRequestCorrelationManager();
        }

        return instance;
    }

    public void addEntry(String correlationProvider, Consumer<GenericResponseMessage> consumer) {
        this.responseProviders.put(correlationProvider, consumer);
    }

    public void deliverResult(String correlationIdentifier, GenericResponseMessage result) {
        if (this.responseProviders.containsKey(correlationIdentifier)) {
            Consumer<GenericResponseMessage> consumer = this.responseProviders.get(correlationIdentifier);
            consumer.accept(result);
        }
    }

    public void removeEntry(String correlationIdentifier) {
        this.responseProviders.remove(correlationIdentifier);
    }
}
