package com.ryderbelserion.laser.core.api.extensions;

import com.ryderbelserion.laser.core.api.interfaces.CommandResult;
import com.ryderbelserion.laser.core.api.message.MessageKey;
import com.ryderbelserion.laser.core.api.message.MessageRegistry;
import com.ryderbelserion.laser.core.api.message.context.MessageContext;
import com.ryderbelserion.laser.core.enums.PermissionMode;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import java.util.Set;

public abstract class SenderExtension<CS, S extends Audience> {

    protected final MessageRegistry<S> registry;

    public SenderExtension(@NonNull final MessageRegistry<S> registry) {
        this.registry = registry;
    }

    public @NonNull abstract CommandResult validateSender(@NonNull final CS source, @NonNull final Class<?> type);

    public abstract void registerPermission(@NonNull final String permission, @NonNull final PermissionMode mode);

    public abstract boolean isPermitted(@NonNull final S sender, @NonNull final String permission);

    public @NonNull abstract S mapSender(@NonNull final CS source, @NonNull final Class<?> type);

    public @NonNull abstract S mapAudience(@NonNull final CS source);

    public @NonNull abstract Class<? extends CS> getSenderType(@NotNull final Class<?> type);

    public @NonNull abstract Set<Class<?>> getValidSenders();

    public abstract void init();

    public void sendMessage(@NonNull final S sender, @NonNull final String message) {
        sender.sendMessage(MiniMessage.miniMessage().deserialize(message));
    }

    public @NonNull final MessageRegistry<S> getRegistry() {
        return this.registry;
    }

    public @NonNull CommandResult error(@NonNull final MessageKey<@NonNull MessageContext> key) {
        return new CommandResult.Error(key);
    }

    public @NonNull CommandResult safe() {
        return new CommandResult.Safe();
    }
}