package com.ryderbelserion.laser.core;

import com.mojang.brigadier.arguments.ArgumentType;
import com.ryderbelserion.laser.core.api.AbstractCommand;
import com.ryderbelserion.laser.core.api.AbstractLogger;
import com.ryderbelserion.laser.core.api.extensions.SenderExtension;
import com.ryderbelserion.laser.core.objects.meta.types.ArgumentMeta;
import com.ryderbelserion.laser.core.objects.keys.LaserKey;
import com.ryderbelserion.laser.core.objects.suggestions.interfaces.SuggestionSupplier;
import net.kyori.adventure.audience.Audience;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class CommandRegistry<CS, S extends Audience> {

    protected final ContextRegistry<CS> contextRegistry;

    public CommandRegistry() {
        this.contextRegistry = new ContextRegistry<>();
    }

    public abstract void registerTree(final AbstractCommand command);

    public abstract void registerSuggestion(final LaserKey key, final SuggestionSupplier<CS> supplier);

    public abstract ArgumentType<?> mapArgumentType(final ArgumentMeta<CS, S> klass);

    public abstract ContextRegistry<CS> getSuggestionManager();

    public abstract SenderExtension<CS, S> getExtension();

    public abstract AbstractLogger getLogger();

    public abstract void post(final String command);

    public abstract void init();
}