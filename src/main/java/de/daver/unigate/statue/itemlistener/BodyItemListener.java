package de.daver.unigate.statue.itemlistener;

import de.daver.unigate.statue.Statue;

public class BodyItemListener extends StatuePoseEditorListener {

    public static final String ID = "statue_move_body";

    @Override
    protected void move(Statue statue, int x, int y, int z, Context context) {
        statue.body().add(x, y, z);
        statue.body().update();
    }
}
