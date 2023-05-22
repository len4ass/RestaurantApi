package ru.len4ass.api.controllers;

import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.len4ass.api.models.authentication.AuthenticationResponse;
import ru.len4ass.api.models.authentication.LoginRequest;
import ru.len4ass.api.models.authentication.SignupRequest;
import ru.len4ass.api.services.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Resource
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginUser(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody SignupRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }
}
