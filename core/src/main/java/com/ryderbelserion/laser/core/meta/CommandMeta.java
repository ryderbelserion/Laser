package com.ryderbelserion.laser.core.meta;

import com.ryderbelserion.laser.core.meta.keys.MetaKey;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public interface CommandMeta {
    
    <V> @NotNull Optional<V> get(@NonNull final MetaKey<V> metaKey);
    
    <V> @Nullable V getNullable(@NonNull final MetaKey<V> metaKey);
    
    @Contract("_, null -> null; _, !null -> !null")
    <V> V getOrDefault(@NonNull final MetaKey<V> metaKey, @Nullable final V def);
    
    <V> boolean isPresent(@NonNull final MetaKey<V> metaKey);

    final class Builder implements CommandMeta {
        
        private final Map<MetaKey<?>, Object> metaMap = new HashMap<>();

        @Contract("_, _ -> this")
        public <V> @NonNull Builder add(@NonNull final MetaKey<V> metaKey, @Nullable final V value) {
            this.metaMap.put(metaKey, value);
            
            return this;
        }

        @Contract("_, -> this")
        public <V> @NonNull Builder add(@NonNull final MetaKey<V> metaKey) {
            return add(metaKey, null);
        }

        @Override
        public @NonNull <V> Optional<V> get(@NonNull final MetaKey<V> metaKey) {
            return Optional.ofNullable(getNullable(metaKey));
        }

        @Override
        public <V> @Nullable V getNullable(@NonNull final MetaKey<V> metaKey) {
            return (V) this.metaMap.get(metaKey);
        }

        @Override
        public <V> @Nullable V getOrDefault(@NonNull final MetaKey<V> metaKey, @Nullable final V def) {
            return (V) this.metaMap.getOrDefault(metaKey, def);
        }

        @Override
        public <V> boolean isPresent(@NonNull final MetaKey<V> metaKey) {
            return this.metaMap.containsKey(metaKey);
        }
    }
}