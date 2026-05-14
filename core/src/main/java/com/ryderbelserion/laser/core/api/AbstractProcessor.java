package com.ryderbelserion.laser.core.api;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.laser.core.api.annotations.Flower;
import com.ryderbelserion.laser.core.api.extensions.SenderExtension;
import com.ryderbelserion.laser.core.objects.types.flower.FlowerCommandProcessor;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Optional;

public abstract class AbstractProcessor<CS, S extends Audience> {

    protected LiteralArgumentBuilder<CS> literal;

    protected final SenderExtension<CS, S> extension;
    protected final String description;
    protected final Object object;

    public AbstractProcessor(@NonNull final SenderExtension<CS, S> extension, @NonNull final Object object, @NonNull final String description) {
        this.description = description;
        this.extension = extension;
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

    public abstract @NonNull LiteralArgumentBuilder<CS> literal();

    public abstract @NonNull AbstractProcessor<CS, S> build();

    public @NonNull String getDescription() {
        return this.description;
    }
}