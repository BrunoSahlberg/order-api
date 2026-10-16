package br.market.flow.order.domain;

@FunctionalInterface
public interface Commandable <I, O> {
    O toCommand(final I input);
    default O toCommand() {
        return null;
    }
}
