package cl.gfmn.authserver.model.user;

import java.util.List;

public record CreateUserRequest(String username,
                                String password,
                                Boolean is_active,
                                List<String> authorities) {
}
