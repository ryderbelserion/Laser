package com.ryderbelserion.laser.core.objects.suggestions;

import org.jspecify.annotations.NullMarked;
import java.util.Locale;

@NullMarked
public record SuggestionKey(String name) {

    public static SuggestionKey of(final String name) {
        return new SuggestionKey(name.toLowerCase(Locale.ROOT));
    }
}