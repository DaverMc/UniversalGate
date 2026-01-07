package de.daver.unigate.command.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.command.CommandExceptions;
import de.daver.unigate.dimension.category.Category;
import de.daver.unigate.dimension.category.CategoryCache;

public record CategoryArgumentType() implements WordArgumentType<Category> {

    @Override
    public Category convert(String nativeType) throws CommandSyntaxException {
        if (!CategoryCache.exists(nativeType)) throw CommandExceptions.VALUE_NOT_EXISTING
                .create(nativeType);
        return CategoryCache.get(nativeType);
    }
}
