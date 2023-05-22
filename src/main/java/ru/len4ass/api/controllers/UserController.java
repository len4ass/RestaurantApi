package ru.len4ass.api.controllers;

import jakarta.annotation.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.len4ass.api.auth.JwtClaimExtractor;
import ru.len4ass.api.exceptions.BadRequestException;
import ru.len4ass.api.exceptions.ForbiddenException;
import ru.len4ass.api.models.GenericResponse;
import ru.len4ass.api.models.user.UserDto;
import ru.len4ass.api.models.user.UserFieldsToUpdateDto;
import ru.len4ass.api.models.user.UserRole;
import ru.len4ass.api.services.UserService;
import ru.len4ass.api.utils.TokenExtractor;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Resource
    private final UserService userService;

    @Resource
    private final JwtClaimExtractor jwtClaimExtractor;

    public UserController(UserService userService, JwtClaimExtractor jwtClaimExtractor) {
        this.userService = userService;
        this.jwtClaimExtractor = jwtClaimExtractor;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCallingUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        var token = TokenExtractor.extractFromHeader(authHeader);
        var id = jwtClaimExtractor.extractUserId(token);
        return ResponseEntity.ok(userService.getUserInfo(id));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                           @PathVariable Integer userId) {
        var token = TokenExtractor.extractFromHeader(authHeader);
        var role = jwtClaimExtractor.extractUserRole(token);

        // Couldn't care less about Spring Boot security filters, they're trash
        if (role != UserRole.ADMIN) {
            throw new ForbiddenException("Access forbidden.");
        }

        return ResponseEntity.ok(userService.getUserInfo(userId));
    }

    @PutMapping("/update-role/{userId}")
    public ResponseEntity<GenericResponse> setUserRole(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @PathVariable Integer userId,
            @RequestBody UserFieldsToUpdateDto userFieldsToUpdateDto) {
        var token = TokenExtractor.extractFromHeader(authHeader);
        var role = jwtClaimExtractor.extractUserRole(token);
        if (role != UserRole.ADMIN) {
            throw new ForbiddenException("Access forbidden.");
        }

        var callerId = jwtClaimExtractor.extractUserId(token);
        if (callerId.equals(userId)) {
            throw new BadRequestException("Can not change own role.");
        }

        return ResponseEntity.ok(userService.updateUserRole(userId, userFieldsToUpdateDto.getUserRole()));
    }
}
