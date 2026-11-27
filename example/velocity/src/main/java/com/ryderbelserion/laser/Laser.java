package com.ryderbelserion.laser;

import com.google.inject.Inject;
import com.ryderbelserion.laser.commands.LaserCommand;
import com.ryderbelserion.laser.core.ContextRegistry;
import com.ryderbelserion.laser.core.objects.keys.LaserKey;
import com.ryderbelserion.laser.resolvers.PlayerResolver;
import com.ryderbelserion.laser.velocity.VelocityCommandRegistry;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import java.util.ArrayList;
import java.util.List;

@Plugin(id = "laser", version = "1.0.0", description = "An example plugin", authors = "ryderbelserion")
public class Laser {

    private final ProxyServer server;

    @Inject
    public Laser(final ProxyServer server) {
        this.server = server;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        final VelocityCommandRegistry commandManager = new VelocityCommandRegistry(this, ComponentLogger.logger("Laser"), this.server);

        final ContextRegistry<CommandSource> contextRegistry = commandManager.getSuggestionManager();

        contextRegistry.register(new PlayerResolver(this.server));

        commandManager.registerSuggestion(LaserKey.of("amount"), context -> {
            if (context.sender().hasPermission("laser.admin")) {
                final List<Integer> values = new ArrayList<>();

                for (int count = 1; count < 100; count++) {
                    values.add(count);
                }

                return values;
            }

            return List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        });

        contextRegistry.register(LaserKey.of("item"),
                List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20));

        List.of(
                new LaserCommand()
        ).forEach(commandManager::registerTree);
    }
}