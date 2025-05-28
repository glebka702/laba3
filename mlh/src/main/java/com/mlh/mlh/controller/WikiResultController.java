package com.mlh.mlh.controller;

import com.mlh.mlh.model.WikiResult;
import com.mlh.mlh.repository.WikiResultRepository;
import com.mlh.mlh.service.WikiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class WikiResultController {

    @Autowired
    private WikiService wikiService;

    @Autowired
    private WikiResultRepository wikiResultRepository;

    @GetMapping("/search")
    public Map<String, Object> search(@RequestParam String term) {
        WikiResult wikiResult = wikiService.getContent(term);
        Map<String, Object> response = new HashMap<>();

        response.put("term", term);
        response.put("title", wikiResult.getTitle());
        response.put("content", wikiResult.getContent());
        response.put("status", wikiResult.getStatus());
        response.put("error", wikiResult.getError());
        response.put("apiMessage", wikiResult.getApiMessage());

        return response;
    }

    @GetMapping("/results")
    public List<WikiResult> getAllResults() {
        return wikiResultRepository.findAll();
    }

    @GetMapping("/results/{id}")
    public ResponseEntity<WikiResult> getResultById(@PathVariable Long id) {
        Optional<WikiResult> result = wikiResultRepository.findById(id);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/results")
    public WikiResult createResult(@RequestBody WikiResult wikiResult) {
        return wikiResultRepository.save(wikiResult);
    }

    @PutMapping("/results/{id}")
    public ResponseEntity<WikiResult> updateResult(@PathVariable Long id, @RequestBody WikiResult updatedResult) {
        Optional<WikiResult> result = wikiResultRepository.findById(id);
        if (result.isPresent()) {
            WikiResult existingResult = result.get();
            existingResult.setTitle(updatedResult.getTitle());
            existingResult.setContent(updatedResult.getContent());
            existingResult.setStatus(updatedResult.getStatus());
            existingResult.setError(updatedResult.getError());
            existingResult.setApiMessage(updatedResult.getApiMessage());
            return ResponseEntity.ok(wikiResultRepository.save(existingResult));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/results/{id}")
    public ResponseEntity<Void> deleteResult(@PathVariable Long id) {
        if (wikiResultRepository.existsById(id)) {
            wikiResultRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}