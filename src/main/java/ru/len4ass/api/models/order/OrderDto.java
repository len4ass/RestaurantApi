package ru.len4ass.api.models.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.len4ass.api.models.dish.DishDto;

import java.util.List;

public class OrderDto {
    @JsonProperty("order_id")
    private Integer orderId;

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("order_status")
    private OrderStatus orderStatus;

    @JsonProperty("ordered_dishes")
    private List<DishDto> orderedDishes;

    public OrderDto(Integer orderId, Integer userId, OrderStatus orderStatus, List<DishDto> orderedDishes) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderStatus = orderStatus;
        this.orderedDishes = orderedDishes;
    }
    public Integer getOrderId() {
        return orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<DishDto> getOrderedDishes() {
        return orderedDishes;
    }
}
