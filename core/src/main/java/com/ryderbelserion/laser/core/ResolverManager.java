package com.ryderbelserion.laser.core;

import com.ryderbelserion.laser.core.objects.arguments.ArgumentResolver;
import com.ryderbelserion.laser.core.objects.suggestions.SuggestionKey;
import com.ryderbelserion.laser.core.objects.suggestions.SuggestionSource;
import com.ryderbelserion.laser.core.objects.suggestions.interfaces.SuggestionSupplier;
import org.jspecify.annotations.NullMarked;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@NullMarked
public final class ResolverManager<CS> {

    private final Map<SuggestionKey, SuggestionSupplier<CS>> suppliers = new ConcurrentHashMap<>();
    private final Map<Class<?>, ArgumentResolver<CS, ?>> resolvers = new ConcurrentHashMap<>();

    public void register(final SuggestionKey key, final SuggestionSupplier<CS> supplier) {
        this.suppliers.put(key, supplier);
    }

    public void register(final SuggestionKey key, final List<?> values) {
        this.suppliers.put(key, _ -> values);
    }

    public <T> void register(final ArgumentResolver<CS, T> resolver) {
        this.resolvers.put(resolver.type(), resolver);
    }

    public Optional<ArgumentResolver<CS, ?>> get(final Class<?> type) {
        return Optional.ofNullable(this.resolvers.get(type));
    }

    public void unregister(final SuggestionKey key) {
        this.suppliers.remove(key);
    }

    public boolean has(final SuggestionKey key) {
        return this.suppliers.containsKey(key);
    }

    public List<?> resolve(final SuggestionKey key, final SuggestionSource<CS> source) {
        final SuggestionSupplier<CS> supplier = this.suppliers.get(key);

        if (supplier == null) return List.of();

        try {
            final List<?> result = supplier.get(source);

            return result == null ? List.of() : result;
        } catch (final Exception exception) {
            return List.of();
        }
    }
}