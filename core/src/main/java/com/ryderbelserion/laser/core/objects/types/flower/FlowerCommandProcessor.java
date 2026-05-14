package com.ryderbelserion.laser.core.objects.types.flower;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.laser.core.api.extensions.SenderExtension;
import com.ryderbelserion.laser.core.objects.types.ArgumentProcessor;
import net.kyori.adventure.audience.Audience;
import org.jspecify.annotations.NonNull;
import java.lang.reflect.Method;

public class FlowerCommandProcessor<CS, S extends Audience> extends ArgumentProcessor<CS, S> {

    private final LiteralArgumentBuilder<CS> literal;

    public FlowerCommandProcessor(@NonNull final SenderExtension<CS, S> extension, @NonNull final Object instance, @NonNull final LiteralArgumentBuilder<CS> literal, @NonNull final Method method) {
        super(extension, instance, method);

        this.literal = literal;
    }

    @Override
    public void build() {
        this.literal.executes(this::execute);
    }
}