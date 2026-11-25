package com.ryderbelserion.laser.core.objects.types;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.laser.core.CommandRegistry;
import com.ryderbelserion.laser.core.ContextRegistry;
import com.ryderbelserion.laser.core.api.annotations.other.Permission;
import com.ryderbelserion.laser.core.api.annotations.types.Leaf;
import com.ryderbelserion.laser.core.api.extensions.SenderExtension;
import com.ryderbelserion.laser.core.objects.meta.MetaKey;
import com.ryderbelserion.laser.core.objects.meta.interfaces.CommandMeta;
import com.ryderbelserion.laser.core.objects.meta.types.ArgumentMeta;
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
            @NonNull final CommandRegistry<CS, S> commandRegistry,
            @NonNull final ContextRegistry<CS> contextRegistry,
            @NonNull final SenderExtension<CS, S> extension,
            @NonNull final Object instance,
            @NonNull final AbstractLogger logger,
            @NonNull final Method method
    ) {
        super(commandRegistry, contextRegistry, extension, instance, method, logger, new CommandMeta.@NonNull Builder());

        final Leaf leaf = method.getAnnotation(Leaf.class);

        this.literal = LiteralArgumentBuilder.literal(leaf.value());

        this.builder.add(MetaKey.description, leaf.desc());
        this.builder.add(MetaKey.aliases, leaf.aliases());
        this.builder.add(MetaKey.literal, leaf.value());

        Optional.ofNullable(method.getAnnotation(Permission.class)).ifPresent(permission -> this.builder.add(MetaKey.permission, new PermissionMeta<>(extension, permission).init()));
    }

    @Override
    public void build(@NonNull final LiteralArgumentBuilder<CS> root) {
        this.builder.get(MetaKey.permission).ifPresent(permission -> this.literal.requires(context -> {
            return permission.isPermitted(context);
        }));

        final LiteralCommandNode<CS> node;

        if (!this.arguments.isEmpty()) {
            RequiredArgumentBuilder<CS, ?> child = null;

            for (int index = this.arguments.size() - 1; index >= 0; index--) {
                final ArgumentMeta<CS, S> meta = this.arguments.get(index);

                if (child == null) {
                    meta.getArgument().executes(this::execute);
                } else {
                    meta.then(child);
                }

                child = meta.getArgument();
            }

            node = this.literal.then(child).build();
        } else {
            node = this.literal.executes(this::execute).build();
        }

        root.then(node);

        final Optional<String[]> optional = this.builder.get(MetaKey.aliases);

        if (optional.isPresent()) {
            final String[] aliases = optional.get();

            for (final String alias : aliases) {
                if (alias.isBlank()) {
                    continue;
                }

                final LiteralArgumentBuilder<CS> type = LiteralArgumentBuilder.literal(alias);

                type.requires(this.literal.getRequirement()).redirect(node);

                if (this.arguments.isEmpty()) {
                    type.executes(this::execute);
                }

                root.then(type);
            }
        }
    }

    @Override
    public @NonNull final CommandMeta meta() {
        return this.builder;
    }
}