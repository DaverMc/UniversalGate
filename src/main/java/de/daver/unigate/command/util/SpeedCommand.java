package de.daver.unigate.command.util;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.NumberArgument;

public class SpeedCommand extends LiteralNode {

    public SpeedCommand() {
        super("speed", "Sets the speed of the player from 1-10");
        permission(Permissions.COMMAND_SPEED);
        executor(this::resetSpeed);
        then(new NumberArgument<>("speed", Integer.class,1, 10))
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

        float speedF =  (float) speed / 10.0f;

        if(player.isFlying()) player.setFlySpeed(speedF);
        else player.setWalkSpeed(speedF);

        context.plugin().languageManager()
                .message(LanguageKeys.SPEED_SET)
                .argument("speed", speed)
                .send(context.sender());
    }
}
