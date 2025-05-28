package com.mlh.mlh.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wiki_search")
public class WikiSearch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String searchTerm;

    @Column(nullable = false)
    private LocalDateTime searchTime;

    @OneToMany(mappedBy = "wikiSearch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WikiResult> results = new ArrayList<>();

    public WikiSearch() {}

    public WikiSearch(String searchTerm) {
        this.searchTerm = searchTerm;
        this.searchTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public LocalDateTime getSearchTime() {
        return searchTime;
    }

    public void setSearchTime(LocalDateTime searchTime) {
        this.searchTime = searchTime;
    }

    public List<WikiResult> getResults() {
        return results;
    }

    public void setResults(List<WikiResult> results) {
        this.results = results;
    }

    public void addResult(WikiResult result) {
        results.add(result);
        result.setWikiSearch(this);
    }
}