package com.ryderbelserion.laser.resolvers.requirements;

import com.ryderbelserion.laser.core.objects.requirements.RequirementResolver;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemType;
import org.jspecify.annotations.NullMarked;
import java.util.function.Predicate;

@NullMarked
public class ItemRequirement implements RequirementResolver<CommandSourceStack> {

    @Override
    public Predicate<CommandSourceStack> get() {
        return source -> {
            final CommandSender sender = source.getSender();

            if (!(sender instanceof Player player)) {
                return true;
            }

            return player.getInventory().contains(ItemType.ITEM_FRAME.createItemStack(3));
        };
    }
}