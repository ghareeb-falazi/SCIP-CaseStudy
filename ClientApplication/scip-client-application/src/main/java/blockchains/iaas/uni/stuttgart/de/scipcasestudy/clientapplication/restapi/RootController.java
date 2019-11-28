package blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.restapi;

import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.jsonrpc.ClientApplicationService;
import com.github.arteam.simplejsonrpc.server.JsonRpcServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
    private Logger log = LoggerFactory.getLogger(RootController.class);

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String performJsonRpcCall(String jsonRequest) {
        log.info("Received the following callback: {}", jsonRequest);
        ClientApplicationService service = new ClientApplicationService();
        JsonRpcServer server = new JsonRpcServer();
        return server.handle(jsonRequest, service);
    }
}
