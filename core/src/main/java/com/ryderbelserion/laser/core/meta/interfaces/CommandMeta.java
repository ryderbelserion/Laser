package com.ryderbelserion.laser.core.meta.interfaces;

import com.ryderbelserion.laser.core.meta.MetaKey;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public interface CommandMeta {
    
    <T> @NotNull Optional<T> get(@NonNull final MetaKey<T> metaKey);
    
    <T> @Nullable T getNullable(@NonNull final MetaKey<T> metaKey);
    
    @Contract("_, null -> null; _, !null -> !null")
    <T> T getOrDefault(@NonNull final MetaKey<T> metaKey, @Nullable final T def);
    
    <T> boolean isPresent(@NonNull final MetaKey<T> metaKey);

    final class Builder implements CommandMeta {
        
        private final Map<MetaKey<?>, Object> metaMap = new HashMap<>();

        @Contract("_, _ -> this")
        public <T> @NonNull Builder add(@NonNull final MetaKey<T> metaKey, @Nullable final T value) {
            this.metaMap.put(metaKey, value);
            
            return this;
        }

        @Contract("_, -> this")
        public <T> @NonNull Builder add(@NonNull final MetaKey<T> metaKey) {
            return add(metaKey, null);
        }

        @Override
        public @NonNull <T> Optional<T> get(@NonNull final MetaKey<T> metaKey) {
            return Optional.ofNullable(getNullable(metaKey));
        }

        @Override
        public <T> @Nullable T getNullable(@NonNull final MetaKey<T> metaKey) {
            return (T) this.metaMap.get(metaKey);
        }

        @Override
        public <T> @Nullable T getOrDefault(@NonNull final MetaKey<T> metaKey, @Nullable final T def) {
            return (T) this.metaMap.getOrDefault(metaKey, def);
        }

        @Override
        public <T> boolean isPresent(@NonNull final MetaKey<T> metaKey) {
            return this.metaMap.containsKey(metaKey);
        }
    }
}