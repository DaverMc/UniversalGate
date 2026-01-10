package de.daver.unigate.command.impl.util;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.command.LiteralNode;
import de.daver.unigate.command.PluginContext;
import de.daver.unigate.command.argument.NumberArgument;

public class SpeedCommand extends LiteralNode {

    public SpeedCommand() {
        super("speed");
        executor(this::resetSpeed);
        then(new NumberArgument<>("speed", IntegerArgumentType.integer(1, 10), Integer.class))
                .executor(this::setSpeed);
    }

    private void resetSpeed(PluginContext context) throws CommandSyntaxException {
        setSpeed(1, context);
    }

    private void setSpeed(PluginContext context) throws CommandSyntaxException {
        var speed = context.getArgument("speed", Integer.class);
        setSpeed(speed, context);
    }

    private void setSpeed(int speed, PluginContext context) throws CommandSyntaxException {
        var player = context.senderPlayer();

        float speedF = 10.0f / speed;

        if(player.isFlying()) player.setFlySpeed(speedF);
        else player.setWalkSpeed(speedF);

        context.plugin().languageManager().message()
                .key(LanguageKeys.SPEED_SET)
                .parsed("speed", speed)
                .build().send(context.sender());
    }
}
