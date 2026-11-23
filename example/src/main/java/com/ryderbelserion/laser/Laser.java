package com.ryderbelserion.laser;

import com.ryderbelserion.laser.commands.LaserCommand;
import com.ryderbelserion.laser.core.SuggestionManager;
import com.ryderbelserion.laser.paper.PaperCommandManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.List;

public class Laser extends JavaPlugin {

    @Override
    public void onEnable() {
        final PaperCommandManager commandManager = new PaperCommandManager(this);

        final SuggestionManager suggestionManager = commandManager.getSuggestionManager();

        suggestionManager.registerSuggestion("amount", List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15));
        suggestionManager.registerSuggestion("item", List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20));

        List.of(
                new LaserCommand()
        ).forEach(commandManager::registerTree);
    }
}