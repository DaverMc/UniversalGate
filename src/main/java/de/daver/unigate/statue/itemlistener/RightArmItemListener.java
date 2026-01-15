package de.daver.unigate.statue.itemlistener;

import de.daver.unigate.statue.Statue;

public class RightArmItemListener extends StatueEditorListener {

    public static final String ID = "statue_move_right_arm";

    @Override
    protected void move(Statue statue, int x, int y, int z, Context context) {
        statue.moveRightArm(x, y, z);
    }
}
