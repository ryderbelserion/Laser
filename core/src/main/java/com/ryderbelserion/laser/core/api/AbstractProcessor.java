package com.ryderbelserion.laser.core.api;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.laser.core.CommandRegistry;
import com.ryderbelserion.laser.core.ContextRegistry;
import com.ryderbelserion.laser.core.api.annotations.Flower;
import com.ryderbelserion.laser.core.api.annotations.types.Leaf;
import com.ryderbelserion.laser.core.api.extensions.SenderExtension;
import com.ryderbelserion.laser.core.objects.meta.interfaces.CommandMeta;
import com.ryderbelserion.laser.core.objects.types.FlowerCommandProcessor;
import com.ryderbelserion.laser.core.objects.types.LeafCommandProcessor;
import net.kyori.adventure.audience.Audience;
import org.jspecify.annotations.NonNull;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class AbstractProcessor<CS, S extends Audience> {

    protected LiteralArgumentBuilder<CS> literal;

    protected final CommandMeta.@NonNull Builder builder;
    protected final CommandRegistry<CS, S> commandRegistry;
    protected final ContextRegistry<CS> contextRegistry;
    protected final SenderExtension<CS, S> extension;
    protected final AbstractLogger logger;
    protected final Object object;

    public AbstractProcessor(
            @NonNull final CommandRegistry<CS, S> commandRegistry,
            @NonNull final ContextRegistry<CS> contextRegistry,
            @NonNull final SenderExtension<CS, S> extension,
            @NonNull final Object object,
            @NonNull final AbstractLogger logger,
            final CommandMeta.@NonNull Builder builder
    ) {
        this.contextRegistry = contextRegistry;
        this.commandRegistry = commandRegistry;
        this.extension = extension;
        this.builder = builder;
        this.logger = logger;
        this.object = object;
    }

    protected @NonNull final Optional<FlowerCommandProcessor<CS, S>> flower(@NonNull final Method[] methods) {
        return Optional.ofNullable(Arrays.stream(methods)
                .filter(insect -> insect.isAnnotationPresent(Flower.class))
                .filter(insect -> {
                    final int modifiers = insect.getModifiers();

                    return !Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers);
                })
                .map(method -> new FlowerCommandProcessor<>(this.commandRegistry, this.contextRegistry, this.extension, this.object, this.logger, method))
                .toList().getFirst());
    }

    protected @NonNull final List<LeafCommandProcessor<CS,S>> leaf(@NonNull final Method[] methods) {
        return Arrays.stream(methods)
                .filter(insect -> insect.isAnnotationPresent(Leaf.class))
                .filter(insect -> {
                    final int modifiers = insect.getModifiers();

                    return !Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers);
                })
                .map(method -> new LeafCommandProcessor<>(this.commandRegistry, this.contextRegistry, this.extension, this.object, this.logger, method))
                .toList();
    }

    public abstract @NonNull LiteralArgumentBuilder<CS> literal();

    public abstract @NonNull AbstractProcessor<CS, S> build();

    public abstract @NonNull CommandMeta meta();
}