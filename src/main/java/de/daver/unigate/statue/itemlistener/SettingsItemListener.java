package de.daver.unigate.statue.itemlistener;

import de.daver.unigate.item.ItemActionListener;
import de.daver.unigate.statue.StatueSettingsDialog;

public class SettingsItemListener implements ItemActionListener {

    public static final String ID = "statue_settings";

    @Override
    public void onClick(Context context) {
        var player = context.player();
        var statue = context.plugin().statueInteractListener().get(player);
        if (statue == null) return;
        var dialog = StatueSettingsDialog.create(context.plugin(), player, statue);
        player.showDialog(dialog);
    }


}
