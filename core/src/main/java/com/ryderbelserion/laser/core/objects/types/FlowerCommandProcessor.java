package com.ryderbelserion.laser.core.objects.types;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.laser.core.CommandRegistry;
import com.ryderbelserion.laser.core.ContextRegistry;
import com.ryderbelserion.laser.core.api.annotations.other.Permission;
import com.ryderbelserion.laser.core.api.extensions.SenderExtension;
import com.ryderbelserion.laser.core.objects.meta.MetaKey;
import com.ryderbelserion.laser.core.objects.meta.interfaces.CommandMeta;
import com.ryderbelserion.laser.core.objects.meta.types.PermissionMeta;
import com.ryderbelserion.laser.core.objects.meta.processors.ArgumentProcessor;
import net.kyori.adventure.audience.Audience;
import com.ryderbelserion.laser.core.api.AbstractLogger;
import org.jspecify.annotations.NonNull;
import java.lang.reflect.Method;
import java.util.Optional;

public class FlowerCommandProcessor<CS, S extends Audience> extends ArgumentProcessor<CS, S> {

    public FlowerCommandProcessor(
            @NonNull final CommandRegistry<CS, S> commandRegistry,
            @NonNull final ContextRegistry<CS> contextRegistry,
            @NonNull final SenderExtension<CS, S> extension,
            @NonNull final Object instance,
            @NonNull final AbstractLogger logger,
            @NonNull final Method method
    ) {
        super(commandRegistry, contextRegistry, extension, instance, method, logger, new CommandMeta.@NonNull Builder());

        Optional.ofNullable(method.getAnnotation(Permission.class)).ifPresent(permission -> this.builder.add(MetaKey.permission, new PermissionMeta<>(extension, permission).init()));
    }

    @Override
    public void build(@NonNull final LiteralArgumentBuilder<CS> root) {
        this.builder.get(MetaKey.permission).ifPresent(permission -> root.requires(context -> {
            return permission.isPermitted(context);
        }));

        root.executes(this::execute);
    }

    @Override
    public @NonNull final CommandMeta meta() {
        return this.builder;
    }
}