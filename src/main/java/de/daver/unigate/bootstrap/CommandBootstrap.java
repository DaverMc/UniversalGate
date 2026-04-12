package de.daver.unigate.bootstrap;

import de.daver.unigate.UniGateAPI;
import de.daver.unigate.command.category.CategoryCommand;
import de.daver.unigate.command.dimension.DimensionCommand;
import de.daver.unigate.command.icon.IconCommand;
import de.daver.unigate.command.lang.LanguageCommand;
import de.daver.unigate.command.statue.StatueCommand;
import de.daver.unigate.command.task.TaskCommand;
import de.daver.unigate.command.util.*;
import de.daver.unigate.core.command.LiteralNode;
import io.papermc.paper.command.brigadier.Commands;

import java.util.HashSet;
import java.util.Set;

public class CommandBootstrap {

    private final Set<LiteralNode> commands;

    public CommandBootstrap() {
        this.commands = new HashSet<>();
    }

    public CommandBootstrap add(LiteralNode command) {
        this.commands.add(command);
        return this;
    }

    public void registerAll(Commands registry) {
        commands.forEach(command -> command.register(registry));
    }

    public static CommandBootstrap create() {
        return new CommandBootstrap()
                .add(new DimensionCommand())
                .add(new CategoryCommand())
                .add(new LanguageCommand())
                .add(new SpeedCommand())
                .add(new CreativeItemsCommand())
                .add(new DebugStickCommand())
                .add(new NightVisionCommand())
                .add(new TaskCommand())
                .add(new HubCommand())
                .add(new StatueCommand())
                .add(new IconCommand())
                .add(new BuildModeCommand())
                .add(new GamemodeCommand());
    }
}
