package de.daver.unigate.command.dimension;

import de.daver.unigate.UniGateAPI;
import de.daver.unigate.command.UniGateCommand;

public class DimensionCommand extends UniGateCommand {

    public DimensionCommand(UniGateAPI api) {
        super(api, "dimension", "Access the Dimension system", "world", "welt", "w", "dim");
        then(new CreateSubCommand());
        then(new DeleteSubCommand());
        then(new ListSubCommand());
        then(new InfoSubCommand());
        then(new TeleportSubCommand());
        then(new AllowedSubCommand());
        then(new ExportSubCommand());
        then(new ImportSubCommand());
        then(new InviteSubCommand());
        then(new KickSubCommand());
        then(new StopLagSubCommand());
        then(new ArchiveSubCommand());
        then(new ActivateSubCommand());
        then(new RenameSubCommand());
        then(new AcceptInviteSubCommand());
        then(new GenerateChunksSubCommand());
    }
}