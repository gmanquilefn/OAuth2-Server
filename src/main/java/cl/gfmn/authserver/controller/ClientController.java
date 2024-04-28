package cl.gfmn.authserver.controller;

import cl.gfmn.authserver.model.Response;
import cl.gfmn.authserver.model.user.CreateClientRequest;
import cl.gfmn.authserver.service.ClientService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/client")
@RequiredArgsConstructor
public class ClientController {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    private final ClientService clientService;

    private final Gson gson = new Gson();

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_api.consume')")
    ResponseEntity<Response> createClient(@RequestBody CreateClientRequest request) {

        logger.info("POST - Create client consumption BEGIN");

        Response response = clientService.createClient(request);

        logger.info("POST - Create client consumption END, response: {}", gson.toJson(response));

        return ResponseEntity.ok(response);
    }
}
