package com.carrotzmarket.api.domain.searchHistory.controller;

import com.carrotzmarket.api.domain.searchHistory.service.SearchHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search-history")
@RequiredArgsConstructor
public class SearchHistoryController {

    private final SearchHistoryService searchHistoryService;

    @PostMapping("/{userId}")
    public ResponseEntity<String> saveSearchHistory(@PathVariable Long userId, @RequestParam String keyword) {
        searchHistoryService.saveSearchHistory(userId, keyword);
        return ResponseEntity.ok("Search history saved successfully.");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<String>> getRecentSearches(@PathVariable Long userId) {
        List<String> recentSearches = searchHistoryService.getRecentSearches(userId);
        return ResponseEntity.ok(recentSearches);
    }
}
