package com.ryderbelserion.laser.resolvers;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.ryderbelserion.laser.core.objects.arguments.ArgumentResolver;
import com.ryderbelserion.laser.core.objects.suggestions.SuggestionSource;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import org.jspecify.annotations.NullMarked;
import java.util.List;

@NullMarked
public class PlayerResolver implements ArgumentResolver<CommandSource, Player> {

    private final ProxyServer server;

    public PlayerResolver(final ProxyServer server) {
        this.server = server;
    }

    @Override
    public Class<Player> type() {
        return Player.class;
    }

    @Override
    public ArgumentType<?> argumentType() {
        return StringArgumentType.string();
    }

    @Override
    public List<?> suggest(final SuggestionSource<CommandSource> source) {
        return this.server.getAllPlayers().stream().map(Player::getUsername).toList();
    }

    @Override
    public Player resolve(final String argumentName, final CommandContext<CommandSource> context) {
        return context.getArgument(argumentName, Player.class);
    }
}