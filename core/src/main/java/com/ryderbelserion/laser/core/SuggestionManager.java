package com.ryderbelserion.laser.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuggestionManager {

    private final Map<String, List<?>> suggestions = new HashMap<>();

    public void registerSuggestion(final String name, final List<?> values) {
        this.suggestions.put(name, values);
    }

    public void unregisterSuggestion(final String name) {
        this.suggestions.remove(name);
    }

    public <T> List<T> getSuggestions(final String name) {
        return (List<T>) this.suggestions.getOrDefault(name, List.of());
    }

    public boolean hasSuggestion(final String name) {
        return this.suggestions.containsKey(name);
    }
}