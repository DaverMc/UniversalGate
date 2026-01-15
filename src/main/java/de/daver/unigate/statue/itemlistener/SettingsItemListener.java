package de.daver.unigate.statue.itemlistener;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.item.ItemActionListener;
import de.daver.unigate.listener.PluginEventListener;
import de.daver.unigate.statue.Statue;
import io.papermc.paper.connection.PlayerGameConnection;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.event.player.PlayerCustomClickEvent;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public class SettingsItemListener implements ItemActionListener {

    public static final String ID = "statue_settings";

    @Override
    public void onClick(Context context) {
        var player = context.player();
        var statue = context.plugin().statueInteractListener().get(player);
        if(statue == null) return;
        var dialog = createDialog(context.plugin(), statue);
        player.showDialog(dialog);
    }

    private Dialog createDialog(UniversalGatePlugin plugin, Statue statue) {
        var lang = plugin.languageManager();
        return Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(Component.text("Configure your new experience value"))
                        .inputs(List.of(
                                DialogInput.text("display_name", Component.text("Display Name", NamedTextColor.AQUA))
                                        .initial(statue.displayName())
                                        .width(500)
                                        .maxLength(1000)
                                        .build(),
                                DialogInput.bool("small", Component.text("Small", NamedTextColor.AQUA))
                                        .initial(statue.small()).build(),
                                DialogInput.bool("base", Component.text("Base", NamedTextColor.AQUA))
                                        .initial(statue.basePlate()).build(),
                                DialogInput.bool("visible", Component.text("Visible", NamedTextColor.AQUA))
                                        .initial(statue.visible()).build(),
                                DialogInput.bool("gravity", Component.text("Gravity", NamedTextColor.AQUA))
                                        .initial(statue.gravity()).build(),
                                DialogInput.bool("arms", Component.text("Arms", NamedTextColor.AQUA))
                                        .initial(statue.arms()).build(),
                                DialogInput.bool("glowing", Component.text("Glowing", NamedTextColor.AQUA))
                                        .initial(statue.glowing()).build(),
                                DialogInput.bool("name_visible", Component.text("NameVisible", NamedTextColor.AQUA))
                                        .initial(statue.nameVisible()).build(),
                                DialogInput.bool("delete", Component.text("Delete", NamedTextColor.RED))
                                        .initial(false)
                                        .build()
                                ))
                        .build()
                )
                .type(DialogType.confirmation(
                        ActionButton.create(
                                Component.text("Confirm", TextColor.color(0xAEFFC1)),
                                Component.text("Click to confirm your input."),
                                100,
                                DialogAction.customClick(Key.key("unigate:statue_settings_confirmed"), null)
                        ),
                        ActionButton.create(
                                Component.text("Discard", TextColor.color(0xFFA0B1)),
                                Component.text("Click to discard your input."),
                                100,
                                null
                        )
                ))
        );
    }

}
