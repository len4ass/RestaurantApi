package ru.len4ass.api.models.dish;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DishCrudOperationResponse {
    @JsonProperty("info")
    private String infoMessage;

    @JsonProperty("dish_id")
    private Integer dishId;

    public DishCrudOperationResponse(String infoMessage, Integer dishId) {
        this.infoMessage = infoMessage;
        this.dishId = dishId;
    }

    public String getInfoMessage() {
        return infoMessage;
    }

    public Integer getDishId() {
        return dishId;
    }
}
