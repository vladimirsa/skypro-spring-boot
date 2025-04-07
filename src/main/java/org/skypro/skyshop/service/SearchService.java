package org.skypro.skyshop.service;

import org.skypro.skyshop.model.search.SearchResult;
import org.skypro.skyshop.model.search.Searchable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class SearchService {
    private final StorageService storageService;

    public SearchService(StorageService storageService) {
        this.storageService = storageService;
    }

    public Collection<SearchResult> search(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            return Collections.emptyList();
        }
        return storageService.getAllSearchables().stream()
                .filter(searchable -> searchable.getSearchTerm().toLowerCase().contains(searchText.toLowerCase()))
                .map(SearchResult::fromSearchable)
                .collect(Collectors.toList());
    }

}
