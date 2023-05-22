package ru.len4ass.api.controllers;

import jakarta.annotation.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.len4ass.api.auth.JwtClaimExtractor;
import ru.len4ass.api.models.order.OrderCreateDto;
import ru.len4ass.api.models.order.OrderCreationResponse;
import ru.len4ass.api.models.order.OrderDto;
import ru.len4ass.api.services.OrderService;
import ru.len4ass.api.utils.TokenExtractor;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Resource
    private final JwtClaimExtractor jwtClaimExtractor;

    @Resource
    private final OrderService orderService;

    public OrderController(JwtClaimExtractor jwtClaimExtractor, OrderService orderService) {
        this.jwtClaimExtractor = jwtClaimExtractor;
        this.orderService = orderService;
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderInfo(@PathVariable Integer orderId) {
        return ResponseEntity.ok(orderService.getOrderInfoById(orderId));
    }

    @PostMapping("")
    public ResponseEntity<OrderCreationResponse> createOrder(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                                             @RequestBody OrderCreateDto request) {
        var token = TokenExtractor.extractFromHeader(authHeader);
        var id = jwtClaimExtractor.extractUserId(token);
        return ResponseEntity.ok(orderService.createOrder(id, request));
    }

}
