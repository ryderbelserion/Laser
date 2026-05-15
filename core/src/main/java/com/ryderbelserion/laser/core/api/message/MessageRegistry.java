package com.ryderbelserion.laser.core.api.message;

import com.ryderbelserion.laser.core.api.message.context.MessageContext;
import com.ryderbelserion.laser.core.api.message.context.MessageResolver;
import org.jspecify.annotations.NonNull;
import java.util.HashMap;
import java.util.Map;

public class MessageRegistry<S> {

    private final Map<MessageKey<?>, MessageResolver<S, ? extends MessageContext>> messages = new HashMap<>();

    public final <C extends MessageContext> void register(@NonNull final MessageKey<C> key, @NonNull final MessageResolver<S, C> resolver) {
        this.messages.put(key, resolver);
    }

    public final <C extends MessageContext> void sendMessage(@NonNull final S sender, @NonNull final MessageKey<C> key, @NonNull final C context) {
        final MessageResolver<S, C> resolver = (MessageResolver<S, C>) this.messages.get(key);

        if (resolver == null) return;

        resolver.resolve(sender, context);
    }
}