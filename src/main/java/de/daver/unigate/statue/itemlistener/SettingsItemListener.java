package de.daver.unigate.statue.itemlistener;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.core.lang.LanguageManager;
import de.daver.unigate.item.ItemActionListener;
import de.daver.unigate.statue.Statue;
import de.daver.unigate.statue.StatueAttributes;
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
        var glowingTitle = lang.message().key(LanguageKeys.DIALOG_STATUE_SETTINGS_GLOWING).build().get(player);
        var deleteTitle = lang.message().key(LanguageKeys.DIALOG_STATUE_SETTINGS_DELETE).build().get(player);
        var confirmTitle = lang.message().key(LanguageKeys.DIALOG_STATUE_SETTINGS_CONFIRM).build().get(player);;
        var confirmHover = lang.message().key(LanguageKeys.DIALOG_STATUE_SETTINGS_CONFIRM_HOVER).build().get(player);
        var discardTitle = lang.message().key(LanguageKeys.DIALOG_STATUE_SETTINGS_DISCARD).build().get(player);;
        var discardHover = lang.message().key(LanguageKeys.DIALOG_STATUE_SETTINGS_DISCARD_HOVER).build().get(player);
        var attributes = statue.attributes();
        return Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(dialogTitle)
                        .inputs(List.of(
                                DialogInput.text("display_name", displayNameTitle)
                                        .initial(statue.displayName())
                                        .width(200)
                                        .maxLength(1000)
                                        .build(),
                                smallCheckBox(lang, player, attributes),
                                basePlateCheckBox(),

                                DialogInput.bool("glowing", glowingTitle)
                                        .initial(attributes.isGlowing()).build(),
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

    private DialogInput smallCheckBox(LanguageManager lang, Player player, StatueAttributes attributes) {
        var smallTitle = lang.message().key(LanguageKeys.DIALOG_STATUE_SETTINGS_SMALL).build().get(player);
        return DialogInput.bool("small", smallTitle)
                .initial(attributes.isSmall()).build();
    }

    private DialogInput basePlateCheckBox() {
        var basePlateTitle = lang.message().key(LanguageKeys.DIALOG_STATUE_SETTINGS_BASE).build().get(player);
        return DialogInput.bool("base", basePlateTitle)
                .initial(attributes.hasBasePlate()).build();
    }

    public DialogInput visibilityCheckBox() {
        var visibleTitle = lang.message().key(LanguageKeys.DIALOG_STATUE_SETTINGS_VISIBLE).build().get(player);
        return DialogInput.bool("visible", visibleTitle)
                .initial(attributes.isVisible()).build();
    }

    public DialogInput armsCheckBox() {
        var armsTitle = lang.message().key(LanguageKeys.DIALOG_STATUE_SETTINGS_ARMS).build().get(player);
        return DialogInput.bool("arms", armsTitle)
                        .initial(attributes.hasArms()).build();
    }

    public DialogInput glowingCheckBox() {

    }

}
