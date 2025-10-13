package de.daver.unigate.statue.itemlistener;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.item.ItemAction;
import de.daver.unigate.item.ItemActionListener;
import de.daver.unigate.item.ItemWrapper;
import de.daver.unigate.statue.Statue;


public abstract class StatuePoseEditorListener implements ItemActionListener {

    public static final int AXIS_X = 0;
    public static final int AXIS_Y = 1;
    public static final int AXIS_Z = 2;

    protected abstract void move(Statue statue, int x, int y, int z, Context context);

    @Override
    public void onClick(Context context) {
        var statue = context.plugin().statueInteractListener().get(context.player());
        if(statue == null) return;

        var item = new ItemWrapper(context.plugin(), context.itemStack());

        var amount = getAmount(context.action());
        var mode = item.getMode();

        switch (context.action()) {
            case LEFT_CLICK, RIGHT_CLICK, SHIFT_LEFT_CLICK, SHIFT_RIGHT_CLICK -> move(statue, amount, mode, context);
            case DROP, SHIFT_DROP -> nexMode(mode, item, context);
        }
    }

    protected void move(Statue statue, int amount, int axis, Context context) {
        switch (axis) {
            case AXIS_X -> move(statue, amount, 0, 0, context);
            case AXIS_Y -> move(statue, 0, amount, 0, context);
            case AXIS_Z -> move(statue, 0, 0, amount, context);
        }
    }

    private int getAmount(ItemAction action) {
        return switch (action) {
            case LEFT_CLICK -> -10;
            case RIGHT_CLICK -> 10;
            case SHIFT_LEFT_CLICK -> -1;
            case SHIFT_RIGHT_CLICK -> 1;
            default -> 0;
        };
    }

    protected void nexMode(int mode, ItemWrapper item, Context context) {
        if(mode == AXIS_Z) item.mode(0);
        else item.mode(mode + 1);
        mode = item.getMode();

        var axis = switch (mode) {
            case AXIS_X -> "X";
            case AXIS_Y -> "Y";
            case AXIS_Z -> "Z";
            default -> "";
        };

        context.plugin().languageManager()
                .message(LanguageKeys.STATUE_TOOLS_CHANGE_AXIS)
                .argument("axis", axis)
                .send(context.player());

        var key = switch (mode) {
            case AXIS_X -> LanguageKeys.ITEM_STATUE_EDITOR_LORE_X;
            case AXIS_Y -> LanguageKeys.ITEM_STATUE_EDITOR_LORE_Y;
            case AXIS_Z -> LanguageKeys.ITEM_STATUE_EDITOR_LORE_Z;
            default -> throw new IllegalStateException("Unexpected value: " + mode);
        };

        item.lore(context.plugin().languageManager().message(key).getLines(context.player()));
    }

}
