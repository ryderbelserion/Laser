package com.ryderbelserion.laser.core.api;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public abstract class AbstractLogger {

    public void safe(@NonNull final String message, @NonNull final Exception exception, @NonNull final Object... args) {
        log(Level.INFO, message, exception, args);
    }

    public void warn(@NonNull final String message, @NonNull final Exception exception, @NonNull final Object... args) {
        log(Level.WARNING, message, exception, args);
    }

    public void error(@NonNull final String message, @NonNull final Exception exception, @NonNull final Object... args) {
        log(Level.ERROR, message, exception, args);
    }

    public void safe(@NonNull final String message, @NonNull final Object... args) {
        log(Level.INFO, message, args);
    }

    public void warn(@NonNull final String message, @NonNull final Object... args) {
        log(Level.WARNING, message, args);
    }

    public void error(@NonNull final String message, @NonNull final Object... args) {
        log(Level.ERROR, message, args);
    }

    public abstract void log(
            @NotNull final Level level,
            @NotNull final String message,
            @NotNull final Exception exception,
            @NotNull final Object... args
    );

    public abstract void log(
            @NotNull final Level level,
            @NotNull final String message,
            @NotNull final Object... args
    );

    public @NonNull final Component asComponent(@NonNull final String message, @NonNull final Object... args) {
        return MiniMessage.miniMessage().deserialize(String.format(message, args)).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    public enum Level {

        INFO,
        WARNING,
        ERROR

    }
}