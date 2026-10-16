package br.market.flow.order.infrastructure.persistence;

import br.market.flow.order.domain.Item;
import br.market.flow.order.domain.Order;
import br.market.flow.order.domain.OrderStatus;

import java.util.UUID;

public class OrderPersistenceMapper {

    public OrderEntity toEntity(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId());
        entity.setUserId(order.getUserId());
        entity.setAddress(order.getAddress());
        entity.setPaymentMethod(order.getPaymentMethod());
        entity.setTotalAmount(order.totalAmount());
        entity.setStatus(OrderStatusEntity.valueOf(order.getStatus().name()));
        entity.setItems(order.getItems().stream().map(this::toItemEntity).toList());
        return entity;
    }

    public OrderEntity updateEntity(OrderEntity entity, Order order) {
        entity.setUserId(order.getUserId());
        entity.setAddress(order.getAddress());
        entity.setPaymentMethod(order.getPaymentMethod());
        entity.setTotalAmount(order.totalAmount());
        entity.setStatus(OrderStatusEntity.valueOf(order.getStatus().name()));
        entity.setItems(order.getItems().stream().map(this::toItemEntity).toList());
        return entity;
    }

    public Order toDomain(OrderEntity entity) {
        return new Order(
                entity.getId(),
                entity.getUserId(),
                entity.getItems().stream().map(this::toItem).toList(),
                entity.getAddress(),
                entity.getPaymentMethod(),
                OrderStatus.valueOf(entity.getStatus().name())
        );
    }

    private ItemEntity toItemEntity(Item item) {
        ItemEntity entity = new ItemEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setProductId(item.getProductId());
        entity.setProductPrice(item.getProductPrice());
        entity.setProductQuantity(item.getProductQuantity());
        return entity;
    }

    private Item toItem(ItemEntity entity) {
        return new Item(entity.getProductId(), entity.getProductPrice(), entity.getProductQuantity());
    }
}
