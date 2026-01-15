package de.daver.unigate.statue.itemlistener;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.item.ItemActionListener;
import de.daver.unigate.statue.Statue;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;

import java.util.List;

public class SettingsItemListener implements ItemActionListener {

    public static final String ID = "statue_settings";

    @Override
    public void onClick(Context context) {
        var player = context.player();
        var statue = context.plugin().statueInteractListener().get(player);
        if(statue == null) return;
        var dialog = createDialog(context.plugin(), player, statue);
        player.showDialog(dialog);
    }

    private Dialog createDialog(UniversalGatePlugin plugin, Player player, Statue statue) {
        var lang = plugin.languageManager();

        var dialogTitle = lang.message().key(LanguageKeys.DIALOG_STATUE_SETTINGS_TITLE).build().get(player);
        var displayNameTitle = lang.message().key(LanguageKeys.DIALOG_STATUE_SETTINGS_DISPLAY_NAME).build().get(player);
        var smallTitle = lang.message().key(LanguageKeys.DIALOG_STATUE_SETTINGS_SMALL).build().get(player);
        var basePlateTitle = lang.message().key(LanguageKeys.DIALOG_STATUE_SETTINGS_BASE).build().get(player);
        var visibleTitle = lang.message().key(LanguageKeys.DIALOG_STATUE_SETTINGS_VISIBLE).build().get(player);
        var gravitiyTitle = lang.message().key(LanguageKeys.DIALOG_STATUE_SETTINGS_GRAVITY).build().get(player);
        var armsTitle = lang.message().key(LanguageKeys.DIALOG_STATUE_SETTINGS_ARMS).build().get(player);
        var glowingTitle = lang.message().key(LanguageKeys.DIALOG_STATUE_SETTINGS_GLOWING).build().get(player);
        var nameVisibleTitle = lang.message().key(LanguageKeys.DIALOG_STATUE_SETTINGS_NAME_VISIBLE).build().get(player);
        var deleteTitle = lang.message().key(LanguageKeys.DIALOG_STATUE_SETTINGS_DELETE).build().get(player);
        var confirmTitle = lang.message().key(LanguageKeys.DIALOG_STATUE_SETTINGS_CONFIRM).build().get(player);;
        var confirmHover = lang.message().key(LanguageKeys.DIALOG_STATUE_SETTINGS_CONFIRM_HOVER).build().get(player);
        var discardTitle = lang.message().key(LanguageKeys.DIALOG_STATUE_SETTINGS_DISCARD).build().get(player);;
        var discardHover = lang.message().key(LanguageKeys.DIALOG_STATUE_SETTINGS_DISCARD_HOVER).build().get(player);

        return Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(dialogTitle)
                        .inputs(List.of(
                                DialogInput.text("display_name", displayNameTitle)
                                        .initial(statue.displayName())
                                        .width(200)
                                        .maxLength(1000)
                                        .build(),
                                DialogInput.bool("small", smallTitle)
                                        .initial(statue.small()).build(),
                                DialogInput.bool("base", basePlateTitle)
                                        .initial(statue.basePlate()).build(),
                                DialogInput.bool("visible", visibleTitle)
                                        .initial(statue.visible()).build(),
                                DialogInput.bool("gravity", gravitiyTitle)
                                        .initial(statue.gravity()).build(),
                                DialogInput.bool("arms", armsTitle)
                                        .initial(statue.arms()).build(),
                                DialogInput.bool("glowing", glowingTitle)
                                        .initial(statue.glowing()).build(),
                                DialogInput.bool("name_visible", nameVisibleTitle)
                                        .initial(statue.nameVisible()).build(),
                                DialogInput.bool("delete", deleteTitle)
                                        .initial(false)
                                        .build()
                                ))
                        .build()
                )
                .type(DialogType.confirmation(
                        ActionButton.create(
                                confirmTitle,
                                confirmHover,
                                100,
                                DialogAction.customClick(Key.key("unigate:statue_settings_confirmed"), null)
                        ),
                        ActionButton.create(
                                discardTitle,
                                discardHover,
                                100,
                                null
                        )
                ))
        );
    }

}
