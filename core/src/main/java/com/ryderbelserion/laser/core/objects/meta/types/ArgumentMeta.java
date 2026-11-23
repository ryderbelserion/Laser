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
import com.ryderbelserion.laser.core.CommandRegistry;
import com.ryderbelserion.laser.core.ContextRegistry;
import com.ryderbelserion.laser.core.api.annotations.other.Requirement;
import com.ryderbelserion.laser.core.api.annotations.other.Suggestion;
import com.ryderbelserion.laser.core.api.annotations.other.args.GreedyString;
import com.ryderbelserion.laser.core.objects.arguments.ArgumentResolver;
import com.ryderbelserion.laser.core.objects.keys.LaserKey;
import com.ryderbelserion.laser.core.objects.requirements.RequirementResolver;
import com.ryderbelserion.laser.core.objects.suggestions.SuggestionSource;
import net.kyori.adventure.audience.Audience;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Locale;

@NullMarked
public class ArgumentMeta<CS, S extends Audience> {

    //private final List<RequirementResolver<CS>> requirements = new ArrayList<>();
    private @Nullable final ArgumentResolver<CS, ?> resolver;
    private final RequiredArgumentBuilder<CS, ?> argument;
    private final ArgumentType<?> argumentType;
    private @Nullable final LaserKey suggestionKey;
    private final String argumentName;
    private final boolean isGreedy;
    private final Class<?> type;

    public ArgumentMeta(final CommandRegistry<CS, S> commandRegistry, final ContextRegistry<CS> contextRegistry, final Parameter parameter) {
        if (parameter.isAnnotationPresent(Suggestion.class)) {
            final Suggestion suggestion = parameter.getAnnotation(Suggestion.class);

            this.suggestionKey = LaserKey.of(this.argumentName = suggestion.name());

            this.type = suggestion.type();
        } else {
            this.suggestionKey = null;
            this.argumentName = parameter.getName();
            this.type = parameter.getType();
        }

        this.resolver = contextRegistry.get(this.type).orElse(null); //todo() combine this with the commandManager#mapArgumentType by returning a custom object.

        /*if (parameter.isAnnotationPresent(Requirement.class)) {
            final Requirement requirement = parameter.getAnnotation(Requirement.class);

            final String[] ids = requirement.values();

            for (final String id : ids) {
                final Optional<RequirementResolver<CS>> optional = contextRegistry.get(LaserKey.of(id));

                if (optional.isEmpty()) {
                    continue;
                }

                this.requirements.add(optional.get());
            }
        }*/

        this.argument = RequiredArgumentBuilder.argument(this.argumentName, this.argumentType = this.resolver == null ? commandRegistry.mapArgumentType(this) : this.resolver.argumentType());

        this.isGreedy = parameter.isAnnotationPresent(GreedyString.class);

        this.argument.suggests((context, builder) -> {
            final String values = builder.getRemaining();
            final SuggestionSource<CS> source = new SuggestionSource<>(context.getSource(), values);
            final String lowercase = values.toLowerCase(Locale.ROOT);

            for (final Object value : values(contextRegistry, source)) {
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

    private List<?> values(final ContextRegistry<CS> manager, final SuggestionSource<CS> source) {
        if (this.suggestionKey != null && manager.has(this.suggestionKey)) {
            return manager.resolve(this.suggestionKey, source);
        }

        if (this.resolver == null) {
            return List.of();
        }

        return this.resolver.suggest(source);
    }

    public final Object extract(final CommandContext<CS> context) throws CommandSyntaxException {
        if (this.resolver == null) {
            return context.getArgument(this.argumentName, mapPrimitive(this.argumentType));
        }

        return this.resolver.resolve(this.argumentName, context);
    }

    public final void then(final RequiredArgumentBuilder<CS, ?> argument) {
        this.argument.then(argument);
    }

    public final RequiredArgumentBuilder<CS, ?> getArgument() {
        return this.argument;
    }

    public final ArgumentType<?> getArgumentType() {
        return this.argumentType;
    }

    public final String getArgumentName() {
        return this.argumentName;
    }

    public final Class<?> getParsedType() {
        return this.type;
    }

    public final boolean isGreedy() {
        return this.isGreedy;
    }

    public final Class<?> mapPrimitive(final ArgumentType<?> argumentType) {
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
        } else if (klass == StringArgumentType.class || this.isGreedy) {
            index = String.class;
        }

        return index;
    }
}