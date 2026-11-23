package com.ryderbelserion.laser.core;

import com.ryderbelserion.laser.core.objects.arguments.ArgumentResolver;
import com.ryderbelserion.laser.core.objects.keys.LaserKey;
import com.ryderbelserion.laser.core.objects.requirements.RequirementResolver;
import com.ryderbelserion.laser.core.objects.suggestions.SuggestionSource;
import com.ryderbelserion.laser.core.objects.suggestions.interfaces.SuggestionSupplier;
import org.jspecify.annotations.NullMarked;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@NullMarked
public final class ContextRegistry<CS> {

    private final Map<LaserKey, RequirementResolver<CS>> requirements = new ConcurrentHashMap<>();
    private final Map<LaserKey, SuggestionSupplier<CS>> suppliers = new ConcurrentHashMap<>();
    private final Map<Class<?>, ArgumentResolver<CS, ?>> resolvers = new ConcurrentHashMap<>();

    public void register(final LaserKey key, final RequirementResolver<CS> supplier) {
        this.requirements.put(key, supplier);
    }

    public Optional<RequirementResolver<CS>> get(final LaserKey key) {
        return Optional.ofNullable(this.requirements.get(key));
    }

    public void register(final LaserKey key, final SuggestionSupplier<CS> supplier) {
        this.suppliers.put(key, supplier);
    }

    public void register(final LaserKey key, final List<?> values) {
        this.suppliers.put(key, _ -> values);
    }

    public <T> void register(final ArgumentResolver<CS, T> resolver) {
        this.resolvers.put(resolver.type(), resolver);
    }

    public Optional<ArgumentResolver<CS, ?>> get(final Class<?> type) {
        return Optional.ofNullable(this.resolvers.get(type));
    }

    public void unregister(final LaserKey key) {
        this.suppliers.remove(key);
    }

    public boolean has(final LaserKey key) {
        return this.suppliers.containsKey(key);
    }

    public List<?> resolve(final LaserKey key, final SuggestionSource<CS> source) {
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