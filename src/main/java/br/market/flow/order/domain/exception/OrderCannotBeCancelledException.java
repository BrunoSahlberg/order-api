package br.market.flow.order.domain.exception;

public class OrderCannotBeCancelledException extends RuntimeException {

    public OrderCannotBeCancelledException(String orderId) {
        super("Order cannot be cancelled: " + orderId);
    }
}
