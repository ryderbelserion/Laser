package com.ryderbelserion.laser.resolvers.requirements;

import com.ryderbelserion.laser.core.objects.requirements.RequirementResolver;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import org.jspecify.annotations.NullMarked;
import java.util.function.Predicate;

@NullMarked
public class ExtraPermissionRequirement implements RequirementResolver<CommandSource> {

    @Override
    public Predicate<CommandSource> get() {
        return source -> {
            if (!(source instanceof Player player)) {
                return true;
            }

            return player.hasPermission("laser.test");
        };
    }
}