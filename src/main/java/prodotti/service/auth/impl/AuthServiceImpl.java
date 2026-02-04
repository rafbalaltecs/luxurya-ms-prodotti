package prodotti.service.auth.impl;

import jakarta.security.auth.message.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import prodotti.service.auth.AuthService;
import prodotti.service.cache.RedisCache;
import prodotti.shared.UserDataShared;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    private UserDataShared userDataShared;
    private final RedisCache redisService;

    public AuthServiceImpl(RedisCache redisService) {
        this.redisService = redisService;
    }

    public UserDataShared getUserDataShared() {
        return userDataShared;
    }


    @Override
    public Boolean existValidToken(String token) {
        if(StringUtils.isNotEmpty(token)){
            return redisService.exists(token);
        }
        return Boolean.FALSE;
    }

    @Override
    public void userFindByToken(String token) throws AuthException {
        if(StringUtils.isNotEmpty(token)){
            userDataShared = (UserDataShared) redisService.get(token);
        }else{
            log.error("Token Not Valid: {}", token);
            throw new AuthException("Token Not Valid, execute Login Action to ms-anagrafica");
        }
    }
}
