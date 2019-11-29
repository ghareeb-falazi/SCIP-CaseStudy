package blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.utils;

import java.util.concurrent.atomic.AtomicLong;

public class JsonRpcIdGenerator {
    private static JsonRpcIdGenerator instance;
    private AtomicLong lastUsedId;

    private JsonRpcIdGenerator() {
        lastUsedId = new AtomicLong(0);
    }

    public static JsonRpcIdGenerator getInstance() {
        if (instance == null) {
            instance = new JsonRpcIdGenerator();
        }

        return instance;
    }

    public long getNextId() {
        return lastUsedId.incrementAndGet();
    }
}
