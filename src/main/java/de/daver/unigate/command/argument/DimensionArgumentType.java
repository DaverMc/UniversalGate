package de.daver.unigate.command.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.command.CommandExceptions;
import de.daver.unigate.dimension.Dimension;
import de.daver.unigate.dimension.DimensionCache;

import java.sql.SQLException;

public record DimensionArgumentType() implements WordArgumentType<Dimension> {

    @Override
    public Dimension convert(String nativeType) throws CommandSyntaxException {
        try {
            return DimensionCache.get(nativeType);
        } catch (SQLException exception) {
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        }
    }
}
