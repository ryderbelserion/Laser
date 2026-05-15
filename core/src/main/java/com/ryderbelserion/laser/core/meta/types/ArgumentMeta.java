package com.ryderbelserion.laser.core.meta.types;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import org.jspecify.annotations.NonNull;
import java.util.Optional;

public class ArgumentMeta<CS> {

    private RequiredArgumentBuilder<CS, ?> argument;

    public final void then(@NonNull final RequiredArgumentBuilder<CS, ?> argument) {
        if (this.argument == null) {
            this.argument = argument;

            return;
        }

        this.argument.then(argument);
    }

    public @NonNull final ArgumentType<?> mapArgument(@NonNull final Class<?> klass) {
        final String type = klass.getSimpleName();

        ArgumentType<?> argumentType = StringArgumentType.string();

        switch (type) {
            case "boolean" -> argumentType = BoolArgumentType.bool();
            case "double" -> argumentType = DoubleArgumentType.doubleArg();
            case "int" -> argumentType = IntegerArgumentType.integer();
        }

        return argumentType;
    }

    public @NonNull final Optional<RequiredArgumentBuilder<CS, ?>> getArgument() {
        return Optional.ofNullable(this.argument);
    }
}