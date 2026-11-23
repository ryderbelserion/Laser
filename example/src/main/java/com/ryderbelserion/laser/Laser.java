package com.ryderbelserion.laser;

import com.ryderbelserion.laser.commands.LaserCommand;
import com.ryderbelserion.laser.core.ResolverManager;
import com.ryderbelserion.laser.core.objects.suggestions.SuggestionKey;
import com.ryderbelserion.laser.paper.PaperCommandManager;
import com.ryderbelserion.laser.resolvers.PlayerResolver;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.List;

public class Laser extends JavaPlugin {

    @Override
    public void onEnable() {
        final PaperCommandManager commandManager = new PaperCommandManager(this);

        final ResolverManager<CommandSourceStack> resolverManager = commandManager.getSuggestionManager();

        resolverManager.register(new PlayerResolver());

        //resolverManager.register(SuggestionKey.of("players"), _ ->
        //        getServer().getOnlinePlayers().stream().map(Player::getName).toList());

        commandManager.registerSuggestion(SuggestionKey.of("amount"), context -> {
            if (context.sender().getSender().hasPermission("laser.admin")) {
                final List<Integer> values = new ArrayList<>();

                for (int count = 1; count < 100; count++) {
                    values.add(count);
                }

                return values;
            }

            return List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        });

        resolverManager.register(SuggestionKey.of("item"), List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20));

        List.of(
                new LaserCommand()
        ).forEach(commandManager::registerTree);
    }
}