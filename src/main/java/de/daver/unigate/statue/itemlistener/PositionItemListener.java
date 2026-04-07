package de.daver.unigate.statue.itemlistener;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.item.ItemWrapper;
import de.daver.unigate.statue.Statue;

public class PositionItemListener extends StatuePoseEditorListener {

    public static final String ID = "statue_move_position";
    private static final double SCALE = 10.0;
    private static final int AXIS_ROTATION = 3;

    @Override
    protected void move(Statue statue, int x, int y, int z, Context context) {
        statue.position().add((double) x / SCALE, (double) y / SCALE, (double) z / SCALE);
        statue.position().update();
    }

    @Override
    protected void move(Statue statue, int amount, int axis, Context context) {
        if(axis == AXIS_ROTATION) moveYaw(statue, amount);
        super.move(statue, amount, axis, context);
    }

    @Override
    protected void nexMode(int mode, ItemWrapper item, Context context) {
        if(mode == AXIS_ROTATION) item.mode(0);
        else item.mode(mode + 1);
        mode = item.getMode();

        var axis = switch (mode) {
            case AXIS_X -> "X";
            case AXIS_Y -> "Y";
            case AXIS_Z -> "Z";
            case AXIS_ROTATION -> "Rotation";
            default -> "";
        };

        context.plugin().languageManager()
                .message(LanguageKeys.STATUE_TOOLS_CHANGE_AXIS)
                .argument("axis", axis)
                .send(context.player());

        var key = switch (mode) {
            case AXIS_X -> LanguageKeys.ITEM_STATUE_HEAD_LORE_X;
            case AXIS_Y -> LanguageKeys.ITEM_STATUE_HEAD_LORE_Y;
            case AXIS_Z -> LanguageKeys.ITEM_STATUE_HEAD_LORE_Z;
            case AXIS_ROTATION -> LanguageKeys.ITEM_STATUE_HEAD_LORE_ROTATION;
            default -> throw new IllegalStateException("Unexpected value: " + mode);
        };

        item.lore(context.plugin().languageManager().message(key).getLines(context.player()));
    }

    private void moveYaw(Statue statue, int amount) {
        statue.position().addYaw(amount);
        statue.position().update();
    }
}
