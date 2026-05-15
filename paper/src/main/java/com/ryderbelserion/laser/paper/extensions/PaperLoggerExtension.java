package com.ryderbelserion.laser.paper.extensions;

import com.ryderbelserion.laser.core.api.AbstractLogger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;

public class PaperLoggerExtension extends AbstractLogger {

    private final ComponentLogger logger;

    public PaperLoggerExtension(@NonNull final JavaPlugin plugin) {
        this.logger = plugin.getComponentLogger();
    }

    @Override
    public void log(@NonNull final Level level, @NonNull final String message, @NonNull final Exception exception, @NonNull final Object... args) {
        final Component component = asComponent(message, args);

        switch (level) {
            case INFO -> this.logger.info(component, exception);
            case WARNING -> this.logger.warn(component, exception);
            case ERROR -> this.logger.error(component, exception);
        }
    }

    @Override
    public void log(@NonNull final Level level, @NonNull final String message, @NonNull final Object... args) {
        final Component component = asComponent(message, args);

        switch (level) {
            case INFO -> this.logger.info(component);
            case WARNING -> this.logger.warn(component);
            case ERROR -> this.logger.error(component);
        }
    }
}