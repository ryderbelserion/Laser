package com.ryderbelserion.laser.core.objects.types;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.ryderbelserion.laser.core.api.annotations.other.Permission;
import com.ryderbelserion.laser.core.api.annotations.types.Leaf;
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

public class LeafCommandProcessor<CS, S extends Audience> extends ArgumentProcessor<CS, S> {

    private final LiteralArgumentBuilder<CS> literal;

    public LeafCommandProcessor(
            @NonNull final SenderExtension<CS, S> extension,
            @NonNull final Object instance,
            @NonNull final AbstractLogger logger,
            @NonNull final Method method
    ) {
        super(extension, instance, method, logger, new CommandMeta.@NonNull Builder());

        final Leaf leaf = method.getAnnotation(Leaf.class);

        this.literal = LiteralArgumentBuilder.literal(leaf.value());

        this.builder.add(MetaKey.description, leaf.desc());
        this.builder.add(MetaKey.literal, leaf.value());

        Optional.ofNullable(method.getAnnotation(Permission.class)).ifPresent(permission -> this.builder.add(MetaKey.permission, new PermissionMeta<>(extension, permission).init()));
    }

    @Override
    public void build(@NonNull final LiteralArgumentBuilder<CS> root) {
        this.builder.get(MetaKey.permission).ifPresent(permission -> this.literal.requires(context -> {
            return permission.isPermitted(context);
        }));

        if (!this.arguments.isEmpty()) {
            RequiredArgumentBuilder<CS, ?> child = null;

            for (int index = this.arguments.size() - 1; index >= 0; index--) {
                final RequiredArgumentBuilder<CS, ?> node = this.arguments.get(index).getArgument();

                child = child == null ? node.executes(this::execute) : node.then(child);
            }

            root.then(this.literal.then(child));

            return;
        }

        root.then(this.literal.executes(this::execute));
    }

    @Override
    public @NonNull final CommandMeta meta() {
        return this.builder;
    }
}