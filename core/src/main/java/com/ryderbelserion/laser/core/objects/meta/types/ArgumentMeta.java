package com.ryderbelserion.laser.core.objects.meta.types;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ryderbelserion.laser.core.CommandManager;
import com.ryderbelserion.laser.core.ResolverManager;
import com.ryderbelserion.laser.core.api.annotations.other.Suggestion;
import com.ryderbelserion.laser.core.api.annotations.other.args.GreedyString;
import com.ryderbelserion.laser.core.objects.arguments.ArgumentResolver;
import com.ryderbelserion.laser.core.objects.suggestions.SuggestionKey;
import com.ryderbelserion.laser.core.objects.suggestions.SuggestionSource;
import net.kyori.adventure.audience.Audience;
import org.jspecify.annotations.NonNull;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Locale;

public class ArgumentMeta<CS, S extends Audience> {

    private final RequiredArgumentBuilder<CS, ?> argument;
    private final ArgumentResolver<CS, ?> resolver;
    private final ArgumentType<?> argumentType;
    private final SuggestionKey suggestionKey;
    private final String argumentName;
    private final boolean isGreedy;
    private final Class<?> type;

    public ArgumentMeta(final CommandManager<CS, S> commandManager, final ResolverManager<CS> resolvers, final Parameter parameter) {
        final Suggestion suggestion = parameter.getAnnotation(Suggestion.class);

        this.suggestionKey = SuggestionKey.of(this.argumentName = suggestion.name());

        this.type = suggestion.type();

        this.resolver = resolvers.get(this.type).orElse(null); //todo() combine this with the commandManager#mapArgumentType by returning a custom object.

        this.argument = RequiredArgumentBuilder.argument(this.argumentName, this.argumentType = this.resolver == null ? commandManager.mapArgumentType(this) : this.resolver.argumentType());

        this.isGreedy = parameter.isAnnotationPresent(GreedyString.class);

        this.argument.suggests((context, builder) -> {
            final String values = builder.getRemaining();
            final SuggestionSource<CS> source = new SuggestionSource<>(context.getSource(), values);
            final String lowercase = values.toLowerCase(Locale.ROOT);

            for (final Object value : values(resolvers, source)) {
                if (value instanceof Integer number) {
                    builder.suggest(number);

                    continue;
                }

                final String text = String.valueOf(value);

                if (text.toLowerCase(Locale.ROOT).startsWith(lowercase)) {
                    builder.suggest(text);
                }
            }

            return builder.buildFuture();
        });
    }

    private List<?> values(@NonNull final ResolverManager<CS> manager, @NonNull final SuggestionSource<CS> source) {
        if (this.suggestionKey != null && manager.has(this.suggestionKey)) {
            return manager.resolve(this.suggestionKey, source);
        }

        if (this.resolver == null) {
            return List.of();
        }

        return this.resolver.suggest(source);
    }

    public Object extract(@NonNull final CommandContext<CS> context) throws CommandSyntaxException {
        if (this.resolver == null) {
            return context.getArgument(this.argumentName, mapPrimitive(this.argumentType));
        }

        return this.resolver.resolve(this.argumentName, context);
    }

    public final void then(@NonNull final RequiredArgumentBuilder<CS, ?> argument) {
        this.argument.then(argument);
    }

    public @NonNull final RequiredArgumentBuilder<CS, ?> getArgument() {
        return this.argument;
    }

    public @NonNull final ArgumentType<?> getArgumentType() {
        return this.argumentType;
    }

    public @NonNull final String getArgumentName() {
        return this.argumentName;
    }

    public @NonNull final Class<?> getParsedType() {
        return this.type;
    }

    public final boolean isGreedy() {
        return this.isGreedy;
    }

    public @NonNull Class<?> mapPrimitive(@NonNull final ArgumentType<?> argumentType) {
        final Class<?> klass = argumentType.getClass();

        Class<?> index = void.class;

        if (klass == IntegerArgumentType.class) {
            index = int.class;
        } else if (klass == DoubleArgumentType.class) {
            index = double.class;
        } else if (klass == BoolArgumentType.class) {
            index = boolean.class;
        } else if (klass == LongArgumentType.class) {
            index = long.class;
        } else if (klass == StringArgumentType.class) {
            index = String.class;
        }

        return index;
    }
}