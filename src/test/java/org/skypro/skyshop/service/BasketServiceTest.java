package org.skypro.skyshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.skyshop.model.basket.BasketItem;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.exceptions.NoSuchProductException;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.service.BasketService;
import org.skypro.skyshop.model.service.StorageService;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тестирование BasketService")
class BasketServiceTest {

    @Mock
    private ProductBasket productBasket;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private BasketService basketService;

    private UUID validProductId;
    private UUID invalidProductId;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        validProductId = UUID.randomUUID();
        invalidProductId = UUID.randomUUID();
        testProduct = new Product(validProductId, "TestProduct", 1000);
    }

    // ==================== ТЕСТЫ addProductToBasket ====================

    @Test
    @DisplayName("Добавление несуществующего товара - выбрасывается исключение")
    void addProductToBasket_WhenProductNotFound_ShouldThrowException() {
        when(storageService.getProductById(invalidProductId))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchProductException.class,
                () -> basketService.addProductToBasket(invalidProductId));

        verify(storageService, times(1)).getProductById(invalidProductId);
        verify(productBasket, never()).addProduct(any());
    }

    @Test
    @DisplayName("Добавление существующего товара - вызывается addProduct у ProductBasket")
    void addProductToBasket_WhenProductExists_ShouldCallProductBasketAddProduct() {
        when(storageService.getProductById(validProductId))
                .thenReturn(Optional.of(testProduct));

        basketService.addProductToBasket(validProductId);

        verify(storageService, times(1)).getProductById(validProductId);
        verify(productBasket, times(1)).addProduct(validProductId);
    }

    @Test
    @DisplayName("Добавление существующего товара несколько раз - множественные вызовы")
    void addProductToBasket_WhenCalledMultipleTimes_ShouldCallProductBasketMultipleTimes() {
        when(storageService.getProductById(validProductId))
                .thenReturn(Optional.of(testProduct));

        basketService.addProductToBasket(validProductId);
        basketService.addProductToBasket(validProductId);
        basketService.addProductToBasket(validProductId);

        verify(productBasket, times(3)).addProduct(validProductId);
    }

    // ==================== ТЕСТЫ getUserBasket ====================

    @Test
    @DisplayName("Получение корзины - корзина пуста")
    void getUserBasket_WhenBasketEmpty_ShouldReturnEmptyBasket() {
        when(productBasket.getBasketProducts()).thenReturn(Collections.emptyMap());

        UserBasket userBasket = basketService.getUserBasket();

        assertThat(userBasket.getItems()).isEmpty();
        assertThat(userBasket.getTotal()).isZero();
        verify(productBasket, times(1)).getBasketProducts();
        verify(storageService, never()).getProductById(any());
    }

    @Test
    @DisplayName("Получение корзины - корзина содержит один товар")
    void getUserBasket_WhenBasketHasOneProduct_ShouldReturnCorrectBasket() {
        Map<UUID, Integer> basketMap = new HashMap<>();
        basketMap.put(validProductId, 3);

        when(productBasket.getBasketProducts()).thenReturn(basketMap);
        when(storageService.getProductById(validProductId))
                .thenReturn(Optional.of(testProduct));

        UserBasket userBasket = basketService.getUserBasket();

        assertThat(userBasket.getItems()).hasSize(1);
        assertThat(userBasket.getTotal()).isEqualTo(1000 * 3);

        BasketItem item = userBasket.getItems().get(0);
        assertThat(item.getProduct()).isEqualTo(testProduct);
        assertThat(item.getQuantity()).isEqualTo(3);

        verify(productBasket, times(1)).getBasketProducts();
        verify(storageService, times(1)).getProductById(validProductId);
    }

    @Test
    @DisplayName("Получение корзины - корзина содержит несколько товаров")
    void getUserBasket_WhenBasketHasMultipleProducts_ShouldReturnCorrectBasket() {
        UUID productId2 = UUID.randomUUID();
        Product product2 = new Product(productId2, "Product2", 500);

        Map<UUID, Integer> basketMap = new HashMap<>();
        basketMap.put(validProductId, 2);
        basketMap.put(productId2, 4);

        when(productBasket.getBasketProducts()).thenReturn(basketMap);
        when(storageService.getProductById(validProductId))
                .thenReturn(Optional.of(testProduct));
        when(storageService.getProductById(productId2))
                .thenReturn(Optional.of(product2));

        UserBasket userBasket = basketService.getUserBasket();

        assertThat(userBasket.getItems()).hasSize(2);
        int expectedTotal = (1000 * 2) + (500 * 4);
        assertThat(userBasket.getTotal()).isEqualTo(expectedTotal);

        verify(productBasket, times(1)).getBasketProducts();
        verify(storageService, times(1)).getProductById(validProductId);
        verify(storageService, times(1)).getProductById(productId2);
    }

    @Test
    @DisplayName("Получение корзины - продукт из корзины отсутствует в StorageService")
    void getUserBasket_WhenProductMissingInStorage_ShouldThrowException() {
        Map<UUID, Integer> basketMap = new HashMap<>();
        basketMap.put(invalidProductId, 1);

        when(productBasket.getBasketProducts()).thenReturn(basketMap);
        when(storageService.getProductById(invalidProductId))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchProductException.class,
                () -> basketService.getUserBasket());

        verify(productBasket, times(1)).getBasketProducts();
        verify(storageService, times(1)).getProductById(invalidProductId);
    }
}