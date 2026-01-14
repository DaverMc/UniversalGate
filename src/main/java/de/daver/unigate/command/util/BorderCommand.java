package de.daver.unigate.command.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.lang.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BorderCommand extends LiteralNode {

    public BorderCommand() {
        super("border");
        executor(this::toggleBorder);
    }

    void toggleBorder(PluginContext context) throws CommandSyntaxException {
        var player = context.senderPlayer();
        var border = player.getWorldBorder();
        if(border == null) setUnlimitedBorder(player, context.plugin().languageManager());
        else setDefaultBorder(player, context.plugin().languageManager());
    }

    private void setDefaultBorder(Player player, LanguageManager languageManager) {
        player.setWorldBorder(null);
        languageManager.message()
                .key(LanguageKeys.BORDER_DEFAULT)
                .build().send(player);
    }

    private void setUnlimitedBorder(Player player, LanguageManager languageManager) {
        var giantBorder = Bukkit.createWorldBorder();
        giantBorder.setCenter(0, 0);
        giantBorder.setSize(5.9E7);
        player.setWorldBorder(giantBorder);
        languageManager.message()
                .key(LanguageKeys.BORDER_UNLIMITED)
                .build().send(player);
    }
}
