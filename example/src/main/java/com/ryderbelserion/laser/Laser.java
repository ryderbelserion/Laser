package com.ryderbelserion.laser;

import com.ryderbelserion.laser.commands.LaserCommand;
import com.ryderbelserion.laser.paper.PaperCommandManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.List;

public class Laser extends JavaPlugin {

    @Override
    public void onEnable() {
        final PaperCommandManager commandManager = new PaperCommandManager(this);

        List.of(
                new LaserCommand()
        ).forEach(commandManager::registerTree);
    }
}