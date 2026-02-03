package de.daver.unigate.command.statue;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.statue.Statue;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

public class CloneSubCommand extends LiteralNode {

    protected CloneSubCommand() {
        super("clone", "Clones the selected statue");
        permission(Permissions.STATUE_CLONE);
        executor(this::cloneStatue);
    }

    void cloneStatue(PluginContext context) throws CommandSyntaxException {
        var player = context.senderPlayer();
        var statue = context.plugin().statueInteractListener().get(player);
        if(statue == null) {
            context.plugin().languageManager().message().key(LanguageKeys.STATUE_NOT_SELECTED)
                    .build().send(player);
            return;
        }

        copyStatue(statue, player.getLocation());

        context.plugin().languageManager().message().key(LanguageKeys.STATUE_CLONED)
                .build().send(player);
    }

    private void copyStatue(Statue statue, Location location) {
        var newStand = location.getWorld().spawn(location, ArmorStand.class);
        var newStatue = new Statue(newStand);
        statue.copyAttributes(newStatue);
    }
}
