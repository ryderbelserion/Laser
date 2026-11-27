package com.ryderbelserion.laser;

import com.ryderbelserion.laser.commands.LaserCommand;
import com.ryderbelserion.laser.core.ContextRegistry;
import com.ryderbelserion.laser.core.objects.keys.LaserKey;
import com.ryderbelserion.laser.paper.PaperCommandRegistry;
import com.ryderbelserion.laser.resolvers.PlayerResolver;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.List;

public class Laser extends JavaPlugin {

    @Override
    public void onEnable() {
        final PaperCommandRegistry commandManager = new PaperCommandRegistry(this);

        final ContextRegistry<CommandSourceStack> contextRegistry = commandManager.getSuggestionManager();

        contextRegistry.register(new PlayerResolver());

        commandManager.registerSuggestion(LaserKey.of("amount"), context -> {
            if (context.sender().getSender().hasPermission("laser.admin")) {
                final List<Integer> values = new ArrayList<>();

                for (int count = 1; count < 100; count++) {
                    values.add(count);
                }

                return values;
            }

            return List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        });

        contextRegistry.register(LaserKey.of("item"), List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20));

        List.of(
                new LaserCommand()
        ).forEach(commandManager::registerTree);
    }
}