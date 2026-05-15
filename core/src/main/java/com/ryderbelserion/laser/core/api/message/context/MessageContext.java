package com.ryderbelserion.laser.core.api.message.context;

import com.ryderbelserion.laser.core.meta.interfaces.CommandMeta;
import org.jspecify.annotations.NonNull;

public class MessageContext {

    private final CommandMeta meta;

    public MessageContext(@NonNull final CommandMeta meta) {
        this.meta = meta;
    }

    public @NonNull final CommandMeta getMeta() {
        return this.meta;
    }
}