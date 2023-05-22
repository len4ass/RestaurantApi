package ru.len4ass.api.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import ru.len4ass.api.models.user.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtTokenGenerator {
    @Resource
    private JwtSecret jwtSecret;

    public String generateJwtToken(User user, Date expirationDate) {
        Map<String, Object> claims = new HashMap<>() {{
            put("id", user.getId());
            put("user_role", user.getUserRole());
        }};

        return generateJwtToken(claims, expirationDate);
    }

    public String generateJwtToken(Map<String, Object> extraClaims, Date expirationDate) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .signWith(jwtSecret.getKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
