package com.ryderbelserion.laser.core.objects.types;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.laser.core.api.AbstractCommand;
import com.ryderbelserion.laser.core.api.AbstractProcessor;
import com.ryderbelserion.laser.core.api.annotations.Tree;
import com.ryderbelserion.laser.core.api.annotations.other.Permission;
import com.ryderbelserion.laser.core.api.annotations.types.Branch;
import com.ryderbelserion.laser.core.api.extensions.SenderExtension;
import com.ryderbelserion.laser.core.meta.interfaces.CommandMeta;
import com.ryderbelserion.laser.core.meta.MetaKey;
import com.ryderbelserion.laser.core.meta.types.PermissionMeta;
import net.kyori.adventure.audience.Audience;
import com.ryderbelserion.laser.core.api.AbstractLogger;
import org.jspecify.annotations.NonNull;
import java.lang.reflect.Method;
import java.util.Optional;

public class TreeCommandProcessor<CS, S extends Audience> extends AbstractProcessor<CS, S> {

    private final AbstractCommand<CS, S> command;

    /**
     * Builds the initial start of the literal chain, which is usually /laser
     *
     * @param command the parent class
     */
    public TreeCommandProcessor(@NonNull final SenderExtension<CS, S> extension, @NonNull final AbstractCommand<CS, S> command, @NonNull final AbstractLogger logger) {
        super(extension, command, logger, new CommandMeta.@NonNull Builder());

        this.command = command;

        final Class<?> klass = this.command.getClass();

        final Tree tree = klass.getAnnotation(Tree.class);

        this.literal = LiteralArgumentBuilder.literal(tree.value());

        this.builder.add(MetaKey.description, tree.desc());
        this.builder.add(MetaKey.literal, tree.value());
        this.builder.add(MetaKey.klass, klass);

        Optional.ofNullable(klass.getAnnotation(Permission.class)).ifPresent(permission -> this.builder.add(MetaKey.permission, new PermissionMeta<>(extension, permission).init()));
    }

    @Override
    public @NonNull final LiteralArgumentBuilder<CS> literal() {
        return this.literal;
    }

    @Override
    public @NonNull final TreeCommandProcessor<CS, S> build() {
        this.builder.get(MetaKey.klass).ifPresent(klass -> {
            final Method[] methods = klass.getDeclaredMethods();

            flower(methods).ifPresent(flower -> flower.build(this.literal));

            leaf(methods).forEach(leaf -> leaf.build(this.literal));
        });

        for (final Object branch : this.command.getBranches()) {
            final Class<?> klass = branch.getClass();

            if (!klass.isAnnotationPresent(Branch.class)) continue;

            final BranchCommandProcessor<CS, S> processor = new BranchCommandProcessor<>(
                    this.extension,
                    this.logger,
                    branch
            );

            this.literal.then(processor.build().literal());
        }

        return this;
    }

    @Override
    public @NonNull final CommandMeta meta() {
        return this.builder;
    }
}