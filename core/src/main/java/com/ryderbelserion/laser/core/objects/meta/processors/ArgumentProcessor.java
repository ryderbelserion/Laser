package com.ryderbelserion.laser.core.objects.meta.processors;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ryderbelserion.laser.core.CommandRegistry;
import com.ryderbelserion.laser.core.ContextRegistry;
import com.ryderbelserion.laser.core.api.annotations.other.Suggestion;
import com.ryderbelserion.laser.core.api.extensions.SenderExtension;
import com.ryderbelserion.laser.core.api.interfaces.CommandResult;
import com.ryderbelserion.laser.core.api.message.MessageKey;
import com.ryderbelserion.laser.core.api.message.context.MessageContext;
import com.ryderbelserion.laser.core.objects.meta.interfaces.CommandMeta;
import com.ryderbelserion.laser.core.objects.meta.types.ArgumentMeta;
import net.kyori.adventure.audience.Audience;
import com.ryderbelserion.laser.core.api.AbstractLogger;
import org.jspecify.annotations.NonNull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ArgumentProcessor<CS, S extends Audience> {

    protected final List<ArgumentMeta<CS, S>> arguments = new ArrayList<>();
    protected final CommandMeta.@NonNull Builder builder;
    protected final CommandRegistry<CS, S> commandRegistry;
    protected final ContextRegistry<CS> contextRegistry;
    protected final Parameter[] parameters;
    protected final AbstractLogger logger;

    private final SenderExtension<CS, S> extension;
    private final Object object;
    private final Method method;

    public ArgumentProcessor(
            @NonNull final CommandRegistry<CS, S> commandRegistry,
            @NonNull final ContextRegistry<CS> contextRegistry,
            @NonNull final SenderExtension<CS, S> extension,
            @NonNull final Object object,
            @NonNull final Method method,
            @NonNull final AbstractLogger logger,

            final CommandMeta.@NonNull Builder builder
    ) {
        this.contextRegistry = contextRegistry;
        this.commandRegistry = commandRegistry;
        this.parameters = method.getParameters();
        this.extension = extension;
        this.builder = builder;
        this.logger = logger;
        this.object = object;
        this.method = method;

        init();
    }

    public void init() {
        for (final Parameter parameter : process(this.parameters)) {
            this.arguments.add(new ArgumentMeta<>(this.commandRegistry, this.contextRegistry, parameter));
        }
    }

    public abstract @NonNull CommandMeta meta();

    public abstract void build(@NonNull final LiteralArgumentBuilder<CS> root);

    protected @NonNull final List<Parameter> process(final Parameter[] parameters) {
        return Arrays.stream(parameters)
                .filter(insect -> insect.isAnnotationPresent(Suggestion.class))
                .toList();
    }

    protected int execute(@NonNull final CommandContext<CS> context) {
        final Class<? extends CS> sender = getSenderType();
        final CS source = context.getSource();

        final CommandResult result = this.extension.validateSender(context.getSource(), sender);
        final S mappedSender = this.extension.mapSender(source, sender);

        if (result instanceof CommandResult.Error(MessageKey<MessageContext> message)) {
            this.extension.getRegistry().sendMessage(mappedSender, message, new MessageContext(this.meta()));

            return Command.SINGLE_SUCCESS;
        }

        final List<Object> arguments = new ArrayList<>();

        arguments.add(mappedSender);

        this.arguments.forEach(argument -> {
            try {
                arguments.add(argument.extract(context));
            } catch (final CommandSyntaxException exception) {
                throw new RuntimeException(exception);
            }
        });

        return invoke(arguments);
    }

    protected int invoke(@NonNull final List<Object> arguments) {
        if (!this.method.trySetAccessible()) return Command.SINGLE_SUCCESS;

        try {
            this.method.invoke(this.object, arguments.toArray());
        } catch (IllegalAccessException | InvocationTargetException exception) {
            exception.printStackTrace();
        }

        return Command.SINGLE_SUCCESS;
    }

    public @NonNull Class<? extends CS> getSenderType() {
        if (this.parameters == null || parameters.length == 0) {
            throw new IllegalStateException("No sender parameter has been found.");
        }

        return this.extension.getSenderType(this.parameters[0].getType());
    }
}