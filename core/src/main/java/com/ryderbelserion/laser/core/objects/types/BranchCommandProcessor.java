package com.ryderbelserion.laser.core.objects.types;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.laser.core.api.AbstractProcessor;
import com.ryderbelserion.laser.core.api.annotations.other.Permission;
import com.ryderbelserion.laser.core.api.annotations.types.Branch;
import com.ryderbelserion.laser.core.api.extensions.SenderExtension;
import com.ryderbelserion.laser.core.meta.MetaKey;
import com.ryderbelserion.laser.core.meta.interfaces.CommandMeta;
import com.ryderbelserion.laser.core.meta.types.PermissionMeta;
import net.kyori.adventure.audience.Audience;
import com.ryderbelserion.laser.core.api.AbstractLogger;
import org.jspecify.annotations.NonNull;
import java.lang.reflect.Method;
import java.util.Optional;

public class BranchCommandProcessor<CS, S extends Audience> extends AbstractProcessor<CS, S> {

    public BranchCommandProcessor(@NonNull final SenderExtension<CS, S> extension, @NonNull final AbstractLogger logger, @NonNull final Object object) {
        super(extension, object, logger, new CommandMeta.@NonNull Builder());

        final Class<?> klass = object.getClass();

        final Branch branch = klass.getAnnotation(Branch.class);

        this.literal = LiteralArgumentBuilder.literal(branch.value());

        this.builder.add(MetaKey.description, branch.desc());
        this.builder.add(MetaKey.literal, branch.value());
        this.builder.add(MetaKey.klass, klass);

        Optional.ofNullable(klass.getAnnotation(Permission.class)).ifPresent(permission -> this.builder.add(MetaKey.permission, new PermissionMeta<>(extension, permission).init()));
    }

    @Override
    public @NonNull final LiteralArgumentBuilder<CS> literal() {
        return this.literal;
    }

    @Override
    public @NonNull final BranchCommandProcessor<CS, S> build() {
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