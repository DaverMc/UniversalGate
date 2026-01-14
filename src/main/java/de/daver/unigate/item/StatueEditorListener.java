package de.daver.unigate.item;

import org.bukkit.util.EulerAngle;


public abstract class StatueEditorListener implements ItemActionListener {

    protected abstract EulerAngle getAngle();

    @Override
    public void onClick(Context context) {
        var item = new ItemWrapper(context.plugin(), context.itemStack());

        var amount = getAmount(context.action());
        var mode = item.getMode();
        var angle = getAngle();
        switch (context.action()) {
            case LEFT_CLICK, RIGHT_CLICK, SHIFT_LEFT_CLICK, SHIFT_RIGHT_CLICK -> setAmount(angle, amount, mode);
            case DROP -> ;
        }
    }

    private int getPreviousMode(int mode) {
        return (mode + 1) % 3;
    }

    public int getAmount(ItemAction action) {
        return switch (action) {
            case LEFT_CLICK -> -10;
            case RIGHT_CLICK -> 10;
            case SHIFT_LEFT_CLICK -> -1;
            case SHIFT_RIGHT_CLICK -> 1;
            default -> 0;
        };
    }

    private void setAmount(EulerAngle angle, int amount, int mode) {
        switch (mode) {
            case 0 -> angle.setX(amount);
            case 1 -> angle.setY(amount);
            case 2 -> angle.setZ(amount);
        }
    }


}
