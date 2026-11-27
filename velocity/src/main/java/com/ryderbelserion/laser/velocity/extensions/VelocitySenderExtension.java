package com.ryderbelserion.laser.velocity.extensions;

import com.ryderbelserion.laser.core.api.extensions.SenderExtension;
import com.ryderbelserion.laser.core.api.interfaces.CommandResult;
import com.ryderbelserion.laser.core.api.message.MessageKey;
import com.ryderbelserion.laser.core.api.message.MessageRegistry;
import com.ryderbelserion.laser.core.enums.PermissionMode;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import org.jspecify.annotations.NullMarked;
import java.util.Set;

@NullMarked
public final class VelocitySenderExtension extends SenderExtension<CommandSource, CommandSource> {

    private final Set<Class<?>> senders = Set.of(ConsoleCommandSource.class, CommandSource.class, Player.class);

    private final ProxyServer proxy;

    public VelocitySenderExtension(final ProxyServer proxy) {
        super(new MessageRegistry<>());

        this.proxy = proxy;
    }

    @Override
    public void init() {
        this.registry.register(MessageKey.console_only, (sender, _) -> sender.sendRichMessage("<yellow>[Laser] <red>Only console players can execute this command!"));
        this.registry.register(MessageKey.player_only, (sender, _) -> sender.sendRichMessage("<yellow>[Laser] <red>Only players can execute this command!"));
    }

    @Override
    public CommandResult validateSender(final CommandSource source, final Class<?> type) {
        if (!this.senders.contains(type)) {
            throw new IllegalStateException("There is no valid sender supplied!");
        }

        if (Player.class.isAssignableFrom(type) && !(source instanceof Player)) {
            return error(MessageKey.player_only);
        }

        if (ConsoleCommandSource.class.isAssignableFrom(type) && !(source instanceof ConsoleCommandSource)) {
            return error(MessageKey.console_only);
        }

        return safe();
    }

    @Override
    public boolean isPermitted(final CommandSource sender, final String permission) {
        return sender.hasPermission(permission);
    }

    @Override
    public CommandSource mapSender(final CommandSource source, final Class<?> type) {
        if (!this.senders.contains(type)) {
            throw new IllegalStateException("There is no valid sender supplied!");
        }

        if (Player.class.isAssignableFrom(type) && source instanceof Player player) {
            return player;
        }

        if (ConsoleCommandSource.class.isAssignableFrom(type) && source instanceof ConsoleCommandSource console) {
            return console;
        }

        return source;
    }

    @Override
    public void registerPermission(final String permission, final PermissionMode mode) {
        // does nothing on velocity
    }

    @Override
    public CommandSource mapAudience(final CommandSource source) {
        return source; // nothing to map lol
    }

    @Override
    public Class<? extends CommandSource> getSenderType(final Class<?> klass) {
        if (!this.getValidSenders().contains(klass)) {
            throw new IllegalStateException("%s is not a valid sender.".formatted(klass.getSimpleName()));
        }

        return (Class<? extends CommandSource>) klass;
    }

    @Override
    public Set<Class<?>> getValidSenders() {
        return this.senders;
    }
}