package de.daver.unigate.command.dimension;

import de.daver.unigate.Permissions;
import de.daver.unigate.command.argument.DimensionArgument;
import de.daver.unigate.core.command.LiteralNode;

public class AllowedSubCommand extends LiteralNode {

    protected AllowedSubCommand() {
        super("allowed", "Access the allowed list of a dimension");
        permission(Permissions.DIMENSION_ALLOWED);
        var dimensionArg = new DimensionArgument("dimension");
        then(dimensionArg)
                .then(new AllowedAddSubCommand())
                .then(new AllowedRemoveSubCommand())
                .then(new AllowedListSubCommand());
    }
}
