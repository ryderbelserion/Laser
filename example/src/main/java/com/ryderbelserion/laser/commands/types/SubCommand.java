package com.ryderbelserion.laser.commands.types;

import com.ryderbelserion.laser.core.api.annotations.Flower;
import com.ryderbelserion.laser.core.api.annotations.other.Permission;
import com.ryderbelserion.laser.core.api.annotations.types.Branch;
import com.ryderbelserion.laser.core.api.annotations.types.Leaf;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

@Branch(value = "sub")
@Permission(permission = "fusion.sub")
public class SubCommand {

    @Flower
    public void flower(CommandSender sender) {
        sender.sendRichMessage("<yellow>This is the default sub command.");
    }

    @Leaf(value = "help", desc = "The help command")
    @Permission(permission = "fusion.help")
    public void help(ConsoleCommandSender sender) {
        sender.sendRichMessage("<green>This is the help command.");
    }

    @Leaf(value = "balance", desc = "The balance command")
    @Permission(permission = "fusion.balance")
    public void balance(ConsoleCommandSender sender) {
        sender.sendRichMessage("<green>This is the balance command.");
    }
}