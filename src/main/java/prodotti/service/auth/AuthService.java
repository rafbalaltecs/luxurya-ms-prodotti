package prodotti.service.auth;

import jakarta.security.auth.message.AuthException;
import prodotti.shared.UserDataShared;

public interface AuthService {
    Boolean existValidToken(final String token);
    void userFindByToken(final String token) throws AuthException;
    UserDataShared getUserDataShared();
}
