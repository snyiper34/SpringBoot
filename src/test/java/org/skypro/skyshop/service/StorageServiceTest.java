package org.skypro.skyshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.search.Searchable;
import org.skypro.skyshop.model.service.StorageService;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование StorageService")
class StorageServiceTest {

    private StorageService storageService;

    @BeforeEach
    void setUp() {
        storageService = new StorageService();
    }

    @Test
    @DisplayName("getAllProducts возвращает непустую коллекцию")
    void getAllProducts_ShouldReturnNonEmptyCollection() {
        Collection<Product> products = storageService.getAllProducts();
        assertThat(products).isNotEmpty();
    }

    @Test
    @DisplayName("getAllArticles возвращает непустую коллекцию")
    void getAllArticles_ShouldReturnNonEmptyCollection() {
        Collection<Article> articles = storageService.getAllArticles();
        assertThat(articles).isNotEmpty();
    }

    @Test
    @DisplayName("getProductById для существующего ID возвращает продукт")
    void getProductById_WhenProductExists_ShouldReturnProduct() {
        Collection<Product> products = storageService.getAllProducts();
        UUID existingId = products.iterator().next().getId();

        Optional<Product> result = storageService.getProductById(existingId);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(existingId);
    }

    @Test
    @DisplayName("getProductById для несуществующего ID возвращает пустой Optional")
    void getProductById_WhenProductDoesNotExist_ShouldReturnEmptyOptional() {
        UUID nonExistingId = UUID.randomUUID();
        Optional<Product> result = storageService.getProductById(nonExistingId);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("getAllSearchable возвращает все продукты и статьи")
    void getAllSearchable_ShouldReturnAllProductsAndArticles() {
        Collection<Searchable> searchables = storageService.getAllSearchable();

        int productCount = storageService.getAllProducts().size();
        int articleCount = storageService.getAllArticles().size();

        assertThat(searchables).hasSize(productCount + articleCount);
    }
}