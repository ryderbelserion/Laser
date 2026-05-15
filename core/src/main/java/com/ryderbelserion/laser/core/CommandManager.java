package com.ryderbelserion.laser.core;

import com.ryderbelserion.laser.core.api.AbstractCommand;
import com.ryderbelserion.laser.core.api.extensions.SenderExtension;
import net.kyori.adventure.audience.Audience;
import org.jspecify.annotations.NonNull;

public abstract class CommandManager<CS, S extends Audience> {

    public abstract void registerTree(@NonNull final AbstractCommand<CS, S> command);

    public abstract SenderExtension<CS, S> getExtension();

    public abstract void post(@NonNull final String command);

    public abstract void init();

}