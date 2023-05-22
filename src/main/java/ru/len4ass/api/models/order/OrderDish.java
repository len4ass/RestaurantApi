package ru.len4ass.api.models.order;

import jakarta.persistence.*;
import ru.len4ass.api.models.dish.Dish;

import java.math.BigDecimal;

@Entity
@Table(name = "order_dish")
public class OrderDish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", referencedColumnName = "id")
    private Dish dish;

    private Integer quantity;

    @Column(columnDefinition = "decimal")
    private BigDecimal price;

    public OrderDish() {

    }

    public OrderDish(Order order, Dish dish, Integer quantity, BigDecimal price) {
        this(0, order, dish, quantity, price);
    }

    public OrderDish(Integer id, Order order, Dish dish, Integer quantity, BigDecimal price) {
        this.id = id;
        this.order = order;
        this.dish = dish;
        this.quantity = quantity;
        this.price = price;
    }

    public Order getOrder() {
        return order;
    }

    public Dish getDish() {
        return dish;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
