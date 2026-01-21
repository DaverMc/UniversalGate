package de.daver.unigate.statue.itemlistener;

import de.daver.unigate.statue.Statue;

public class HeadItemListener extends StatuePoseEditorListener {

    public static final String ID = "statue_move_head";

    @Override
    protected void move(Statue statue, int x, int y, int z, Context context) {
        statue.head().add(x, y, z);
        statue.head().update();
    }
}
