package ru.len4ass.api.repositories;

import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ru.len4ass.api.models.dish.Dish;
import ru.len4ass.api.models.order.Order;
import ru.len4ass.api.models.order.OrderDish;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface OrderDishRepository extends JpaRepository<OrderDish, Integer> {
    List<OrderDish> findOrderDishesByOrder(Order order);

    List<OrderDish> findOrderDishesByDish(Dish dish);
}
