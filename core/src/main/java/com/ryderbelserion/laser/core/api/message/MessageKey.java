package com.ryderbelserion.laser.core.api.message;

import com.ryderbelserion.laser.core.api.message.context.MessageContext;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class MessageKey<C extends MessageContext> {

    public static final MessageKey<MessageContext> console_only = of("console.only", MessageContext.class);

    public static final MessageKey<MessageContext> player_only = of("player.only", MessageContext.class);

    private final Class<C> type;
    private final String key;

    public MessageKey(@NonNull final String key, @NonNull final Class<C> type) {
        this.type = type;
        this.key = key;
    }

    public @NonNull final Class<C> getType() {
        return this.type;
    }

    public @NonNull final String getKey() {
        return this.key;
    }

    public static <C extends MessageContext> @NotNull MessageKey<C> of(@NonNull final String key, @NonNull final Class<C> type) {
        return new MessageKey<>(key, type);
    }
}