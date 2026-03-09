package org.skypro.skyshop.model.service;

import org.skypro.skyshop.model.search.SearchResult;
import org.skypro.skyshop.model.search.Searchable;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.stream.Collectors;


@Service
public class SearchService {
    private final StorageService storageService;

    public SearchService(StorageService storageService) {
        this.storageService = storageService;
    }

    public Collection<SearchResult> search(String pattern) {
        Collection<Searchable> searchables = storageService.getAllSearchable();

        if (pattern == null || pattern.trim().isEmpty()) {
            return searchables.stream()
                    .map(SearchResult::fromSearchable)
                    .collect(Collectors.toList());
        }

        String searchPattern = pattern.toLowerCase().trim();
        return searchables.stream()
                .filter(item -> item.getSearchTerm().toLowerCase().contains(searchPattern))
                .map(SearchResult::fromSearchable)
                .collect(Collectors.toList());
    }
}