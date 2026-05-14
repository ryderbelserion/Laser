package com.ryderbelserion.laser.core.meta.keys;

import org.jspecify.annotations.NonNull;

public class MetaKey<T> {

    public static final MetaKey<String> description = new MetaKey<>(String.class, "command.description");
    public static final MetaKey<String> literal = new MetaKey<>(String.class, "command.literal");
    public static final MetaKey<Class<?>> klass = new MetaKey<>(Class.class, "command.class");

    private final Class<? super T> type;
    private final String key;

    private MetaKey(
            @NonNull final Class<? super T> type,
            @NonNull final String key
    ) {
        this.type = type;
        this.key = key;
    }

    public @NonNull final Class<? super T> getType() {
        return this.type;
    }

    public @NonNull final String getKey() {
        return this.key;
    }

    public static <V> @NonNull MetaKey<V> of(@NonNull final Class<V> type, @NonNull final String key) {
        return new MetaKey<>(type, key);
    }
}