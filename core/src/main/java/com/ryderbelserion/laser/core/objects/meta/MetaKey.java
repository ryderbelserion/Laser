package com.ryderbelserion.laser.core.objects.meta;

import com.ryderbelserion.laser.core.objects.meta.types.PermissionMeta;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class MetaKey<T> {

    public static final MetaKey<PermissionMeta> permission = MetaKey.of(PermissionMeta.class, "command.permision");
    public static final MetaKey<String> description = MetaKey.of(String.class, "command.description");
    public static final MetaKey<String> literal = MetaKey.of(String.class, "command.literal");
    public static final MetaKey<String[]> aliases = MetaKey.of(String[].class, "command.aliases");
    public static final MetaKey<Class<?>> klass = MetaKey.of(Class.class, "command.class");

    private final Class<? super T> type;
    private final String key;

    private MetaKey(
            final Class<? super T> type,
            final String key
    ) {
        this.type = type;
        this.key = key;
    }

    public Class<? super T> getType() {
        return this.type;
    }

    public String getKey() {
        return this.key;
    }

    public static <T> MetaKey<T> of(final Class<? super T> type, final String key) {
        return new MetaKey<>(type, key);
    }
}