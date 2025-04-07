package org.skypro.skyshop.model.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.skypro.skyshop.model.search.Searchable;

import java.util.Objects;
import java.util.UUID;

public abstract class Product implements Searchable {
    private final UUID id;
    private final String name;

    public Product(UUID id, String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя продукта не может быть null или пустым");
        }
        this.id = id;
        this.name = name;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public abstract int getCost();

    public boolean isSpecial() {
        return false;
    }

    @Override
    public String toString() {
        return name + ": " + getCost();
    }

    @JsonIgnore
    @Override
    public String getSearchTerm() {
        return getName();
    }

    @JsonIgnore
    @Override
    public String getContentType() {
        return "PRODUCT";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
