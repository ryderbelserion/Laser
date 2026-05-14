package com.ryderbelserion.laser.core.meta;

import com.ryderbelserion.laser.core.meta.types.PermissionMeta;
import org.jspecify.annotations.NonNull;

public class MetaKey<T> {

    public static final MetaKey<PermissionMeta> permission = MetaKey.of(PermissionMeta.class, "command.permision");
    public static final MetaKey<String> description = MetaKey.of(String.class, "command.description");
    public static final MetaKey<String> literal = MetaKey.of(String.class, "command.literal");
    public static final MetaKey<Class<?>> klass = MetaKey.of(Class.class, "command.class");

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

    public static <T> @NonNull MetaKey<T> of(@NonNull final Class<? super T> type, @NonNull final String key) {
        return new MetaKey<>(type, key);
    }
}