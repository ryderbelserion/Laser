package com.ryderbelserion.laser.core.objects.types;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.laser.core.api.AbstractCommand;
import com.ryderbelserion.laser.core.api.AbstractProcessor;
import com.ryderbelserion.laser.core.api.annotations.Tree;
import com.ryderbelserion.laser.core.api.annotations.other.Permission;
import com.ryderbelserion.laser.core.api.extensions.SenderExtension;
import com.ryderbelserion.laser.core.meta.interfaces.CommandMeta;
import com.ryderbelserion.laser.core.meta.MetaKey;
import com.ryderbelserion.laser.core.meta.types.PermissionMeta;
import net.kyori.adventure.audience.Audience;
import org.jspecify.annotations.NonNull;
import java.lang.reflect.Method;
import java.util.Optional;

public class TreeCommandProcessor<CS, S extends Audience> extends AbstractProcessor<CS, S> {

    /**
     * Builds the initial start of the literal chain, which is usually /laser
     *
     * @param command the parent class
     */
    public TreeCommandProcessor(@NonNull final SenderExtension<CS, S> extension, @NonNull final AbstractCommand<CS, S> command, @NonNull final Object object) {
        super(extension, object, new CommandMeta.Builder());

        final Class<?> klass = command.getClass();

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

            flower(methods).ifPresent(FlowerCommandProcessor::build);

            leaf(methods).forEach(LeafCommandProcessor::build);
        });

        return this;
    }

    @Override
    public @NonNull final CommandMeta meta() {
        return this.builder;
    }
}