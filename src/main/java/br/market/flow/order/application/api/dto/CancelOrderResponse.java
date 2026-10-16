package br.market.flow.order.application.api.dto;

import br.market.flow.order.domain.OrderStatus;

public record CancelOrderResponse(OrderStatus status) {
}
