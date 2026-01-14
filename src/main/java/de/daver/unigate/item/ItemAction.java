package de.daver.unigate.item;

public enum ItemAction {
    LEFT_CLICK,
    RIGHT_CLICK,
    SHIFT_LEFT_CLICK,
    SHIFT_RIGHT_CLICK,
    DROP,
    SHIFT_DROP;

    public static ItemAction fromClick(boolean left, boolean shift) {
        if (left && shift) return SHIFT_LEFT_CLICK;
        if (left) return LEFT_CLICK;
        if (shift) return SHIFT_RIGHT_CLICK;
        return RIGHT_CLICK;
    }

    public static ItemAction fromDrop(boolean shift) {
        if (shift) return SHIFT_DROP;
        return DROP;
    }
}
