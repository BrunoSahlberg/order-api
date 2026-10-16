package br.market.flow.order.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Item {

    private final String productId;
    private final BigDecimal productPrice;
    private final int productQuantity;

    public Item(String productId, BigDecimal productPrice, int productQuantity) {
        this.productId = Objects.requireNonNull(productId, "productId is required");
        this.productPrice = Objects.requireNonNull(productPrice, "productPrice is required");
        this.productQuantity = productQuantity;
    }

    public String getProductId() {
        return productId;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public BigDecimal lineTotal() {
        return productPrice.multiply(BigDecimal.valueOf(productQuantity));
    }
}
