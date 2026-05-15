package com.ryderbelserion.laser.core.api.interfaces;

import com.ryderbelserion.laser.core.api.message.MessageKey;
import com.ryderbelserion.laser.core.api.message.context.MessageContext;
import org.jspecify.annotations.NonNull;

public interface CommandResult {

    class Safe implements CommandResult {}

    record Error(@NonNull MessageKey<@NonNull MessageContext> message) implements CommandResult {

        @Override
        public @NonNull MessageKey<MessageContext> message() {
            return this.message;
        }
    }
}