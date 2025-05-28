package com.mlh.mlh.repository;

import com.mlh.mlh.model.WikiSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WikiSearchRepository extends JpaRepository<WikiSearch, Long> {

    @Query("SELECT ws FROM WikiSearch ws JOIN ws.results wr WHERE ws.searchTerm LIKE %:term%")
    List<WikiSearch> findBySearchTermContaining(@Param("term") String term);
}