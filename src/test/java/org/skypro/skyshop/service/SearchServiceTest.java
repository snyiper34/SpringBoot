package org.skypro.skyshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.search.SearchResult;
import org.skypro.skyshop.model.search.Searchable;
import org.skypro.skyshop.model.service.SearchService;
import org.skypro.skyshop.model.service.StorageService;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тестирование SearchService")
class SearchServiceTest {

    @Mock
    private StorageService storageService;

    @InjectMocks
    private SearchService searchService;

    private Product testProduct;
    private Article testArticle;

    @BeforeEach
    void setUp() {
        testProduct = new Product(UUID.randomUUID(), "TestProduct", 1000);
        testArticle = new Article(UUID.randomUUID(), "Test Article", "This is a test article");
    }

    @Test
    @DisplayName("Поиск при пустом хранилище - возвращает пустую коллекцию")
    void search_WhenStorageIsEmpty_ShouldReturnEmptyCollection() {
        when(storageService.getAllSearchable()).thenReturn(Collections.emptyList());

        Collection<SearchResult> results = searchService.search("Test");

        assertThat(results).isEmpty();
        verify(storageService, times(1)).getAllSearchable();
    }

    @Test
    @DisplayName("Поиск при непустом хранилище и отсутствии совпадений - возвращает пустую коллекцию")
    void search_WhenNoMatches_ShouldReturnEmptyCollection() {
        List<Searchable> searchables = Arrays.asList(testProduct, testArticle);
        when(storageService.getAllSearchable()).thenReturn(searchables);

        Collection<SearchResult> results = searchService.search("NonExistentPattern");

        assertThat(results).isEmpty();
        verify(storageService, times(1)).getAllSearchable();
    }

    @Test
    @DisplayName("Поиск с хранилищем, содержащим подходящий объект - возвращает список размером 1")
    void search_WhenMatchExists_ShouldReturnSingleResult() {
        List<Searchable> searchables = Collections.singletonList(testProduct);
        when(storageService.getAllSearchable()).thenReturn(searchables);

        Collection<SearchResult> results = searchService.search("Test");

        assertThat(results).hasSize(1);
        SearchResult result = results.iterator().next();
        assertThat(result.getName()).isEqualTo("TestProduct");
        verify(storageService, times(1)).getAllSearchable();
    }

    @Test
    @DisplayName("Поиск с несколькими подходящими объектами - возвращает все совпадения")
    void search_WhenMultipleMatchesExist_ShouldReturnAllMatches() {
        Product anotherProduct = new Product(UUID.randomUUID(), "TestAnother", 2000);
        List<Searchable> searchables = Arrays.asList(testProduct, anotherProduct, testArticle);
        when(storageService.getAllSearchable()).thenReturn(searchables);

        Collection<SearchResult> results = searchService.search("Test");

        assertThat(results).hasSize(3);
        verify(storageService, times(1)).getAllSearchable();
    }
}