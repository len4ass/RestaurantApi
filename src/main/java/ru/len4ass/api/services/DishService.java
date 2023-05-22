package ru.len4ass.api.services;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import ru.len4ass.api.exceptions.BadRequestException;
import ru.len4ass.api.exceptions.NotFoundException;
import ru.len4ass.api.models.dish.Dish;
import ru.len4ass.api.models.dish.DishCrudOperationResponse;
import ru.len4ass.api.models.dish.DishModelDto;
import ru.len4ass.api.repositories.DishRepository;
import ru.len4ass.api.repositories.OrderDishRepository;
import ru.len4ass.api.utils.Mapper;

import java.math.BigDecimal;

@Service
public class DishService {
    @Resource
    private final DishRepository dishRepository;

    @Resource
    private final OrderDishRepository orderDishRepository;

    public DishService(DishRepository dishRepository, OrderDishRepository orderDishRepository) {
        this.dishRepository = dishRepository;
        this.orderDishRepository = orderDishRepository;
    }

    public DishModelDto getDishById(Integer id) {
        var dish = dishRepository.findById(id);
        if (dish.isEmpty()) {
            throw new NotFoundException(String.format("No dish with %d id found.", id));
        }

        return Mapper.dishToDishModelDto(dish.get());
    }

    public DishCrudOperationResponse createDish(DishModelDto dishModel) {
        var equality = dishModel.getPrice().compareTo(BigDecimal.ZERO);
        if (equality <= 0) {
            throw new BadRequestException("Can't create dish with zero or negative price.");
        }

        if (dishModel.getQuantity() < 0) {
            throw new BadRequestException("Can't create dish with negative quantity.");
        }

        var dish = new Dish(dishModel.getName(), dishModel.getDescription(), dishModel.getPrice(), dishModel.getQuantity());
        dish = dishRepository.saveAndFlush(dish);

        return new DishCrudOperationResponse("Created dish successfully.", dish.getId());
    }

    public DishCrudOperationResponse updateDishById(Integer id, DishModelDto dishModel) {
        var optionalDish = dishRepository.findById(id);
        if (optionalDish.isEmpty()) {
            throw new NotFoundException(String.format("No dish with %d id found.", id));
        }

        var equality = dishModel.getPrice().compareTo(BigDecimal.ZERO);
        if (equality <= 0) {
            throw new BadRequestException("Can't update dish with zero or negative price.");
        }

        if (dishModel.getQuantity() < 0) {
            throw new BadRequestException("Can't update dish with negative quantity.");
        }

        var dish = optionalDish.get();
        dish.setName(dishModel.getName());
        dish.setDescription(dishModel.getDescription());
        dish.setPrice(dishModel.getPrice());
        dish.setQuantity(dishModel.getQuantity());
        dishRepository.saveAndFlush(dish);

        return new DishCrudOperationResponse(String.format("Updated dish with id %d successfully.", id), id);
    }

    public DishCrudOperationResponse removeDishById(Integer id) {
        var optionalDish = dishRepository.findById(id);
        if (optionalDish.isEmpty()) {
            throw new NotFoundException("Dish does not exist.");
        }

        var dish = optionalDish.get();
        var ordersWithDish = orderDishRepository.findOrderDishesByDish(dish);
        if (!ordersWithDish.isEmpty()) {
            throw new BadRequestException("There are still orders waiting on that dish.");
        }

        dishRepository.delete(dish);
        return new DishCrudOperationResponse(String.format("Dish with id %d deleted successfully.", id), id);
    }
}
