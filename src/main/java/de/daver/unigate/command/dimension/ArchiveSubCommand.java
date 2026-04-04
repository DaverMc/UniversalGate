package de.daver.unigate.command.dimension;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.command.argument.DimensionArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.dimension.Dimension;

public class ArchiveSubCommand extends LiteralNode {

    protected ArchiveSubCommand() {
        super("archive", "Archives a dimension so it can't be edited anymore");
        permission(Permissions.DIMENSION_ARCHIVE);
        then(new DimensionArgument("dimension"))
                .executor(this::archiveDimension);
    }

    void archiveDimension(PluginContext context) throws Exception {
        var dimension = context.getArgument("dimension", Dimension.class);

        context.plugin().dimensionCache().archive(dimension);
        context.plugin().languageManager()
                .message(LanguageKeys.DIMENSION_ARCHIVE)
                .argument("dimension", dimension.name())
                .send(context.sender());
    }
}
