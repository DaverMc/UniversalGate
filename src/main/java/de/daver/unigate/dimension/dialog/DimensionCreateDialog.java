package de.daver.unigate.dimension.dialog;

import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.core.lang.LanguageManager;
import de.daver.unigate.core.lang.neu.LanguagesCache;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import org.bukkit.entity.Player;

import java.util.List;

public class DimensionCreateDialog {

    public static Dialog create(UniversalGatePlugin plugin, Player player) {
        return Dialog.create(builder -> builder.empty()
                .base(dialogBase(plugin.languageManager(), player))
                .type(dialogType(plugin.languageManager(), player)));
    }

    private static DialogBase dialogBase(LanguagesCache lang, Player player) {
        var title = lang.message(null).get(player);
        var inputs = List.of(nameInput(lang, player));
        return DialogBase.builder(title)
                .inputs(inputs)
                .build();
    }

    private static DialogType dialogType(LanguagesCache lang, Player player) {
        return DialogType.confirmation(confirmButton(lang, player), discardButton(lang, player));
    }

    private static DialogInput nameInput(LanguagesCache lang, Player player) {
        var label = lang.message(null).get(player);
        return DialogInput.text("name", label)
                .build();
    }

    private static ActionButton confirmButton(LanguagesCache lang, Player player) {
        var label = lang.message(null).get(player);

        return ActionButton.create(label, label, 100, null);
    }

    private static ActionButton discardButton(LanguagesCache lang, Player player) {
        var label = lang.message(null).get(player);

        return ActionButton.create(label, label, 100, null);
    }

}
