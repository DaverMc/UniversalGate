package de.daver.unigate.command.dimension;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.command.argument.DimensionArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.ConfirmArgument;
import de.daver.unigate.dimension.Dimension;

public class DeleteSubCommand extends LiteralNode {

    public DeleteSubCommand() {
        super("delete", "Deletes a Dimension");
        permission(Permissions.DIMENSION_DELETE);
        then(new DimensionArgument("dimension"))
                .executor(this::sendConfirmMessage)
                .then(new ConfirmArgument())
                .executor(this::deleteDimension);
    }


    private void sendConfirmMessage(PluginContext context) {
        context.plugin().languageManager()
                .message(LanguageKeys.DIMENSION_DELETE_CONFIRM)
                .argument("dimension", context.getArgument("dimension", Dimension.class).name())
                .send(context.sender());
    }

    private void deleteDimension(PluginContext context) throws Exception {
        Dimension dimension = context.getArgument("dimension", Dimension.class);
        dimension.delete();
        UniversalGatePlugin.getInstance().dimensionCache().delete(dimension);
        context.plugin().languageManager()
                .message(LanguageKeys.DIMENSION_DELETE_SUCCESS)
                .argument("dimension", dimension.name())
                .send(context.sender());
    }
}
