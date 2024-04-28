package cl.gfmn.authserver.model.user;

import java.util.List;

public record CreateClientRequest(String client_id,
                                  String client_secret,
                                  List<String> authentication_methods,
                                  List<String> authorization_grant_types,
                                  List<String> scopes,
                                  List<String> redirect_uris,
                                  Integer access_token_time_to_live) {
}
