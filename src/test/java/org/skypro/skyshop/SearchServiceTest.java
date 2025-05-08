package org.skypro.skyshop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.search.SearchResult;
import org.skypro.skyshop.model.search.Searchable;
import org.skypro.skyshop.service.SearchService;
import org.skypro.skyshop.service.StorageService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchServiceTest {

    private StorageService storageService;
    private SearchService searchService;

    @BeforeEach
    void setUp() {
        storageService = mock(StorageService.class);
        searchService = new SearchService(storageService);
    }

    @Test
    void searchWhenNoObjectsInStorageService() {
        // Arrange
        when(storageService.getAllSearchables()).thenReturn(Collections.emptyList());

        // Act
        Collection<SearchResult> result = searchService.search("Тест");

        // Assert
        assertTrue(result.isEmpty(), "Результат поиска не должен содержать элементов");
        verify(storageService, times(1)).getAllSearchables();
    }

    @Test
    void searchWhenNoMatchingObjectsInStorageService() {
        // Arrange
        Searchable nonMatchingObject = mock(Searchable.class);
        when(nonMatchingObject.getSearchTerm()).thenReturn("Нет совпадений");
        List<Searchable> searchables = new ArrayList<>();
        searchables.add(nonMatchingObject);
        when(storageService.getAllSearchables()).thenReturn(searchables);

        // Act
        Collection<SearchResult> result = searchService.search("Тест");

        // Assert
        assertTrue(result.isEmpty(), "Результат поиска не должен содержать элементов");
        verify(storageService, times(1)).getAllSearchables();
    }

    @Test
    void searchWhenMatchingObjectsExistInStorageService() {
        // Arrange
        UUID mockId = UUID.randomUUID();
        Searchable matchingObject = mock(Searchable.class);
        when(matchingObject.getSearchTerm()).thenReturn("Тестовый продукт");
        when(matchingObject.getId()).thenReturn(mockId);
        when(matchingObject.getName()).thenReturn("Тест");
        when(matchingObject.getContentType()).thenReturn("product");

        List<Searchable> searchables = new ArrayList<>();
        searchables.add(matchingObject);
        when(storageService.getAllSearchables()).thenReturn(searchables);

        // Act
        Collection<SearchResult> result = searchService.search("тест");

        // Assert
        assertFalse(result.isEmpty(), "Результат поиска должен содержать элементы");
        assertEquals(1, result.size(), "Результат поиска должен содержать один элемент");

        SearchResult searchResult = result.iterator().next();
        assertEquals(mockId.toString(), searchResult.getId(), "ID должен совпадать");
        assertEquals("Тест", searchResult.getName(), "Название должно совпадать");
        assertEquals("product", searchResult.getContentType(), "Тип должен совпадать");
        verify(storageService, times(1)).getAllSearchables();
    }
}
