package com.ryderbelserion.laser.core.objects.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ryderbelserion.laser.core.objects.suggestions.SuggestionSource;
import org.jspecify.annotations.NullMarked;
import java.util.List;

@NullMarked
public interface ArgumentResolver<CS, T> {

    ArgumentType<?> argumentType();

    Class<T> type();

    T resolve(final String argumentName, final CommandContext<CS> context) throws CommandSyntaxException;

    default List<?> suggest(SuggestionSource<CS> source) {
        return List.of();
    }
}