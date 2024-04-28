package cl.gfmn.authserver.controller;

import cl.gfmn.authserver.model.Response;
import cl.gfmn.authserver.model.client.CreateClientRequest;
import cl.gfmn.authserver.service.ClientService;
import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@OpenAPIDefinition(
        info = @Info(
                title = "Auth Server Maintainers API definition",
                version = "1.0.0"
        )
)

@SecurityScheme(
        name = "BearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)

@RestController
@RequestMapping("/v1/api/client")
@RequiredArgsConstructor
@Tag(name = "Client maintainer endpoints")
public class ClientController {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    private final ClientService clientService;

    private final Gson gson = new Gson();

    @PostMapping
    @SecurityRequirement(name = "BearerAuth")
    @PreAuthorize("hasAuthority('SCOPE_api.consume')")
    ResponseEntity<Response> createClient(@RequestBody CreateClientRequest request) {

        logger.info("POST - Create client consumption BEGIN");

        Response response = clientService.createClient(request);

        logger.info("POST - Create client consumption END, response: {}", gson.toJson(response));

        return ResponseEntity.ok(response);
    }
}
