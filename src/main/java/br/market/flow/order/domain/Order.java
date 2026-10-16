package br.market.flow.order.domain;

import br.market.flow.order.domain.exception.OrderCannotBeCancelledException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class Order {

    private final String id;
    private final String userId;
    private final List<Item> items;
    private final String address;
    private final String paymentMethod;
    private OrderStatus status;

    public Order(
            String id,
            String userId,
            List<Item> items,
            String address,
            String paymentMethod,
            OrderStatus status
    ) {
        this.id = Objects.requireNonNull(id, "id is required");
        this.userId = Objects.requireNonNull(userId, "userId is required");
        this.items = List.copyOf(Objects.requireNonNull(items, "items is required"));
        this.address = Objects.requireNonNull(address, "address is required");
        this.paymentMethod = Objects.requireNonNull(paymentMethod, "paymentMethod is required");
        this.status = Objects.requireNonNull(status, "status is required");
    }

    public void cancel() {
        if (status == OrderStatus.CANCELLED) {
            throw new OrderCannotBeCancelledException(id);
        }
        this.status = OrderStatus.CANCELLED;
    }

    public BigDecimal totalAmount() {
        return items.stream()
                .map(Item::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public List<Item> getItems() {
        return items;
    }

    public String getAddress() {
        return address;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
