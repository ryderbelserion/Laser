package com.ryderbelserion.laser.paper.extensions;

import com.ryderbelserion.laser.core.api.interfaces.CommandResult;
import com.ryderbelserion.laser.core.api.extensions.SenderExtension;
import com.ryderbelserion.laser.core.api.message.MessageKey;
import com.ryderbelserion.laser.core.api.message.MessageRegistry;
import com.ryderbelserion.laser.core.enums.PermissionMode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import java.util.Set;

public final class PaperSenderExtension extends SenderExtension<CommandSourceStack, CommandSender> {

    private final Set<Class<?>> senders = Set.of(ConsoleCommandSender.class, CommandSender.class, Player.class);

    private final JavaPlugin plugin;

    public PaperSenderExtension(@NonNull final JavaPlugin plugin) {
        super(new MessageRegistry<>());

        this.plugin = plugin;
    }

    @Override
    public void init() {
        this.registry.register(MessageKey.console_only, (sender, _) -> sender.sendRichMessage("<yellow>[Laser] <red>Only console players can execute this command!"));
        this.registry.register(MessageKey.player_only, ((sender, _) -> sender.sendRichMessage("<yellow>[Laser] <red>Only players can execute this command!")));
    }

    @Override
    public @NonNull CommandResult validateSender(@NonNull final CommandSourceStack source, @NonNull final Class<?> type) {
        if (!this.senders.contains(type)) {
            throw new IllegalStateException("There is no valid sender supplied!");
        }

        final CommandSender sender = source.getSender();

        if (Player.class.isAssignableFrom(type) && (!(sender instanceof Player))) {
            return error(MessageKey.player_only);
        }

        if (ConsoleCommandSender.class.isAssignableFrom(type) && !(sender instanceof ConsoleCommandSender)) {
            return error(MessageKey.console_only);
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
    public void registerPermission(@NonNull final String permission, @NonNull final PermissionMode mode) {
        final PluginManager server = this.plugin.getServer().getPluginManager();

        final PermissionDefault permissionDefault = switch (mode) {
            case TRUE -> PermissionDefault.TRUE;
            case FALSE -> PermissionDefault.FALSE;
            default -> PermissionDefault.OP;
        };

        if (server.getPermission(permission) != null) {
            return;
        }

        server.addPermission(new Permission(permission, permissionDefault));
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