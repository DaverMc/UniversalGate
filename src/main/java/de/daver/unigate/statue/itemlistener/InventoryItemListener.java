package de.daver.unigate.statue.itemlistener;

import de.daver.unigate.item.ItemActionListener;
import de.daver.unigate.statue.StatueInventoryGui;

public class InventoryItemListener implements ItemActionListener {

    public static final String ID = "statue_inventory";

    @Override
    public void onClick(Context context) {
        var player = context.player();
        var statue = context.plugin().statueInteractListener().get(player);
        if(statue == null) return;
        new StatueInventoryGui(context.plugin(), player, statue).open(player);
    }
}
