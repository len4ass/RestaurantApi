package ru.len4ass.api.services;

import jakarta.annotation.Resource;
import jakarta.persistence.EntityExistsException;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.len4ass.api.auth.JwtTokenGenerator;
import ru.len4ass.api.configs.AdminCredentialsConfig;
import ru.len4ass.api.models.session.Session;
import ru.len4ass.api.models.user.User;
import ru.len4ass.api.models.user.UserRole;
import ru.len4ass.api.repositories.SessionRepository;
import ru.len4ass.api.repositories.UserRepository;

import java.util.Date;

@Component
public class StartupService {
    @Resource
    private final UserRepository userRepository;

    @Resource
    private final SessionRepository sessionRepository;

    @Resource
    private final JwtTokenGenerator jwtTokenGenerator;

    @Resource
    private final AdminCredentialsConfig adminSecret;

    public StartupService(UserRepository userRepository,
                          SessionRepository sessionRepository,
                          JwtTokenGenerator jwtTokenGenerator,
                          AdminCredentialsConfig adminSecret) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.adminSecret = adminSecret;
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        var username = adminSecret.getUsername();
        var email = adminSecret.getEmail();

        var optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent() && optionalUser.get().getUserRole() != UserRole.ADMIN) {
            throw new EntityExistsException("Non-admin user with such username already exists.");
        }

        if (optionalUser.isPresent()) {
            return;
        }

        var passwordEncoder = UserAuthenticationService.getPasswordEncoder();
        var encodedPass = passwordEncoder.encode(adminSecret.getPassword());
        var adminUser = new User(
                username,
                email,
                encodedPass,
                UserRole.ADMIN);
        adminUser = userRepository.saveAndFlush(adminUser);

        var tokenExpirationDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60);
        var jwtToken = jwtTokenGenerator.generateJwtToken(adminUser, tokenExpirationDate);
        var session = new Session(adminUser, jwtToken, tokenExpirationDate);
        sessionRepository.saveAndFlush(session);
    }
}
