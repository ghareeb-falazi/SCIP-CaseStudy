package blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.restapi;

import com.google.common.base.Strings;

public class UrlProvider {
    private static UrlProvider instance;

    private UrlProvider() {

    }

    public static UrlProvider getInstance() {
        if (instance == null) {
            instance = new UrlProvider();
        }

        return instance;
    }

    public String getEmsBalUrl() {
        String envVar = System.getenv("EMS_BAL_URL");

        if (Strings.isNullOrEmpty(envVar)) {
            return "http://localhost:8090";
        }

        return envVar;
    }

    public String getDigestBalUrl() {
        String envVar = System.getenv("DIGEST_BAL_URL");

        if (Strings.isNullOrEmpty(envVar)) {
            return "http://localhost:8090";
        }

        return envVar;
    }

    public String getCallbackUrl() {
        String envVar = System.getenv("CLIENT_CALLBACK_URL");

        if (Strings.isNullOrEmpty(envVar)) {
            return "http://localhost:9090";
        }

        return envVar;
    }
}
