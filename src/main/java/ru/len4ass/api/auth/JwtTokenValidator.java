package ru.len4ass.api.auth;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import ru.len4ass.api.models.user.User;

import java.util.Date;

@Service
public class JwtTokenValidator {
    @Resource
    private final JwtClaimExtractor jwtClaimExtractor;

    public JwtTokenValidator(JwtClaimExtractor jwtClaimExtractor) {
        this.jwtClaimExtractor = jwtClaimExtractor;
    }

    public boolean isTokenExpired(String jwtToken) {
        return jwtClaimExtractor.extractExpirationDate(jwtToken).before(new Date(System.currentTimeMillis()));
    }

    public boolean isValidToken(String jwtToken, User user) {
        var id = jwtClaimExtractor.extractUserId(jwtToken);
        return user.getId().equals(id) && !isTokenExpired(jwtToken);
    }
}
