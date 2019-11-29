package blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.restapi;

import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.jsonrpc.ClientApplicationService;
import com.github.arteam.simplejsonrpc.server.JsonRpcServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
    private static Logger log = LoggerFactory.getLogger(RootController.class);

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public String performJsonRpcCall(@RequestBody String jsonRequest) {
        log.info("Received the following callback: {}", jsonRequest);
        ClientApplicationService service = new ClientApplicationService();
        JsonRpcServer server = new JsonRpcServer();
        return server.handle(jsonRequest, service);
    }
}
