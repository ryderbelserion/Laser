package com.ryderbelserion.laser.core.objects;

import com.ryderbelserion.laser.core.api.AbstractCommand;
import com.ryderbelserion.laser.core.api.AbstractLogger;
import com.ryderbelserion.laser.core.api.extensions.SenderExtension;
import com.ryderbelserion.laser.core.objects.types.TreeCommandProcessor;
import net.kyori.adventure.audience.Audience;
import org.jspecify.annotations.NonNull;

public final class RootCommandProcessor<CS, S extends Audience> {

    private final TreeCommandProcessor<CS, S> processor; // process the /laser command.

    private final AbstractCommand<CS, S> command; // contains information like branch commands.
    private final Object instance; // invocation instance.

    public RootCommandProcessor(@NonNull final SenderExtension<CS, S> extension, @NonNull final AbstractLogger logger, @NonNull final AbstractCommand<CS, S> command) {
        this.processor = new TreeCommandProcessor<>(extension, command, logger).build();

        this.instance = command;
        this.command = command;
    }

    public @NonNull TreeCommandProcessor<CS, S> getTreeProcessor() {
        return this.processor;
    }

    public @NonNull AbstractCommand<CS, S> getCommand() {
        return this.command;
    }

    public @NonNull Object getInstance() {
        return this.instance;
    }
}