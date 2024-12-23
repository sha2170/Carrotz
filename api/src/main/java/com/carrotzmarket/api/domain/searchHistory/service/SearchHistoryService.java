package com.carrotzmarket.api.domain.searchHistory.service;

import com.carrotzmarket.api.domain.searchHistory.repository.SearchHistoryRepository;
import com.carrotzmarket.db.searchHistory.SearchHistoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;

    public void saveSearchHistory(Long userId, String keyword) {
        SearchHistoryEntity searchHistory = SearchHistoryEntity.builder()
                .userId(userId)
                .keyword(keyword)
                .createdAt(LocalDateTime.now())
                .build();
        searchHistoryRepository.save(searchHistory);
    }

    public List<String> getRecentSearches(Long userId) {
        return searchHistoryRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(SearchHistoryEntity::getKeyword)
                .collect(Collectors.toList());
    }
}
