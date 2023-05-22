package ru.len4ass.api.services;

import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.len4ass.api.events.OrderCreatedEvent;
import ru.len4ass.api.exceptions.BadRequestException;
import ru.len4ass.api.exceptions.NotFoundException;
import ru.len4ass.api.models.dish.DishDto;
import ru.len4ass.api.models.order.*;
import ru.len4ass.api.repositories.DishRepository;
import ru.len4ass.api.repositories.OrderDishRepository;
import ru.len4ass.api.repositories.OrderRepository;
import ru.len4ass.api.repositories.UserRepository;
import ru.len4ass.api.utils.Mapper;

import java.util.ArrayList;

@Service
public class OrderService {
    @Resource
    private final ApplicationEventPublisher applicationEventPublisher;

    @Resource
    private final UserRepository userRepository;

    @Resource
    private final OrderRepository orderRepository;

    @Resource
    private final OrderDishRepository orderDishRepository;

    @Resource
    private final DishRepository dishRepository;

    public OrderService(ApplicationEventPublisher applicationEventPublisher, UserRepository userRepository,
                        OrderRepository orderRepository,
                        OrderDishRepository orderDishRepository,
                        DishRepository dishRepository) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.orderDishRepository = orderDishRepository;
        this.dishRepository = dishRepository;
    }

    private void validatePlacedOrder(OrderCreateDto request) {
        var dishes = request.getDishes();
        if (dishes.isEmpty()) {
            throw new BadRequestException("You have not chosen any menu items in your order.");
        }

        for (var dish : dishes) {
            var dishInDb = dishRepository.findById(dish.getDishId());
            if (dishInDb.isEmpty()) {
                throw new BadRequestException(String.format("Dish with id %d doesn't exist", dish.getDishId()));
            }

            var dishQuantityInStorage = dishInDb.get().getQuantity();
            if (dishQuantityInStorage - dish.getQuantity() < 0) {
                throw new BadRequestException(String.format(
                        "Not enough dish with id %d. Requested: %d, available: %d",
                        dish.getDishId(),
                        dish.getQuantity(),
                        dishQuantityInStorage));
            }
        }
    }

    public OrderDto getOrderInfoById(Integer id) {
        var optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isEmpty()) {
            throw new NotFoundException(String.format("No order found with id %d", id));
        }

        var order = optionalOrder.get();
        var orderedDishes = orderDishRepository.findOrderDishesByOrder(order);
        var orderedDtoDishes = new ArrayList<DishDto>();
        for (var orderedDish : orderedDishes) {
            orderedDtoDishes.add(Mapper.orderDishToDishDto(orderedDish));
        }

        return new OrderDto(
                id,
                order.getUser().getId(),
                order.getStatus(),
                orderedDtoDishes);
    }

    @Transactional
    public OrderCreationResponse createOrder(Integer userId, OrderCreateDto request) {
        validatePlacedOrder(request);
        var user = userRepository.findById(userId).orElseThrow();
        var order = new Order(user, OrderStatus.WAITING, request.getSpecialRequests());
        order = orderRepository.saveAndFlush(order);

        var orderedDishes = new ArrayList<OrderDish>();
        var dishes = request.getDishes();
        for (var dish : dishes) {
            var dishInDb = dishRepository.findById(dish.getDishId()).orElseThrow();
            var orderedDish = new OrderDish(order, dishInDb, dish.getQuantity(), dishInDb.getPrice());

            dishInDb.setQuantity(dishInDb.getQuantity() - dish.getQuantity());
            dishRepository.save(dishInDb);
            orderedDishes.add(orderedDish);
        }

        orderDishRepository.saveAll(orderedDishes);

        var orderCreatedEvent = new OrderCreatedEvent(applicationEventPublisher, order);
        applicationEventPublisher.publishEvent(orderCreatedEvent);

        return new OrderCreationResponse("Created order successfully.", order.getId());
    }
}
