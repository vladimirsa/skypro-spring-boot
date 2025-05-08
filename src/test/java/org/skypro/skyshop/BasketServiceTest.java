package org.skypro.skyshop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skypro.skyshop.exception.NoSuchProductException;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.service.BasketService;
import org.skypro.skyshop.service.StorageService;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BasketServiceTest {

    private StorageService storageService;
    private ProductBasket productBasket;
    private BasketService basketService;

    @BeforeEach
    void setUp() {
        storageService = mock(StorageService.class);
        productBasket = mock(ProductBasket.class);
        basketService = new BasketService(productBasket, storageService);
    }

    @Test
    void addNonExistentProductToBasketThrowsException() {
        // Arrange
        UUID nonExistentProductId = UUID.randomUUID();
        when(storageService.getProductById(nonExistentProductId)).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchProductException exception = assertThrows(
                NoSuchProductException.class,
                () -> basketService.addProductToBasket(nonExistentProductId),
                "Добавив несуществующий продукт в корзину должно вызываться исключение"
        );

        assert(exception.getMessage().contains(nonExistentProductId.toString()));

        verify(storageService, times(1)).getProductById(nonExistentProductId);
    }

    @Test
    void addExistingProductToBasketCallsAddProduct() {
        // Arrange
        UUID existingProductId = UUID.randomUUID();
        when(storageService.getProductById(existingProductId)).thenReturn(Optional.of(mock(Product.class)));

        // Act
        basketService.addProductToBasket(existingProductId);

        // Assert
        verify(productBasket, times(1)).addProduct(existingProductId);
        verify(storageService, times(1)).getProductById(existingProductId);
    }

    @Test
    void getUserBasketReturnsEmptyBasketWhenProductBasketIsEmpty() {
        // Arrange
        when(productBasket.getProducts()).thenReturn(Collections.emptyMap());

        // Act
        var result = basketService.getUserBasket();

        // Assert
        assertTrue(result.getItems().isEmpty(), "Корзина пользователя должна быть пустой, если ProductBasket пуст");
        verify(productBasket, times(1)).getProducts();
    }

    @Test
    void getUserBasketReturnsCorrectBasketWhenProductBasketHasProducts() {
        // Arrange
        UUID productId = UUID.randomUUID();
        int quantity = 2;
        Product mockProduct = mock(Product.class);
        when(productBasket.getProducts()).thenReturn(Map.of(productId, quantity));
        when(storageService.getProductById(productId)).thenReturn(Optional.of(mockProduct));

        // Act
        var result = basketService.getUserBasket();

        // Assert
        assertFalse(result.getItems().isEmpty(), "Корзина пользователя не должна быть пустой, если ProductBasket содержит товары");
        assertEquals(1, result.getItems().size(), "Корзина должна содержать один элемент");
        assertEquals(quantity, result.getItems().get(0).getQuantity(), "Количество товара в корзине должно совпадать");
        verify(productBasket, times(1)).getProducts();
        verify(storageService, times(1)).getProductById(productId);
    }

}
