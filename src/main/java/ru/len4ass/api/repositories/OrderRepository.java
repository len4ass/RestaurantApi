package ru.len4ass.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ru.len4ass.api.models.order.Order;
import ru.len4ass.api.models.user.User;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Optional<Order> findOrderByUser(User user);
}
