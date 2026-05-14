package com.ryderbelserion.laser.core.api;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.laser.core.api.annotations.Flower;
import com.ryderbelserion.laser.core.api.annotations.types.Leaf;
import com.ryderbelserion.laser.core.api.extensions.SenderExtension;
import com.ryderbelserion.laser.core.meta.interfaces.CommandMeta;
import com.ryderbelserion.laser.core.objects.types.FlowerCommandProcessor;
import com.ryderbelserion.laser.core.objects.types.LeafCommandProcessor;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class AbstractProcessor<CS, S extends Audience> {

    protected LiteralArgumentBuilder<CS> literal;

    protected final CommandMeta.@NonNull Builder builder;
    protected final SenderExtension<CS, S> extension;
    protected final Object object;

    public AbstractProcessor(@NonNull final SenderExtension<CS, S> extension, @NonNull final Object object, final CommandMeta.@NonNull Builder builder) {
        this.extension = extension;
        this.builder = builder;
        this.object = object;
    }

    protected @NotNull final Optional<FlowerCommandProcessor<CS, S>> flower(@NotNull final Method[] methods) {
        return Optional.ofNullable(Arrays.stream(methods)
                .filter(insect -> insect.isAnnotationPresent(Flower.class))
                .filter(insect -> {
                    final int modifiers = insect.getModifiers();

                    return !Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers);
                })
                .map(method -> new FlowerCommandProcessor<>(this.extension, this.object, this.literal, method))
                .toList().getFirst());
    }

    protected @NotNull final List<LeafCommandProcessor<CS,S>> leaf(@NotNull final Method[] methods) {
        return Arrays.stream(methods)
                .filter(insect -> insect.isAnnotationPresent(Leaf.class))
                .filter(insect -> {
                    final int modifiers = insect.getModifiers();

                    return !Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers);
                })
                .map(method -> new LeafCommandProcessor<>(this.extension, this.literal, this.object, method))
                .toList();
    }

    public abstract @NonNull LiteralArgumentBuilder<CS> literal();

    public abstract @NonNull AbstractProcessor<CS, S> build();

    public abstract @NonNull CommandMeta meta();
}