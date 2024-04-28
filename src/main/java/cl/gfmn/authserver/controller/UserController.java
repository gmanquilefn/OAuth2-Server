package cl.gfmn.authserver.controller;

import cl.gfmn.authserver.model.Response;
import cl.gfmn.authserver.model.user.CreateUserRequest;
import cl.gfmn.authserver.service.UserService;
import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityScheme(
        name = "BearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)

@RestController
@RequestMapping(path = "/v1/api/user")
@RequiredArgsConstructor
@Tag(name = "User maintainer endpoints")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    private final Gson gson = new Gson();

    @PostMapping
    @SecurityRequirement(name = "BearerAuth")
    @PreAuthorize("hasAuthority('SCOPE_api.consume')")
    ResponseEntity<Response> createUser(@RequestBody CreateUserRequest request) {

        logger.info("POST - Create user consumption BEGIN");

        Response response = userService.createUser(request);

        logger.info("POST - Create user consumption END, response = {}", gson.toJson(response));

        return ResponseEntity.ok(response);
    }

}
