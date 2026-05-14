package com.ryderbelserion.laser.core.api.interfaces;

import org.jspecify.annotations.NonNull;

public interface CommandResult {

    class Safe implements CommandResult {}

    record Error(@NonNull String message) implements CommandResult {

        @Override
        public @NonNull String message() {
            return this.message;
        }
    }
}