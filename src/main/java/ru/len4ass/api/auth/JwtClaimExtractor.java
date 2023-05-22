package ru.len4ass.api.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import ru.len4ass.api.models.user.UserRole;

import java.util.Date;
import java.util.function.Function;

@Service
public class JwtClaimExtractor {
    @Resource
    private JwtSecret jwtSecret;

    private Claims extractClaims(String jwtToken) {
        return Jwts
                .parserBuilder()
                .setSigningKey(jwtSecret.getKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    public Integer extractUserId(String jwtToken) {
        var claims = extractClaims(jwtToken);
        return claims.get("id", Integer.class);
    }

    public UserRole extractUserRole(String jwtToken) {
        var claims = extractClaims(jwtToken);
        return UserRole.valueOf(claims.get("user_role").toString());
    }

    public <T> T extractClaim(String jwtToken, Function<Claims, T> resolver) {
        var claims = extractClaims(jwtToken);
        return resolver.apply(claims);
    }

    public Date extractExpirationDate(String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration);
    }
}
