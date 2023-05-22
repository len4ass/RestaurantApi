package ru.len4ass.api.utils;

import ru.len4ass.api.models.dish.Dish;
import ru.len4ass.api.models.dish.DishDto;
import ru.len4ass.api.models.dish.DishModelDto;
import ru.len4ass.api.models.order.OrderDish;
import ru.len4ass.api.models.user.User;
import ru.len4ass.api.models.user.UserDto;

public class Mapper {
    public static DishDto orderDishToDishDto(OrderDish orderDish) {
        return new DishDto(orderDish.getDish().getId(), orderDish.getQuantity());
    }

    public static DishModelDto dishToDishModelDto(Dish dish) {
        return new DishModelDto(dish.getName(), dish.getDescription(), dish.getPrice(), dish.getQuantity());
    }

    public static UserDto userToUserDto(User user) {
        return new UserDto(user.getUsername(), user.getEmail(), user.getUserRole());
    }
}
