package com.ryderbelserion.laser.velocity.types.parser;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ryderbelserion.laser.velocity.types.PlayerArgument;

public class PlayerArgumentParser implements ArgumentType<PlayerArgument> {

    @Override
    public PlayerArgument parse(final StringReader reader) throws CommandSyntaxException {
        return new PlayerArgument(reader.readQuotedString());
    }
}