package org.skypro.skyshop.model.service;


import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.search.Searchable;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class StorageService {
    private final Map<UUID, Product> productStorage;
    private final Map<UUID, Article> articleStorage;

    public StorageService() {
        this.productStorage = new HashMap<>();
        this.articleStorage = new HashMap<>();
        fillTestData();
    }

    private void fillTestData() {
        productStorage.put(UUID.randomUUID(), new Product(UUID.randomUUID(), "Ноутбук Lenovo", 75000));
        productStorage.put(UUID.randomUUID(), new Product(UUID.randomUUID(), "Беспроводная мышь", 1500));
        productStorage.put(UUID.randomUUID(), new Product(UUID.randomUUID(), "Смартфон Samsung", 35000));
        productStorage.put(UUID.randomUUID(), new Product(UUID.randomUUID(), "Беспроводные наушники", 8000));
        productStorage.put(UUID.randomUUID(), new Product(UUID.randomUUID(), "Кабель USB-C 2м", 5000));

        articleStorage.put(UUID.randomUUID(), new Article(UUID.randomUUID(),
                "Как выбрать ноутбук",
                "В этом руководстве мы расскажем, на что обратить внимание при выборе ноутбука"));
        articleStorage.put(UUID.randomUUID(), new Article(UUID.randomUUID(),
                "Преимущества беспроводных наушников",
                "Беспроводные наушники обеспечивают свободу движения"));
        articleStorage.put(UUID.randomUUID(), new Article(UUID.randomUUID(),
                "Обзор смартфонов 2024",
                "Лучшие смартфоны этого года"));
    }

    public Collection<Product> getAllProducts() {
        return productStorage.values();
    }

    public Collection<Article> getAllArticles() {
        return articleStorage.values();
    }

    public Collection<Searchable> getAllSearchable() {
        List<Searchable> searchables = new ArrayList<>();
        searchables.addAll(productStorage.values());
        searchables.addAll(articleStorage.values());
        return searchables;
    }
}