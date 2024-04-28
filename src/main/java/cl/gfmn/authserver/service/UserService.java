package cl.gfmn.authserver.service;

import cl.gfmn.authserver.exception.BadRequestException;
import cl.gfmn.authserver.model.Response;
import cl.gfmn.authserver.model.user.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    /**
     * Method that creates an user un database
     * @param request CreateUserRequest POJO
     * @return Response POJO
     */
    public Response createUser(CreateUserRequest request) {

        //validate if user exists
        if(userDetailsManager.userExists(request.username()))
            throw new BadRequestException("User already exists");


        List<SimpleGrantedAuthority> authoritiesList = new ArrayList<>();
        //validates if authorities list got the 'ROLE_' prefix, if is the case, transform string to SimpleGrantedAuthority class
        request.authorities().forEach(authority -> {
            if(!authority.startsWith("ROLE_")) {
                throw new BadRequestException("Authority =  " + authority + " must start with ROLE_ prefix");
            }
            authoritiesList.add(new SimpleGrantedAuthority(authority));
        });

        userDetailsManager.createUser(User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .authorities(authoritiesList)
                .build());

        return new Response(LocalDateTime.now().toString(), "User created");
    }
}
