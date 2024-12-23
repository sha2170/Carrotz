package com.carrotzmarket.api.domain.searchHistory.repository;

import com.carrotzmarket.db.searchHistory.SearchHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistoryEntity, Long> {

    List<SearchHistoryEntity> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);
}
