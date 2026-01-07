package de.daver.unigate.command.impl.dimension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.command.ArgumentNode;
import de.daver.unigate.command.CommandExceptions;
import de.daver.unigate.command.ContextWrapper;
import de.daver.unigate.command.LiteralNode;
import de.daver.unigate.command.argument.CategoryArgumentType;
import de.daver.unigate.command.argument.EnumArgumentType;
import de.daver.unigate.command.argument.WordArgumentType;
import de.daver.unigate.dimension.Dimension;
import de.daver.unigate.dimension.DimensionType;
import de.daver.unigate.dimension.category.Category;

import java.io.IOException;
import java.sql.SQLException;

class CreateSubCommand extends LiteralNode {

    public CreateSubCommand() {
        super("create");
        then(new ArgumentNode<>("category", new CategoryArgumentType()))
                .then(new ArgumentNode<>("theme", WordArgumentType.string()))
                .runsCommand(this::createDimension)
                .then(new ArgumentNode<>("type", new EnumArgumentType<>(DimensionType.class)))
                .runsCommand(this::createCustomDimension);
    }

    private void createDimension(ContextWrapper context) throws CommandSyntaxException {
        Category category = context.getArgument("category", Category.class);
        String theme = context.getArgument("theme", String.class);
        createDimension(category, theme, DimensionType.VOID, context);
    }

    private void createCustomDimension(ContextWrapper context) throws CommandSyntaxException {
        Category category = context.getArgument("category", Category.class);
        String theme = context.getArgument("theme", String.class);
        DimensionType type = context.getArgument("type", DimensionType.class);
        createDimension(category, theme, type, context);
    }

    private void createDimension(Category category, String theme, DimensionType type, ContextWrapper context) throws CommandSyntaxException {
        try {
            Dimension dimension = Dimension.create(category, theme, type, context.executor().getUniqueId());
            context.sender().sendMessage("Created dimension " + dimension.id());
        } catch (SQLException e) {
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        } catch (IOException e) {
            throw CommandExceptions.FILE_EXCEPTION.create();
        }

    }

}
