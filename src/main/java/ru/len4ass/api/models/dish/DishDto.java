package ru.len4ass.api.models.dish;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.len4ass.api.models.order.OrderStatus;

public class DishDto {
    @JsonProperty("dish_id")
    private Integer dishId;

    @JsonProperty("quantity")
    private Integer quantity;

    public DishDto() {

    }

    public DishDto(Integer dishId, Integer quantity) {
        this.dishId = dishId;
        this.quantity = quantity;
    }

    public Integer getDishId() {
        return dishId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
