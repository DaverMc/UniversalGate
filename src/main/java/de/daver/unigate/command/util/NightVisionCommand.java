package de.daver.unigate.command.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.lang.LanguageManager;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NightVisionCommand extends LiteralNode {

    public NightVisionCommand() {
        super("nightvision", "Toggles Night Vision", "nv", "vision", "gamma");
        permission(Permissions.COMMAND_NIGHT_VISION);
        executor(this::toggleVision);
    }

    void toggleVision(PluginContext context) throws CommandSyntaxException {
        var player = context.senderPlayer();
        if(player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            removeNightVision(player, context.plugin().languageManager());
        } else {
            addNightVision(player, context.plugin().languageManager());
        }
    }

    private void addNightVision(Player player, LanguageManager languageManager) {
        var effect = new PotionEffect(PotionEffectType.NIGHT_VISION, PotionEffect.INFINITE_DURATION, 0, false, false);
        player.addPotionEffect(effect);
        languageManager.message().key(LanguageKeys.NIGHTVISION_ADD)
                .build().send(player);
    }

    private void removeNightVision(Player player, LanguageManager languageManager) {
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        languageManager.message().key(LanguageKeys.NIGHTVISION_REMOVED)
                .build().send(player);
    }
}
