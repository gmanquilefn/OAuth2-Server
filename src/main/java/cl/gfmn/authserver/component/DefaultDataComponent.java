package cl.gfmn.authserver.component;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class DefaultDataComponent {

    private final PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner usersRunner(UserDetailsManager usersDetailsManager) {
        return args -> {
            List<UserDetails> users = new ArrayList<>();
            users.add(User.builder()
                    .username("user")
                    .password(passwordEncoder.encode("user"))
                    .roles("USER")
                    .build());
            users.add(User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .roles("ADMIN")
                    .build());
            users.forEach(user -> {
                if(!usersDetailsManager.userExists(user.getUsername()))
                    usersDetailsManager.createUser(user);
            });
        };
    }

    @Bean
    ApplicationRunner clientsRunner(RegisteredClientRepository registeredClientRepository) {
        return args -> {
            List<RegisteredClient> clients = new ArrayList<>();
            clients.add(RegisteredClient
                    .withId(UUID.randomUUID().toString())
                    .clientId("apibasic")
                    .clientSecret(passwordEncoder.encode("apibasic"))
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                    .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                    .scope("api.consume")
                            .tokenSettings(TokenSettings.builder()
                                    .accessTokenTimeToLive(Duration.ofSeconds(3600))
                                    .build())
                    .build());
//            clients.add(RegisteredClient
//                    .withId(UUID.randomUUID().toString())
//                    .clientId("apiauthcode")
//                    .clientSecret(passwordEncoder.encode("apiauthcode"))
//                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                    .authorizationGrantTypes(grantTypes -> grantTypes.addAll(Set.of(
//                            AuthorizationGrantType.CLIENT_CREDENTIALS,
//                            AuthorizationGrantType.AUTHORIZATION_CODE,
//                            AuthorizationGrantType.REFRESH_TOKEN)))
//                    .scopes(scopes -> scopes.addAll(Set.of("")))
//                    .build());
            clients.forEach(client -> {
                if(registeredClientRepository.findByClientId(client.getClientId()) == null)
                    registeredClientRepository.save(client);
            });
        };
    }
}
