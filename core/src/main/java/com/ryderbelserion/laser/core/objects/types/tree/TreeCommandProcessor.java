package com.ryderbelserion.laser.core.objects.types.tree;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.laser.core.api.AbstractCommand;
import com.ryderbelserion.laser.core.api.AbstractProcessor;
import com.ryderbelserion.laser.core.api.annotations.Tree;
import com.ryderbelserion.laser.core.api.extensions.SenderExtension;
import com.ryderbelserion.laser.core.objects.types.flower.FlowerCommandProcessor;
import net.kyori.adventure.audience.Audience;
import org.jspecify.annotations.NonNull;
import java.lang.reflect.Method;

public class TreeCommandProcessor<CS, S extends Audience> extends AbstractProcessor<CS, S> {

    /**
     * Builds the initial start of the literal chain, which is usually /laser
     *
     * @param command the parent class
     */
    public TreeCommandProcessor(@NonNull final SenderExtension<CS, S> extension, @NonNull final AbstractCommand<CS, S> command, @NonNull final Object object) {
        final Tree tree = command.getClass().getAnnotation(Tree.class);

        super(extension, object, tree.desc());

        this.literal = LiteralArgumentBuilder.literal(tree.value());
    }

    @Override
    public @NonNull final LiteralArgumentBuilder<CS> literal() {
        return this.literal;
    }

    @Override
    public @NonNull final TreeCommandProcessor<CS, S> build() {
        final Method[] methods = this.object.getClass().getDeclaredMethods();

        flower(methods).ifPresent(FlowerCommandProcessor::build);

        return this;
    }
}