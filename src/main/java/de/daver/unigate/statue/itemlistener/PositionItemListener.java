package de.daver.unigate.statue.itemlistener;

import de.daver.unigate.statue.Statue;

public class PositionItemListener extends StatuePoseEditorListener {

    public static final String ID = "statue_move_position";
    private static final double SCALE = 10.0;

    @Override
    protected void move(Statue statue, int x, int y, int z, Context context) {
        statue.position().add((double) x / SCALE, (double) y / SCALE, (double) z / SCALE);
        statue.position().update();
    }
}
