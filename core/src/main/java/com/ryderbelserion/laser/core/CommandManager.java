package com.ryderbelserion.laser.core;

import com.mojang.brigadier.arguments.ArgumentType;
import com.ryderbelserion.laser.core.api.AbstractCommand;
import com.ryderbelserion.laser.core.api.extensions.SenderExtension;
import com.ryderbelserion.laser.core.objects.meta.types.ArgumentMeta;
import com.ryderbelserion.laser.core.objects.suggestions.SuggestionKey;
import com.ryderbelserion.laser.core.objects.suggestions.interfaces.SuggestionSupplier;
import net.kyori.adventure.audience.Audience;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class CommandManager<CS, S extends Audience> {

    protected final ResolverManager<CS> resolverManager;

    public CommandManager() {
        this.resolverManager = new ResolverManager<>();
    }

    public abstract void registerTree(final AbstractCommand<CS, S> command);

    public abstract void registerSuggestion(final SuggestionKey key, final SuggestionSupplier<CS> supplier);

    public abstract ArgumentType<?> mapArgumentType(final ArgumentMeta<CS, S> klass);

    public abstract ResolverManager<CS> getSuggestionManager();

    public abstract SenderExtension<CS, S> getExtension();

    public abstract void post(final String command);

    public abstract void init();
}