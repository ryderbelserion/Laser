package com.ryderbelserion.laser.paper;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.ryderbelserion.laser.core.CommandRegistry;
import com.ryderbelserion.laser.core.ContextRegistry;
import com.ryderbelserion.laser.core.api.AbstractCommand;
import com.ryderbelserion.laser.core.objects.meta.MetaKey;
import com.ryderbelserion.laser.core.objects.meta.interfaces.CommandMeta;
import com.ryderbelserion.laser.core.objects.RootCommandProcessor;
import com.ryderbelserion.laser.core.objects.meta.types.ArgumentMeta;
import com.ryderbelserion.laser.core.objects.keys.LaserKey;
import com.ryderbelserion.laser.core.objects.suggestions.interfaces.SuggestionSupplier;
import com.ryderbelserion.laser.core.objects.types.TreeCommandProcessor;
import com.ryderbelserion.laser.paper.extensions.PaperLoggerExtension;
import com.ryderbelserion.laser.paper.extensions.PaperSenderExtension;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;
import java.util.Arrays;

@NullMarked
public final class PaperCommandRegistry extends CommandRegistry<CommandSourceStack, CommandSender> {

    private final PaperLoggerExtension logger;
    private final JavaPlugin plugin;

    public PaperCommandRegistry(final JavaPlugin plugin) {
        this.logger = new PaperLoggerExtension(this.plugin = plugin);

        init();
    }

    private PaperSenderExtension extension;

    @Override
    public void registerTree(final AbstractCommand command) {
        final RootCommandProcessor<CommandSourceStack, CommandSender> processor = new RootCommandProcessor<>(this, this.contextRegistry, this.extension, this.logger, command);

        final TreeCommandProcessor<CommandSourceStack, CommandSender> tree = processor.getTreeProcessor();

        final LifecycleEventManager<Plugin> eventManager = this.plugin.getLifecycleManager();

        final CommandMeta meta = tree.meta();

        eventManager.registerEventHandler(LifecycleEvents.COMMANDS, event -> event.registrar().register(
                tree.literal().build(),
                meta.get(MetaKey.description).orElse("N/A"),
                Arrays.asList(meta.get(MetaKey.aliases).orElse(new String[0]))
        ));
    }

    @Override
    public void registerSuggestion(final LaserKey key, final SuggestionSupplier<CommandSourceStack> supplier) {
        this.contextRegistry.register(key, supplier);
    }

    @Override
    public ArgumentType<?> mapArgumentType(final ArgumentMeta<CommandSourceStack, CommandSender> meta) {
        final Class<?> klass = meta.getParsedType();

        ArgumentType<?> argumentType = StringArgumentType.string();

        if (klass == int.class || klass == Integer.class) {
            argumentType = IntegerArgumentType.integer();
        } else if (klass == double.class || klass == Double.class) {
            argumentType = DoubleArgumentType.doubleArg();
        } else if (klass == boolean.class || klass == Boolean.class) {
            argumentType = BoolArgumentType.bool();
        } else if (klass == long.class || klass == Long.class) {
            argumentType = LongArgumentType.longArg();
        } else if (klass == String.class) {
            argumentType = meta.isGreedy() ? StringArgumentType.greedyString() : StringArgumentType.string();
        }

        return argumentType;
    }

    @Override
    public ContextRegistry<CommandSourceStack> getSuggestionManager() {
        return this.contextRegistry;
    }

    @Override
    public void post(final String command) {

    }

    @Override
    public void init() {
        this.extension = new PaperSenderExtension(this.plugin);
        this.extension.init();
    }

    @Override
    public PaperSenderExtension getExtension() {
        return this.extension;
    }

    @Override
    public PaperLoggerExtension getLogger() {
        return this.logger;
    }
}