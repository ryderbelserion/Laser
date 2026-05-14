package com.ryderbelserion.laser.core.api.extensions;

import com.ryderbelserion.laser.core.api.interfaces.CommandResult;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import java.util.Set;

public abstract class SenderExtension<CS, S extends Audience> {

    public @NonNull abstract CommandResult validateSender(@NonNull final CS source, @NonNull final Class<?> type);

    public abstract boolean isPermitted(@NonNull final S sender, @NonNull final String permission);

    public @NonNull abstract S mapSender(@NonNull final CS source, @NonNull final Class<?> type);

    public @NonNull abstract S mapAudience(@NonNull final CS source);

    public @NonNull abstract Class<? extends CS> getSenderType(@NotNull final Class<?> type);

    public @NonNull abstract Set<Class<?>> getValidSenders();

    public void sendMessage(@NonNull final S sender, @NonNull final String message) {
        sender.sendMessage(MiniMessage.miniMessage().deserialize(message));
    }

    public @NonNull CommandResult error(@NonNull final String message) {
        return new CommandResult.Error(message);
    }

    public @NonNull CommandResult safe() {
        return new CommandResult.Safe();
    }
}