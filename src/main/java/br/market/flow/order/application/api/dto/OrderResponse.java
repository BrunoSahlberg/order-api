
package br.market.flow.order.application.api.dto;

import br.market.flow.order.domain.Order;
import br.market.flow.order.domain.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponse(
        String userId,
        List<OrderItemResponse> items,
        String address,
        String paymentMethod,
        OrderStatus status
) {

    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getUserId(),
                order.getItems().stream()
                        .map(item -> new OrderItemResponse(
                                item.getProductId(),
                                item.getProductPrice(),
                                item.getProductQuantity()
                        ))
                        .toList(),
                order.getAddress(),
                order.getPaymentMethod(),
                order.getStatus()
        );
    }

    public record OrderItemResponse(
            String productId,
            BigDecimal productPrice,
            int productQuantity
    ) {
    }
}
