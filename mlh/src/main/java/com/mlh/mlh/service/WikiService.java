package com.mlh.mlh.service;

import com.mlh.mlh.model.WikiSearch;
import com.mlh.mlh.model.WikiResult;
import com.mlh.mlh.repository.WikiSearchRepository;
import com.mlh.mlh.repository.WikiResultRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WikiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final WikiSearchRepository wikiSearchRepository;
    private final WikiResultRepository wikiResultRepository;
    private final CacheService cacheService;
    private static final String WIKI_API_URL = "https://en.wikipedia.org/w/api.php";

    @Autowired
    public WikiService(RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper,
                       WikiSearchRepository wikiSearchRepository, WikiResultRepository wikiResultRepository,
                       CacheService cacheService) {
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
        this.wikiSearchRepository = wikiSearchRepository;
        this.wikiResultRepository = wikiResultRepository;
        this.cacheService = cacheService;
    }

    public WikiResult getContent(String term) {
        // Проверяем, есть ли результат в кэше
        WikiResult cachedResult = cacheService.getFromCache(term);
        if (cachedResult != null) {
            return cachedResult;
        }

        // Если в кэше нет, выполняем запрос
        WikiSearch wikiSearch = new WikiSearch(term);
        WikiResult wikiResult = new WikiResult();
        wikiResult.setStatus("processing");

        try {
            String url = buildUrl(term);
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                processResponse(wikiResult, response.getBody());
            } else {
                handleError(wikiResult, "HTTP Error: " + response.getStatusCodeValue());
            }
        } catch (HttpClientErrorException e) {
            handleError(wikiResult, "API Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (Exception e) {
            handleError(wikiResult, "General Error: " + e.getMessage());
        }

        // Сохраняем в базу данных
        wikiSearch.addResult(wikiResult);
        wikiSearchRepository.save(wikiSearch);

        // Сохраняем в кэш
        cacheService.putInCache(term, wikiResult);

        return wikiResult;
    }

    private String buildUrl(String term) {
        return UriComponentsBuilder.fromHttpUrl(WIKI_API_URL)
                .queryParam("action", "query")
                .queryParam("format", "json")
                .queryParam("prop", "extracts")
                .queryParam("exintro", "")
                .queryParam("explaintext", "")
                .queryParam("titles", term)
                .encode()
                .toUriString();
    }

    private void processResponse(WikiResult wikiResult, String jsonResponse) throws Exception {
        JsonNode root = objectMapper.readTree(jsonResponse);
        JsonNode pages = root.path("query").path("pages");
        JsonNode page = pages.elements().hasNext() ? pages.elements().next() : null;

        if (page == null || page.has("missing")) {
            wikiResult.setStatus("error");
            wikiResult.setApiMessage("Page does not exist");
        } else {
            wikiResult.setTitle(page.path("title").asText());
            wikiResult.setContent(page.path("extract").asText());
            wikiResult.setStatus("found");
        }
    }

    private void handleError(WikiResult wikiResult, String errorMessage) {
        wikiResult.setStatus("error");
        wikiResult.setError(errorMessage);
        wikiResult.setApiMessage("Failed to fetch data from Wikipedia API");
    }
}