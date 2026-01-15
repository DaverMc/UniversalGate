package de.daver.unigate.statue.itemlistener;

import de.daver.unigate.statue.Statue;

public class LeftLegItemListener extends StatueEditorListener {

    public static final String ID = "statue_move_left_leg";

    @Override
    protected void move(Statue statue, int x, int y, int z, Context context) {
        statue.moveLeftLeg(x, y, z);
    }
}
