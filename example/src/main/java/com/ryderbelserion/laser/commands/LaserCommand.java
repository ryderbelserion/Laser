package com.ryderbelserion.laser.commands;

import com.ryderbelserion.laser.commands.types.SubCommand;
import com.ryderbelserion.laser.core.api.AbstractCommand;
import com.ryderbelserion.laser.core.api.annotations.Flower;
import com.ryderbelserion.laser.core.api.annotations.Tree;
import com.ryderbelserion.laser.core.api.annotations.other.Permission;
import com.ryderbelserion.laser.core.api.annotations.other.Suggestion;
import com.ryderbelserion.laser.core.api.annotations.types.Leaf;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@Tree(value = "laser", desc = "The base command for Laser!")
@Permission(permission = "laser.use")
public class LaserCommand extends AbstractCommand<CommandSourceStack, CommandSender> {

    public LaserCommand() {
        addBranch(new SubCommand());
    }

    @Flower
    @Permission(permission = "laser.access")
    public void execute(ConsoleCommandSender sender) {
        sender.sendRichMessage("<yellow>This is the /laser command!");
    }

    @Leaf(value = "give", desc = "The give command")
    @Permission(permission = "laser.give")
    public void give(
            Player player
            //@Suggestion(name = "amount", type = int.class) int amount,
            //@Suggestion(name = "item", type = int.class) int item
            //@Suggestion(name = "bank", type = int.class) int bank
    ) {
        player.sendRichMessage("<red>This is the /laser give command.");

        //if (amount > 0) {
        //    player.sendRichMessage("<red>Amount: %s".formatted(amount));
        //}

        //if (item > 0) {
        //    player.sendRichMessage("<green>%s is greater than 0!".formatted(item));
        //}

        //if (bank > 0) {
        //    player.sendRichMessage("<yellow>%s is greater than 0!".formatted(bank));
        //}
    }
}