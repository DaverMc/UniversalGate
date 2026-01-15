package de.daver.unigate.statue.itemlistener;

import de.daver.unigate.statue.Statue;

public class PositionItemListener extends StatueEditorListener {

    public static final String ID = "statue_move_position";

    @Override
    protected void move(Statue statue, int x, int y, int z, Context context) {
        statue.move((double) x / 10, (double) y / 10, (double) z / 10);
    }
}
