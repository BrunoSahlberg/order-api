package br.market.flow.order.application.usecase;

import br.market.flow.order.domain.Item;
import br.market.flow.order.domain.Order;
import br.market.flow.order.domain.OrderRepository;
import br.market.flow.order.domain.OrderStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class CreateOrderUseCase {

    private final OrderRepository orderRepository;

    public CreateOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order execute(Command command) {
        List<Item> items = command.items().stream()
                .map(item -> new Item(item.productId(), item.productPrice(), item.productQuantity()))
                .toList();

        Order order = new Order(
                UUID.randomUUID().toString(),
                command.userId(),
                items,
                command.address(),
                command.paymentMethod(),
                OrderStatus.CREATED
        );

        return orderRepository.save(order);
    }

    public record Command(
            String userId,
            List<ItemCommand> items,
            String address,
            String paymentMethod
    ) {
    }

    public record ItemCommand(
            String productId,
            BigDecimal productPrice,
            int productQuantity
    ) {
    }
}
