package ru.len4ass.api.models.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.len4ass.api.models.dish.DishDto;

import java.util.List;

public class OrderCreateDto {
    @JsonProperty("dishes")
    private List<DishDto> dishes;

    @JsonProperty("special_requests")
    private String specialRequests;

    public OrderCreateDto() {

    }
    public OrderCreateDto(List<DishDto> dishes, String specialRequests) {
        this.dishes = dishes;
        this.specialRequests = specialRequests;
    }
    public List<DishDto> getDishes() {
        return dishes;
    }
    public String getSpecialRequests() {
        return specialRequests;
    }
}
