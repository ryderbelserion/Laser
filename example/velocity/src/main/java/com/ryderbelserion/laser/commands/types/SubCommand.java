package com.ryderbelserion.laser.commands.types;

import com.ryderbelserion.laser.core.api.annotations.Flower;
import com.ryderbelserion.laser.core.api.annotations.other.Permission;
import com.ryderbelserion.laser.core.api.annotations.types.Branch;
import com.ryderbelserion.laser.core.api.annotations.types.Leaf;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;

@Branch(value = "sub")
@Permission(permission = "laser.sub")
public class SubCommand {

    @Flower
    public void flower(CommandSource sender) {
        sender.sendRichMessage("<yellow>This is the default sub command.");
    }

    @Leaf(value = "help", desc = "The help command", aliases = "support")
    @Permission(permission = {"laser.help", "laser.test"})
    public void help(ConsoleCommandSource sender) {
        sender.sendRichMessage("<green>This is the help command.");
    }

    @Leaf(value = "balance", desc = "The balance command", aliases = "vault")
    @Permission(permission = "laser.balance")
    public void balance(ConsoleCommandSource sender) {
        sender.sendRichMessage("<green>This is the balance command.");
    }
}