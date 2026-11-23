package com.ryderbelserion.laser.core.objects.suggestions;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record SuggestionSource<CS> (CS sender, String index) {}