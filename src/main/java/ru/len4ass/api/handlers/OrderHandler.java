package ru.len4ass.api.handlers;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import ru.len4ass.api.events.OrderCreatedEvent;
import ru.len4ass.api.models.order.OrderStatus;
import ru.len4ass.api.repositories.OrderRepository;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class OrderHandler implements ApplicationListener<OrderCreatedEvent> {
    private final Logger logger = LoggerFactory.getLogger(OrderHandler.class);

    @Resource
    private final OrderRepository orderRepository;

    public OrderHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void onApplicationEvent(OrderCreatedEvent event) {
        var order = event.getOrder();

        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(500, 1000));
        } catch (InterruptedException ignored) {
            order.setStatus(OrderStatus.CANCELLED);
            order.setUpdatedAt(System.currentTimeMillis());
            orderRepository.saveAndFlush(order);
            logger.info("Interrupted order processing thread. Cancelled order {}.", order.getId());
            return;
        }

        order.setStatus(OrderStatus.PROCESSING);
        order.setUpdatedAt(System.currentTimeMillis());
        order = orderRepository.saveAndFlush(order);
        logger.info("Processing order with id {}", order.getId());

        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(7500, 10000));
        } catch (InterruptedException ignored) {
            order.setStatus(OrderStatus.CANCELLED);
            order.setUpdatedAt(System.currentTimeMillis());
            orderRepository.saveAndFlush(order);
            logger.info("Interrupted order processing thread. Cancelled order {}.", order.getId());
        }

        order.setStatus(OrderStatus.DONE);
        order.setUpdatedAt(System.currentTimeMillis());
        order = orderRepository.saveAndFlush(order);
        logger.info("Order with id {} is ready!", order.getId());
    }
}
