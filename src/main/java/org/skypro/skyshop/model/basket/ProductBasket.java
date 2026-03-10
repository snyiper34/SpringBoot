package org.skypro.skyshop.model.basket;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import java.util.*;

@SessionScope
@Component
public class ProductBasket {
    private final Map<UUID, Integer> basketProducts = new HashMap<>();

    public void addProduct(UUID id) {
        basketProducts.put(id, basketProducts.getOrDefault(id, 0) + 1);
    }

    public Map<UUID, Integer> getBasketProducts() {
        return Collections.unmodifiableMap(basketProducts);
    }
}