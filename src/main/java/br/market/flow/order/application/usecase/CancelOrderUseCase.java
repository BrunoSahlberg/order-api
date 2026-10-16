package br.market.flow.order.application.usecase;

import br.market.flow.order.domain.Order;
import br.market.flow.order.domain.exception.OrderNotFoundException;
import br.market.flow.order.domain.OrderRepository;
import br.market.flow.order.domain.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CancelOrderUseCase {

    private final OrderRepository orderRepository;

    public CancelOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderStatus execute(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        order.cancel();
        return orderRepository.save(order).getStatus();
    }
}
