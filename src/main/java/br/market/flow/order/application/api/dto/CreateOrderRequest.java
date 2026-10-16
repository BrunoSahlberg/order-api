package br.market.flow.order.application.api.dto;

import br.market.flow.order.application.usecase.CreateOrderUseCase;
import br.market.flow.order.domain.Commandable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record CreateOrderRequest(
        @NotBlank String userId,
        @NotEmpty List<@Valid CreateOrderItemRequest> items,
        @NotBlank String address,
        @NotBlank String paymentMethod
) implements Commandable<CreateOrderRequest, CreateOrderUseCase.Command> {

    @Override
    public CreateOrderUseCase.Command toCommand(final CreateOrderRequest input) {
        return new CreateOrderUseCase.Command(
                input.userId(),
                input.items().stream()
                        .map(item -> new CreateOrderUseCase.ItemCommand(
                                item.productId(),
                                item.productPrice(),
                                item.productQuantity()
                        ))
                        .toList(),
                input.address(),
                input.paymentMethod()
        );
    }

    public record CreateOrderItemRequest(
            @NotBlank String productId,
            @NotNull @Positive BigDecimal productPrice,
            @Positive int productQuantity
    ) {
    }
}
