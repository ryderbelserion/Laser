package com.ryderbelserion.laser.paper.extensions;

import com.ryderbelserion.laser.core.api.interfaces.CommandResult;
import com.ryderbelserion.laser.core.api.extensions.SenderExtension;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import java.util.Set;

public final class PaperSenderExtension extends SenderExtension<CommandSourceStack, CommandSender> {

    private final Set<Class<?>> senders = Set.of(ConsoleCommandSender.class, CommandSender.class, Player.class);

    @Override
    public @NonNull CommandResult validateSender(@NonNull final CommandSourceStack source, @NonNull final Class<?> type) {
        if (!this.senders.contains(type)) {
            throw new IllegalStateException("There is no valid sender supplied!");
        }

        if (Player.class.isAssignableFrom(type)) {
            return error("You must be a player to run this command!");
        }

        if (ConsoleCommandSender.class.isAssignableFrom(type)) {
            return error("You must be executing this command from console!");
        }

        return safe();
    }

    @Override
    public boolean isPermitted(@NonNull final CommandSender sender, @NonNull final String permission) {
        return sender.hasPermission(permission);
    }

    @Override
    public @NonNull CommandSender mapSender(@NonNull final CommandSourceStack source, @NonNull final Class<?> type) {
        if (!this.senders.contains(type)) {
            throw new IllegalStateException("There is no valid sender supplied!");
        }

        final CommandSender sender = source.getSender();

        if (Player.class.isAssignableFrom(type) && sender instanceof Player player) {
            return player;
        }

        if (ConsoleCommandSender.class.isAssignableFrom(type) && sender instanceof ConsoleCommandSender console) {
            return console;
        }

        return sender;
    }

    @Override
    public @NonNull CommandSender mapAudience(@NonNull final CommandSourceStack source) {
        return source.getSender();
    }

    @Override
    public @NonNull Class<? extends CommandSourceStack> getSenderType(@NotNull final Class<?> klass) {
        if (!this.getValidSenders().contains(klass)) {
            throw new IllegalStateException("%s is not a valid sender.".formatted(klass.getSimpleName()));
        }

        return (Class<? extends CommandSourceStack>) klass;
    }

    @Override
    public @NonNull Set<Class<?>> getValidSenders() {
        return this.senders;
    }
}