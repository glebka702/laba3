package com.mlh.mlh.controller;

import com.mlh.mlh.model.WikiSearch;
import com.mlh.mlh.repository.WikiSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/searches")
public class WikiSearchController {

    @Autowired
    private WikiSearchRepository wikiSearchRepository;

    @GetMapping("/by-term")
    public ResponseEntity<List<WikiSearch>> getSearchesByTerm(@RequestParam String term) {
        List<WikiSearch> searches = wikiSearchRepository.findBySearchTermContaining(term);
        if (searches.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(searches);
    }

    @GetMapping
    public List<WikiSearch> getAllSearches() {
        return wikiSearchRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WikiSearch> getSearchById(@PathVariable Long id) {
        Optional<WikiSearch> search = wikiSearchRepository.findById(id);
        return search.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public WikiSearch createSearch(@RequestBody WikiSearch wikiSearch) {
        return wikiSearchRepository.save(wikiSearch);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WikiSearch> updateSearch(@PathVariable Long id, @RequestBody WikiSearch updatedSearch) {
        Optional<WikiSearch> search = wikiSearchRepository.findById(id);
        if (search.isPresent()) {
            WikiSearch existingSearch = search.get();
            existingSearch.setSearchTerm(updatedSearch.getSearchTerm());
            return ResponseEntity.ok(wikiSearchRepository.save(existingSearch));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSearch(@PathVariable Long id) {
        if (wikiSearchRepository.existsById(id)) {
            wikiSearchRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}