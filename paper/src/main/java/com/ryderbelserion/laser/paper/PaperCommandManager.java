package com.ryderbelserion.laser.paper;

import com.ryderbelserion.laser.core.CommandManager;
import com.ryderbelserion.laser.core.api.AbstractCommand;
import com.ryderbelserion.laser.core.meta.MetaKey;
import com.ryderbelserion.laser.core.objects.RootCommandProcessor;
import com.ryderbelserion.laser.core.objects.types.TreeCommandProcessor;
import com.ryderbelserion.laser.paper.extensions.PaperLoggerExtension;
import com.ryderbelserion.laser.paper.extensions.PaperSenderExtension;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;

public final class PaperCommandManager extends CommandManager<CommandSourceStack, CommandSender> {

    private final PaperLoggerExtension logger;
    private final JavaPlugin plugin;

    public PaperCommandManager(@NonNull final JavaPlugin plugin) {
        this.logger = new PaperLoggerExtension(this.plugin = plugin);

        init();
    }

    private PaperSenderExtension extension;

    @Override
    public void registerTree(@NonNull final AbstractCommand<CommandSourceStack, CommandSender> command) {
        final RootCommandProcessor<CommandSourceStack, CommandSender> processor = new RootCommandProcessor<>(this.extension, this.logger, command);

        final TreeCommandProcessor<CommandSourceStack, CommandSender> tree = processor.getTreeProcessor();

        final LifecycleEventManager<Plugin> eventManager = this.plugin.getLifecycleManager();

        eventManager.registerEventHandler(LifecycleEvents.COMMANDS, event -> event.registrar().register(tree.literal().build(), tree.meta().get(MetaKey.description).orElse("N/A")));
    }

    @Override
    public void post(@NonNull final String command) {

    }

    @Override
    public void init() {
        this.extension = new PaperSenderExtension(this.plugin);
    }

    @Override
    public @NonNull PaperSenderExtension getExtension() {
        return this.extension;
    }
}