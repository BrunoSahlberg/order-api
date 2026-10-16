package br.market.flow.order.application.api.dto;

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

    public record OrderItemResponse(
            String productId,
            BigDecimal productPrice,
            int productQuantity
    ) {
    }
}
