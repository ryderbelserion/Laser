package com.ryderbelserion.laser.core.objects.types;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.laser.core.api.annotations.other.Permission;
import com.ryderbelserion.laser.core.api.extensions.SenderExtension;
import com.ryderbelserion.laser.core.meta.MetaKey;
import com.ryderbelserion.laser.core.meta.interfaces.CommandMeta;
import com.ryderbelserion.laser.core.meta.types.PermissionMeta;
import com.ryderbelserion.laser.core.meta.processors.ArgumentProcessor;
import net.kyori.adventure.audience.Audience;
import com.ryderbelserion.laser.core.api.AbstractLogger;
import org.jspecify.annotations.NonNull;
import java.lang.reflect.Method;
import java.util.Optional;

public class FlowerCommandProcessor<CS, S extends Audience> extends ArgumentProcessor<CS, S> {

    private final LiteralArgumentBuilder<CS> literal;

    public FlowerCommandProcessor(
            @NonNull final SenderExtension<CS, S> extension,
            @NonNull final Object instance,
            @NonNull final LiteralArgumentBuilder<CS> literal,
            @NonNull final AbstractLogger logger,
            @NonNull final Method method
    ) {
        super(extension, instance, method, logger, new CommandMeta.@NonNull Builder());

        this.literal = literal;

        Optional.ofNullable(method.getAnnotation(Permission.class)).ifPresent(permission -> this.builder.add(MetaKey.permission, new PermissionMeta<>(extension, permission).init()));
    }

    @Override
    public void build() {
        this.builder.get(MetaKey.permission).ifPresent(permission -> this.literal.requires(context -> {
            return permission.isPermitted(context);
        }));

        this.literal.executes(this::execute);
    }

    @Override
    public @NonNull final CommandMeta meta() {
        return this.builder;
    }
}