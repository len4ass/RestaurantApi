package ru.len4ass.api.events;

import org.springframework.context.ApplicationEvent;
import ru.len4ass.api.models.order.Order;

public class OrderCreatedEvent extends ApplicationEvent {
    private final Order order;

    public OrderCreatedEvent(Object source, Order order) {
        super(source);
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
