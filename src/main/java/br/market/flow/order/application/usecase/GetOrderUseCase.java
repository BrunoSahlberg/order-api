package br.market.flow.order.application.usecase;

import br.market.flow.order.domain.Order;
import br.market.flow.order.domain.exception.OrderNotFoundException;
import br.market.flow.order.domain.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class GetOrderUseCase {

    private final OrderRepository orderRepository;

    public GetOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order execute(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }
}
