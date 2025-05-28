package com.mlh.mlh.model;

import jakarta.persistence.*;

@Entity
@Table(name = "wiki_result")
public class WikiResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String status;

    @Column(length = 1000)
    private String error;

    @Column(length = 1000)
    private String apiMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wiki_search_id", nullable = false)
    private WikiSearch wikiSearch;

    public WikiResult() {}

    public WikiResult(String title, String content, String status, String error, String apiMessage) {
        this.title = title;
        this.content = content;
        this.status = status;
        this.error = error;
        this.apiMessage = apiMessage;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getApiMessage() {
        return apiMessage;
    }

    public void setApiMessage(String apiMessage) {
        this.apiMessage = apiMessage;
    }

    public WikiSearch getWikiSearch() {
        return wikiSearch;
    }

    public void setWikiSearch(WikiSearch wikiSearch) {
        this.wikiSearch = wikiSearch;
    }
}