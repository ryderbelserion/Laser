package com.ryderbelserion.laser.resolvers;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ryderbelserion.laser.core.objects.arguments.ArgumentResolver;
import com.ryderbelserion.laser.core.objects.suggestions.SuggestionSource;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import java.util.List;

@NullMarked
public class PlayerResolver implements ArgumentResolver<CommandSourceStack, Player> {

    @Override
    public Class<Player> type() {
        return Player.class;
    }

    @Override
    public ArgumentType<?> argumentType() {
        return ArgumentTypes.player();
    }

    @Override
    public List<?> suggest(final SuggestionSource<CommandSourceStack> source) {
        return Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName).toList();
    }

    @Override
    public Player resolve(final String argumentName, final CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        return context.getArgument(argumentName, PlayerSelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
    }
}