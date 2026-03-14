package org.skypro.skyshop.model.basket;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import java.util.*;

@SessionScope
@Component
public class ProductBasket {
    private final Map<UUID, Integer> basketProducts = new HashMap<>();

    public void addProduct(UUID id) {
        basketProducts.computeIfAbsent(id, key -> 0);
        basketProducts.put(id, basketProducts.get(id) + 1);
    }

    public Map<UUID, Integer> getBasketProducts() {
        return Collections.unmodifiableMap(basketProducts);
    }
}