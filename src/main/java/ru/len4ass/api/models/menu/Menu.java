package ru.len4ass.api.models.menu;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.len4ass.api.models.dish.Dish;

import java.util.List;

public class Menu {
    @JsonProperty("dishes")
    private List<Dish> dishes;

    public Menu(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public List<Dish> getDishes() {
        return dishes;
    }
}
