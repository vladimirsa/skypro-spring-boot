package org.skypro.skyshop.service;

import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.DiscountedProduct;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.product.SimpleProduct;
import org.springframework.stereotype.Service;
import org.skypro.skyshop.model.search.Searchable;

import java.util.Optional;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class StorageService {
    private final Map<UUID, Product> productStorage;
    private final Map<UUID, Article> articleStorage;

    public StorageService() {
        this.productStorage = new HashMap<>();
        this.articleStorage = new HashMap<>();
        enterData();
    }

    public Collection<Product> getAllProducts() {
        return productStorage.values();
    }

    public Collection<Article> getAllArticles() {
        return articleStorage.values();
    }

    public Optional<Product> getProductById(UUID id) {
        return Optional.ofNullable(productStorage.get(id));
    }

    private void enterData() {
        Product product1 = new SimpleProduct(UUID.randomUUID(), "Хлеб", 100);
        Product product2 = new DiscountedProduct(UUID.randomUUID(), "Сыр", 200, 20);
        productStorage.put(product1.getId(), product1);
        productStorage.put(product2.getId(), product2);

        Article article1 = new Article(UUID.randomUUID(), "Статья о хлебе", "Я знаю всё о хлебе");
        Article article2 = new Article(UUID.randomUUID(), "Статья о сыре", "Я знаю всё о сыре");
        articleStorage.put(article1.getId(), article1);
        articleStorage.put(article2.getId(), article2);
    }

    public Collection<Searchable> getAllSearchables() {
        Collection<Searchable> searchables = new ArrayList<>();

        searchables.addAll(getAllProducts().stream()
                .map(product -> (Searchable) product)
                .toList());

        searchables.addAll(getAllArticles().stream()
                .map(article -> (Searchable) article)
                .toList());

        return searchables;
    }


}