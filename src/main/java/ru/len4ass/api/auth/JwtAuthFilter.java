package ru.len4ass.api.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.len4ass.api.models.error.ErrorResponse;
import ru.len4ass.api.models.user.User;
import ru.len4ass.api.repositories.UserRepository;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Resource
    private final JwtTokenValidator jwtTokenValidator;

    @Resource
    private final JwtClaimExtractor jwtClaimExtractor;

    @Resource
    private final UserRepository userRepository;

    public JwtAuthFilter(
            JwtTokenValidator jwtTokenValidator,
            JwtClaimExtractor jwtClaimExtractor,
            UserRepository userDetailsService) {
        this.jwtTokenValidator = jwtTokenValidator;
        this.jwtClaimExtractor = jwtClaimExtractor;
        this.userRepository = userDetailsService;
    }

    public void setServletResponseBody(@NonNull HttpServletResponse response, ErrorResponse errorResponse) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }

    private void runFilterChain(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull FilterChain filterChain) throws ServletException, IOException {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            setServletResponseBody(response, new ErrorResponse("Authorization header not found."));
            return;
        }

        var authHeaderSplitBySpace = authHeader.split("\\s+");
        if (authHeaderSplitBySpace.length != 2) {
            setServletResponseBody(response, new ErrorResponse("Authorization header is malformed."));
            return;
        }

        var authType = authHeaderSplitBySpace[0];
        if (!authType.equals("Bearer")) {
            setServletResponseBody(response, new ErrorResponse("Authorization type must be bearer token."));
            return;
        }

        var jwtToken = authHeaderSplitBySpace[1];
        var id = jwtClaimExtractor.extractUserId(jwtToken);
        if (id == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        User user = userRepository.findById(id).orElseThrow();
        if (jwtTokenValidator.isValidToken(jwtToken, user)) {
            var authToken = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource()
                    .buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            runFilterChain(request, response, filterChain);
        } catch (JwtException e) {
            setServletResponseBody(response, new ErrorResponse("Failed to authorize. Check your JWT token."));
        }
    }
}
