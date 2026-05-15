package com.ryderbelserion.laser.core.meta.processors;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.ryderbelserion.laser.core.api.annotations.other.Suggestion;
import com.ryderbelserion.laser.core.api.extensions.SenderExtension;
import com.ryderbelserion.laser.core.api.interfaces.CommandResult;
import com.ryderbelserion.laser.core.meta.interfaces.CommandMeta;
import com.ryderbelserion.laser.core.meta.types.ArgumentMeta;
import net.kyori.adventure.audience.Audience;
import com.ryderbelserion.laser.core.api.AbstractLogger;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ArgumentProcessor<CS, S extends Audience> {

    protected final CommandMeta.@NonNull Builder builder;
    protected final ArgumentMeta<CS> argumentMeta;
    protected final Parameter[] parameters;

    private final SenderExtension<CS, S> extension;
    protected final AbstractLogger logger;
    private final Object object;
    private final Method method;

    public ArgumentProcessor(
            @NonNull final SenderExtension<CS, S> extension,
            @NonNull final Object object,
            @NonNull final Method method,
            @NonNull final AbstractLogger logger,

            final CommandMeta.@NonNull Builder builder
    ) {
        this.parameters = method.getParameters();
        this.argumentMeta = new ArgumentMeta<>();
        this.extension = extension;
        this.builder = builder;
        this.logger = logger;
        this.object = object;
        this.method = method;
    }

    public abstract @NonNull CommandMeta meta();

    public abstract void build(@NonNull final LiteralArgumentBuilder<CS> root);

    protected @NotNull final List<Parameter> process() {
        return Arrays.stream(this.parameters)
                .filter(insect -> insect.isAnnotationPresent(Suggestion.class))
                .toList();
    }

    protected int execute(@NonNull final CommandContext<CS> context) {
        final Class<? extends CS> sender = getSenderType();
        final CS source = context.getSource();

        final CommandResult result = this.extension.validateSender(context.getSource(), sender);

        if (result instanceof CommandResult.Error(String message)) {
            this.extension.sendMessage(this.extension.mapAudience(source), message);

            return Command.SINGLE_SUCCESS;
        }

        final List<Object> arguments = new ArrayList<>();

        arguments.add(this.extension.mapSender(source, sender));

        return invoke(arguments);
    }

    protected int invoke(@NotNull final List<Object> arguments) {
        if (!this.method.trySetAccessible()) return Command.SINGLE_SUCCESS;

        try {
            this.method.invoke(this.object, arguments.toArray());
        } catch (IllegalAccessException | InvocationTargetException exception) {
            exception.printStackTrace();
        }

        return Command.SINGLE_SUCCESS;
    }

    public @NotNull Class<? extends CS> getSenderType() {
        if (this.parameters == null || parameters.length == 0) {
            throw new IllegalStateException("No sender parameter has been found.");
        }

        return this.extension.getSenderType(this.parameters[0].getType());
    }
}