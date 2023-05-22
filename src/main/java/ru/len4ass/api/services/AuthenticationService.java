package ru.len4ass.api.services;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import ru.len4ass.api.auth.JwtTokenGenerator;
import ru.len4ass.api.auth.JwtTokenValidator;
import ru.len4ass.api.exceptions.UnauthorizedException;
import ru.len4ass.api.models.authentication.AuthenticationResponse;
import ru.len4ass.api.models.authentication.LoginRequest;
import ru.len4ass.api.models.authentication.SignupRequest;
import ru.len4ass.api.models.session.Session;
import ru.len4ass.api.models.user.User;
import ru.len4ass.api.repositories.SessionRepository;
import ru.len4ass.api.repositories.UserRepository;
import ru.len4ass.api.validators.EmailValidator;
import ru.len4ass.api.validators.PasswordValidator;
import ru.len4ass.api.validators.UsernameValidator;

import java.util.Date;

@Service
public class AuthenticationService {
    @Resource
    private final UserRepository userRepository;

    @Resource
    private final SessionRepository sessionRepository;

    @Resource
    private final JwtTokenGenerator jwtTokenGenerator;

    @Resource
    private final JwtTokenValidator jwtTokenValidator;

    public AuthenticationService(UserRepository userRepository,
                                 SessionRepository sessionRepository,
                                 JwtTokenGenerator jwtTokenGenerator,
                                 JwtTokenValidator jwtTokenValidator) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.jwtTokenValidator = jwtTokenValidator;
    }

    private void validateRegistrationRequest(SignupRequest request) {
        if (!UsernameValidator.isValid(request.getUsername())) {
            throw new UnauthorizedException(UsernameValidator.getInvalidConstraints());
        }

        if (!EmailValidator.isValid(request.getEmail())) {
            throw new UnauthorizedException(EmailValidator.getInvalidConstraints());
        }

        if (!PasswordValidator.isValid(request.getPassword())) {
            throw new UnauthorizedException(PasswordValidator.getInvalidConstraints());
        }
    }

    public AuthenticationResponse register(SignupRequest request) {
        var userByEmail = userRepository.findUserByEmail(request.getEmail());
        if (userByEmail.isPresent()) {
            throw new UnauthorizedException(String.format("User with email %s already exists.", request.getEmail()));
        }

        var userByName = userRepository.findByUsername(request.getUsername());
        if (userByName.isPresent()) {
            throw new UnauthorizedException(String.format("User with username %s already exists.", request.getUsername()));
        }

        validateRegistrationRequest(request);

        var passwordEncoder = UserAuthenticationService.getPasswordEncoder();
        var user = new User(request.getUsername(), request.getEmail(), passwordEncoder.encode(request.getPassword()));
        user = userRepository.saveAndFlush(user);

        var tokenExpirationDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60);
        var jwtToken = jwtTokenGenerator.generateJwtToken(user, tokenExpirationDate);
        var session = new Session(user, jwtToken, tokenExpirationDate);
        sessionRepository.saveAndFlush(session);

        return new AuthenticationResponse("Signed up successfully.", jwtToken);
    }

    public AuthenticationResponse login(LoginRequest request) {
        var userToAuth = userRepository.findUserByEmail(request.getEmail());
        if (userToAuth.isEmpty()) {
            throw new UnauthorizedException("Invalid email or password.");
        }

        var isValidPassword = BCrypt.checkpw(request.getPassword(), userToAuth.get().getPassword());
        if (!isValidPassword) {
            throw new UnauthorizedException("Invalid email or password.");
        }

        User user = userToAuth.get();
        var sessionOrEmpty = sessionRepository.findSessionByUser(user);
        if (sessionOrEmpty.isEmpty()) {
            var expirationDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60);
            var jwtToken = jwtTokenGenerator.generateJwtToken(user, expirationDate);
            var session = new Session(user, jwtToken, expirationDate);
            sessionRepository.save(session);

            return new AuthenticationResponse("Signed in successfully.", jwtToken);
        }

        var session = sessionOrEmpty.get();
        var token = session.getToken();
        try {
            jwtTokenValidator.isValidToken(token, user);
            return new AuthenticationResponse("Signed in successfully.", token);
        } catch (ExpiredJwtException ignored) {
        }

        var expirationDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60);
        var jwtToken = jwtTokenGenerator.generateJwtToken(user, expirationDate);

        session.setToken(jwtToken);
        session.setExpirationDate(expirationDate);
        sessionRepository.saveAndFlush(session);

        return new AuthenticationResponse("Signed in successfully.", jwtToken);
    }
}
