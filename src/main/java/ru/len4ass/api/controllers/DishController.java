package ru.len4ass.api.controllers;

import jakarta.annotation.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.len4ass.api.auth.JwtClaimExtractor;
import ru.len4ass.api.exceptions.ForbiddenException;
import ru.len4ass.api.models.dish.DishCrudOperationResponse;
import ru.len4ass.api.models.dish.DishModelDto;
import ru.len4ass.api.models.user.UserRole;
import ru.len4ass.api.services.DishService;
import ru.len4ass.api.utils.TokenExtractor;

@RestController
@RequestMapping("/api/dishes")
public class DishController {
    @Resource
    private final DishService dishService;

    @Resource
    private final JwtClaimExtractor jwtClaimExtractor;

    public DishController(DishService dishService, JwtClaimExtractor jwtClaimExtractor) {
        this.dishService = dishService;
        this.jwtClaimExtractor = jwtClaimExtractor;
    }

    // Again, I couldn't care less about Sprint security filters, they're trash
    private boolean callerHasAccess(String jwtToken) {
        var role = jwtClaimExtractor.extractUserRole(jwtToken);
        return role == UserRole.ADMIN || role == UserRole.MANAGER;
    }

    @GetMapping("/{dishId}")
    public ResponseEntity<DishModelDto> getDish(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                                @PathVariable Integer dishId) {
        var token = TokenExtractor.extractFromHeader(authHeader);
        if (!callerHasAccess(token)) {
            throw new ForbiddenException("Access forbidden.");
        }

        return ResponseEntity.ok(dishService.getDishById(dishId));
    }

    @PostMapping("")
    public ResponseEntity<DishCrudOperationResponse> createDish(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                                                @RequestBody DishModelDto dishModelDto) {
        var token = TokenExtractor.extractFromHeader(authHeader);
        if (!callerHasAccess(token)) {
            throw new ForbiddenException("Access forbidden.");
        }

        return ResponseEntity.ok(dishService.createDish(dishModelDto));
    }

    @PutMapping("/{dishId}")
    public ResponseEntity<DishCrudOperationResponse> updateDish(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                                                @RequestBody DishModelDto dishModelDto,
                                                                @PathVariable Integer dishId) {
        var token = TokenExtractor.extractFromHeader(authHeader);
        if (!callerHasAccess(token)) {
            throw new ForbiddenException("Access forbidden.");
        }

        return ResponseEntity.ok(dishService.updateDishById(dishId, dishModelDto));
    }

    @DeleteMapping("/{dishId}")
    public ResponseEntity<DishCrudOperationResponse> deleteDish(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                                                @PathVariable Integer dishId) {
        var token = TokenExtractor.extractFromHeader(authHeader);
        if (!callerHasAccess(token)) {
            throw new ForbiddenException("Access forbidden.");
        }

        return ResponseEntity.ok(dishService.removeDishById(dishId));
    }
}
