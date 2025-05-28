package com.mlh.mlh.service;

import com.mlh.mlh.model.WikiResult;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CacheService {

    private final Map<String, WikiResult> cache = new HashMap<>();

    public WikiResult getFromCache(String key) {
        return cache.get(key);
    }

    public void putInCache(String key, WikiResult value) {
        cache.put(key, value);
    }

    public void clearCache() {
        cache.clear();
    }
}