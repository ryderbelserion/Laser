package com.ryderbelserion.laser.velocity;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.ryderbelserion.laser.core.CommandRegistry;
import com.ryderbelserion.laser.core.ContextRegistry;
import com.ryderbelserion.laser.core.api.AbstractCommand;
import com.ryderbelserion.laser.core.objects.RootCommandProcessor;
import com.ryderbelserion.laser.core.objects.keys.LaserKey;
import com.ryderbelserion.laser.core.objects.meta.MetaKey;
import com.ryderbelserion.laser.core.objects.meta.interfaces.CommandMeta;
import com.ryderbelserion.laser.core.objects.meta.types.ArgumentMeta;
import com.ryderbelserion.laser.core.objects.suggestions.interfaces.SuggestionSupplier;
import com.ryderbelserion.laser.core.objects.types.TreeCommandProcessor;
import com.ryderbelserion.laser.velocity.extensions.VelocityLoggerExtension;
import com.ryderbelserion.laser.velocity.extensions.VelocitySenderExtension;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class VelocityCommandRegistry extends CommandRegistry<CommandSource, CommandSource> {

    private final VelocitySenderExtension extension;
    private final VelocityLoggerExtension logger;
    private final ProxyServer server;
    private final Object plugin;

    public VelocityCommandRegistry(final Object plugin, final ComponentLogger logger, final ProxyServer server) {
        this.logger = new VelocityLoggerExtension(logger);
        this.extension = new VelocitySenderExtension(this.server = server);
        this.plugin = plugin;
    }

    @Override
    public void registerTree(final AbstractCommand<CommandSource, CommandSource> command) {
        final RootCommandProcessor<CommandSource, CommandSource> processor = new RootCommandProcessor<>(this, this.contextRegistry, this.extension, this.logger, command);

        final TreeCommandProcessor<CommandSource, CommandSource> tree = processor.getTreeProcessor();

        final CommandManager commandManager = this.server.getCommandManager();

        final CommandMeta meta = tree.meta();

        commandManager.register(
                commandManager.metaBuilder(meta.get(MetaKey.literal).orElse("")).plugin(this.plugin).aliases(meta.get(MetaKey.aliases).orElse(new String[0])).build(),
                new BrigadierCommand(tree.literal().build())
        );
    }

    @Override
    public void registerSuggestion(final LaserKey key, final SuggestionSupplier<CommandSource> supplier) {
        this.contextRegistry.register(key, supplier);
    }

    @Override
    public ArgumentType<?> mapArgumentType(final ArgumentMeta<CommandSource, CommandSource> meta) {
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
    public ContextRegistry<CommandSource> getSuggestionManager() {
        return this.contextRegistry;
    }

    @Override
    public void post(final String command) {

    }

    @Override
    public void init() {
        this.extension.init();
    }

    @Override
    public VelocitySenderExtension getExtension() {
        return this.extension;
    }

    @Override
    public VelocityLoggerExtension getLogger() {
        return this.logger;
    }
}