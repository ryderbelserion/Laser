package com.ryderbelserion.laser.core.meta.types;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.ryderbelserion.laser.core.api.annotations.other.Suggestion;
import com.ryderbelserion.laser.core.api.annotations.other.args.GreedyString;
import org.jspecify.annotations.NonNull;
import java.lang.reflect.Parameter;

public class ArgumentMeta<CS> {

    private final RequiredArgumentBuilder<CS, ?> argument;
    private final ArgumentType<?> argumentType;
    private final String argumentName;
    private final boolean isGreedy;

    public ArgumentMeta(final Parameter parameter) {
        final Suggestion suggestion = parameter.getAnnotation(Suggestion.class);

        this.argumentType = mapArgument(suggestion.type());
        this.argumentName = suggestion.name();
        this.argument = RequiredArgumentBuilder.argument(
                this.argumentName,
                this.argumentType
        );

        this.isGreedy = parameter.isAnnotationPresent(GreedyString.class);
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

    private @NonNull ArgumentType<?> mapArgument(@NonNull final Class<?> klass) {
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
            argumentType = isGreedy ? StringArgumentType.greedyString() : StringArgumentType.string();
        }

        return argumentType;
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