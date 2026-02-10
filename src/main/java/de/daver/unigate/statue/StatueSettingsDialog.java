package de.daver.unigate.statue;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.core.lang.LanguageManager;
import de.daver.unigate.core.lang.neu.LanguagesCache;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;

import java.util.List;

public class StatueSettingsDialog {

    public static Dialog create(UniversalGatePlugin plugin, Player player, Statue statue) {
        var lang = plugin.languageManager();
        var attributes = statue.attributes();
        var dialogBase = dialogBase(lang, player, attributes);
        var dialogType = DialogType.multiAction(List.of(
                confirmChangesButton(lang, player),
                deleteStatueButton(lang, player)))
                .build();

        return Dialog.create(builder -> builder.empty()
                .base(dialogBase)
                .type(dialogType));
    }

    private static DialogBase dialogBase(LanguagesCache lang, Player player, StatueAttributes attributes) {
        var dialogTitle = lang.message(LanguageKeys.DIALOG_STATUE_SETTINGS_TITLE).get(player);
        var inputs = List.of(
                displayNameTextField(lang, player, attributes),
                smallCheckBox(lang, player, attributes),
                basePlateCheckBox(lang, player, attributes),
                visibilityCheckBox(lang, player, attributes),
                armsCheckBox(lang, player, attributes),
                glowingCheckBox(lang, player, attributes));

        return DialogBase.builder(dialogTitle)
                .inputs(inputs)
                .build();
    }

    private static DialogInput displayNameTextField(LanguagesCache lang, Player player, StatueAttributes attributes) {
        var displayNameTitle = lang.message(LanguageKeys.DIALOG_STATUE_SETTINGS_DISPLAY_NAME).get(player);
        return DialogInput.text("display_name", displayNameTitle)
                .initial(attributes.nameString())
                .width(200)
                .maxLength(1000)
                .build();
    }

    private static DialogInput smallCheckBox(LanguagesCache lang, Player player, StatueAttributes attributes) {
        var smallTitle = lang.message(LanguageKeys.DIALOG_STATUE_SETTINGS_SMALL).get(player);
        return DialogInput.bool("small", smallTitle)
                .initial(attributes.isSmall()).build();
    }

    private static DialogInput basePlateCheckBox(LanguagesCache lang, Player player, StatueAttributes attributes) {
        var basePlateTitle = lang.message(LanguageKeys.DIALOG_STATUE_SETTINGS_BASE).get(player);
        return DialogInput.bool("base", basePlateTitle)
                .initial(attributes.hasBasePlate()).build();
    }

    private static DialogInput visibilityCheckBox(LanguagesCache lang, Player player, StatueAttributes attributes) {
        var visibleTitle = lang.message(LanguageKeys.DIALOG_STATUE_SETTINGS_VISIBLE).get(player);
        return DialogInput.bool("visible", visibleTitle)
                .initial(attributes.isVisible()).build();
    }

    private static DialogInput armsCheckBox(LanguagesCache lang, Player player, StatueAttributes attributes) {
        var armsTitle = lang.message(LanguageKeys.DIALOG_STATUE_SETTINGS_ARMS).get(player);
        return DialogInput.bool("arms", armsTitle)
                .initial(attributes.hasArms()).build();
    }

    private static DialogInput glowingCheckBox(LanguagesCache lang, Player player, StatueAttributes attributes) {
        var glowingTitle = lang.message(LanguageKeys.DIALOG_STATUE_SETTINGS_GLOWING).get(player);
        return DialogInput.bool("glowing", glowingTitle)
                .initial(attributes.isGlowing()).build();
    }

    private static ActionButton confirmChangesButton(LanguagesCache lang, Player player) {
        var confirmTitle = lang.message(LanguageKeys.DIALOG_STATUE_SETTINGS_CONFIRM).get(player);
        var confirmHover = lang.message(LanguageKeys.DIALOG_STATUE_SETTINGS_CONFIRM_HOVER).get(player);
        return ActionButton.create(
                confirmTitle,
                confirmHover,
                100,
                DialogAction.customClick(Key.key("unigate:statue_settings_confirmed"), null));
    }

    private static ActionButton deleteStatueButton(LanguagesCache lang, Player player) {
        var deleteTitle = lang.message(LanguageKeys.DIALOG_STATUE_SETTINGS_DELETE).get(player);
        var deletedHover = lang.message(LanguageKeys.DIALOG_STATUE_SETTINGS_DELETE_HOVER).get(player);
        return ActionButton.create(
                deleteTitle,
                deletedHover,
                100,
                DialogAction.customClick(Key.key("unigate:statue_delete"), null));
    }

}
