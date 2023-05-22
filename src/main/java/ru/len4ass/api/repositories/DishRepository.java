package ru.len4ass.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.len4ass.api.models.dish.Dish;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Integer> {
    List<Dish> findDishesByQuantityAfter(Integer quantity);
}
