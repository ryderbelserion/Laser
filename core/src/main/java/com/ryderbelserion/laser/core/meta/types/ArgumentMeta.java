package com.ryderbelserion.laser.core.meta.types;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.ryderbelserion.laser.core.api.annotations.other.Suggestion;
import org.jspecify.annotations.NonNull;
import java.lang.reflect.Parameter;
import java.util.Optional;

public class ArgumentMeta<CS> {

    private RequiredArgumentBuilder<CS, ?> argument;

    private final ArgumentType<?> argumentType;
    private final String argumentName;

    public ArgumentMeta(final Parameter parameter) {
        final Suggestion suggestion = parameter.getAnnotation(Suggestion.class);

        this.argumentType = mapArgument(suggestion.type());
        this.argumentName = suggestion.name();
    }

    public final void then(@NonNull final RequiredArgumentBuilder<CS, ?> argument) {
        if (this.argument == null) {
            this.argument = argument;

            return;
        }

        this.argument.then(argument);
    }

    public @NonNull final Optional<RequiredArgumentBuilder<CS, ?>> getArgument() {
        return Optional.ofNullable(this.argument);
    }

    public @NonNull final ArgumentType<?> getArgumentType() {
        return this.argumentType;
    }

    public @NonNull final String getArgumentName() {
        return this.argumentName;
    }

    private @NonNull ArgumentType<?> mapArgument(@NonNull final Class<?> klass) {
        final String type = klass.getSimpleName();

        ArgumentType<?> argumentType = StringArgumentType.string();

        switch (type) {
            case "boolean" -> argumentType = BoolArgumentType.bool();
            case "double" -> argumentType = DoubleArgumentType.doubleArg();
            case "int" -> argumentType = IntegerArgumentType.integer();
        }

        return argumentType;
    }
}