package com.ryderbelserion.laser.core.objects.suggestions.interfaces;

import com.ryderbelserion.laser.core.objects.suggestions.SuggestionSource;
import java.util.List;

@FunctionalInterface
public interface SuggestionSupplier<CS> {

    List<?> get(SuggestionSource<CS> source);

}