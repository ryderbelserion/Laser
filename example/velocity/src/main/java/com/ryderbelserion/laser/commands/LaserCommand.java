package com.ryderbelserion.laser.commands;

import com.ryderbelserion.laser.commands.types.SubCommand;
import com.ryderbelserion.laser.core.api.AbstractCommand;
import com.ryderbelserion.laser.core.api.annotations.Flower;
import com.ryderbelserion.laser.core.api.annotations.Tree;
import com.ryderbelserion.laser.core.api.annotations.other.Permission;
import com.ryderbelserion.laser.core.api.annotations.other.Suggestion;
import com.ryderbelserion.laser.core.api.annotations.types.Leaf;
import com.ryderbelserion.laser.core.enums.PermissionMode;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

@Tree(value = "laserv", desc = "The base command for Laser!", aliases = {"le"})
@Permission(permission = "laser.use")
public class LaserCommand extends AbstractCommand {

    private final ProxyServer server;

    public LaserCommand(final ProxyServer server) {
        this.server = server;

        addBranch(new SubCommand());
    }

    @Flower
    @Permission(permission = "laser.access")
    public void execute(ConsoleCommandSource sender) {
        sender.sendRichMessage("<yellow>This is the /laser command!");
    }

    @Leaf(value = "give", desc = "The give command", aliases = "deliver")
    @Permission(permission = {"laser.give", "laser.use"}, mode = PermissionMode.ANY_OF)
    public void give(
            Player player,
            @Suggestion(name = "amount", type = int.class) int amount,
            @Suggestion(name = "item", type = int.class) int item
    ) {
        if (amount > 0) {
            player.sendRichMessage("<red>Amount: %s".formatted(amount));
        }

        if (item > 0) {
            player.sendRichMessage("<green>%s is greater than 0!".formatted(item));
        }
    }

    @Leaf(value = "msg", desc = "Sends a message to a player.", aliases = "message")
    public void test(
            Player player,
            @Suggestion(name = "players", type = String.class) String target
    ) {
        this.server.getPlayer(target).ifPresentOrElse(index -> {
            index.sendRichMessage("<red>You got mail! %s is trying to reach you about your car's extended warranty".formatted(player.getUsername()));
        }, () -> {
            player.sendRichMessage("<red>%s not found.".formatted(target));
        });
    }

    @Leaf(value = "boolean", desc = "Tests a boolean argument.", aliases = "not-boolean")
    public void booleanTest(Player player, @Suggestion(name = "boolean", type = Boolean.class, values = {"true", "false"}) boolean isReady) {
        if (isReady) {
            player.sendRichMessage("<red>This is ready!");

            return;
        }

        player.sendRichMessage("<red>This is not ready!");
    }
}