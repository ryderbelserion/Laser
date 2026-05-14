package com.ryderbelserion.laser.core.objects.types.leaf;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.laser.core.api.annotations.other.Permission;
import com.ryderbelserion.laser.core.api.annotations.types.Leaf;
import com.ryderbelserion.laser.core.api.extensions.SenderExtension;
import com.ryderbelserion.laser.core.meta.MetaKey;
import com.ryderbelserion.laser.core.meta.interfaces.CommandMeta;
import com.ryderbelserion.laser.core.meta.types.PermissionMeta;
import com.ryderbelserion.laser.core.objects.types.ArgumentProcessor;
import net.kyori.adventure.audience.Audience;
import org.jspecify.annotations.NonNull;
import java.lang.reflect.Method;
import java.util.Optional;

public class LeafCommandProcessor<CS, S extends Audience> extends ArgumentProcessor<CS, S> {

    private final LiteralArgumentBuilder<CS> literal;
    private final LiteralArgumentBuilder<CS> root;

    public LeafCommandProcessor(
            @NonNull final SenderExtension<CS, S> extension,
            @NonNull final LiteralArgumentBuilder<CS> root,
            @NonNull final Object instance,
            @NonNull final Method method
    ) {
        super(extension, instance, method, new CommandMeta.Builder());

        final Leaf leaf = method.getAnnotation(Leaf.class);

        this.literal = LiteralArgumentBuilder.literal(leaf.value());

        this.builder.add(MetaKey.description, leaf.desc());
        this.builder.add(MetaKey.literal, leaf.value());

        Optional.ofNullable(method.getAnnotation(Permission.class)).ifPresent(permission -> this.builder.add(MetaKey.permission, new PermissionMeta<>(extension, permission).init()));

        this.root = root;
    }

    @Override
    public void build() {
        this.builder.get(MetaKey.permission).ifPresent(permission -> this.literal.requires(context -> {
            return permission.isPermitted(context);
        }));

        this.root.then(this.literal.executes(this::execute));
    }

    @Override
    public @NonNull final CommandMeta meta() {
        return this.builder;
    }
}