package br.market.flow.order.application.api;

import br.market.flow.order.application.usecase.CancelOrderUseCase;
import br.market.flow.order.application.usecase.CreateOrderUseCase;
import br.market.flow.order.application.usecase.GetOrderUseCase;
import br.market.flow.order.domain.Order;
import br.market.flow.order.application.api.dto.CancelOrderResponse;
import br.market.flow.order.application.api.dto.CreateOrderRequest;
import br.market.flow.order.application.api.dto.CreateOrderResponse;
import br.market.flow.order.application.api.dto.OrderResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderResource {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;

    public OrderResource(
            CreateOrderUseCase createOrderUseCase,
            GetOrderUseCase getOrderUseCase,
            CancelOrderUseCase cancelOrderUseCase
    ) {
        this.createOrderUseCase = createOrderUseCase;
        this.getOrderUseCase = getOrderUseCase;
        this.cancelOrderUseCase = cancelOrderUseCase;
    }

    @PostMapping
    public ResponseEntity<CreateOrderResponse> create(
            @RequestHeader(value = "X-Idempotency-Key", required = false) String idempotencyKey,
            @Valid @RequestBody CreateOrderRequest request
    ) {
        // TODO: persist and add X-Idempotency-Key in a later step
        Order order = createOrderUseCase.execute(request.toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateOrderResponse(order.getId()));
    }

    @GetMapping("/{orderId}")
    public OrderResponse get(@PathVariable String orderId) {
        return toResponse(getOrderUseCase.execute(orderId));
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<CancelOrderResponse> cancel(@PathVariable String orderId) {
        return ResponseEntity.accepted().body(new CancelOrderResponse(cancelOrderUseCase.execute(orderId)));
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getUserId(),
                order.getItems().stream()
                        .map(item -> new OrderResponse.OrderItemResponse(
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
}
