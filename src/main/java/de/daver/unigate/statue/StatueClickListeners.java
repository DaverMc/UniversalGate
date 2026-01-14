package de.daver.unigate.statue;

import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.item.ItemActionListener;

public interface StatueClickListeners {

    String MOVE_HEAD_X = "move_head_x";
    String MOVE_HEAD_Y = "move_head_y";
    String MOVE_HEAD_Z = "move_head_z";
    String MOVE_BODY_X = "move_body_x";
    String MOVE_BODY_Y = "move_body_y";
    String MOVE_BODY_Z = "move_body_z";
    String MOVE_LEFT_ARM_X = "move_left_arm_x";
    String MOVE_LEFT_ARM_Y = "move_left_arm_y";
    String MOVE_LEFT_ARM_Z = "move_left_arm_z";
    String MOVE_RIGHT_ARM_X = "move_right_arm_x";
    String MOVE_RIGHT_ARM_Y = "move_right_arm_y";
    String MOVE_RIGHT_ARM_Z = "move_right_arm_z";
    String MOVE_LEFT_LEG_X = "move_left_leg_x";
    String MOVE_LEFT_LEG_Y = "move_left_leg_y";
    String MOVE_LEFT_LEG_Z = "move_left_leg_z";
    String MOVE_RIGHT_LEG_X = "move_right_leg_x";
    String MOVE_RIGHT_LEG_Y = "move_right_leg_y";
    String MOVE_RIGHT_LEG_Z = "move_right_leg_z";
    String MOVE_POSITION_X = "move_position_x";
    String MOVE_POSITION_Y = "move_position_y";
    String MOVE_POSITION_Z = "move_position_z";


    static void register(UniversalGatePlugin plugin) {
        ItemActionListener headMovementX = move((statue, amount) -> statue.moveHead(amount, 0, 0));
        ItemActionListener headMovementY = move((statue, amount) -> statue.moveHead(0, amount, 0));
        ItemActionListener headMovementZ = move((statue, amount) -> statue.moveHead(0, 0, amount));

        ItemActionListener bodyMovementX = move((statue, amount) -> statue.moveBody(amount, 0, 0));
        ItemActionListener bodyMovementY = move((statue, amount) -> statue.moveBody(0, amount, 0));
        ItemActionListener bodyMovementZ = move((statue, amount) -> statue.moveBody(0, 0, amount));

        ItemActionListener leftArmMovementX = move((statue, amount) -> statue.moveLeftArm(amount, 0, 0));
        ItemActionListener leftArmMovementY = move((statue, amount) -> statue.moveLeftArm(0, amount, 0));
        ItemActionListener leftArmMovementZ = move((statue, amount) -> statue.moveLeftArm(0, 0, amount));

        ItemActionListener rightArmMovementX = move((statue, amount) -> statue.moveRightArm(amount, 0, 0));
        ItemActionListener rightArmMovementY = move((statue, amount) -> statue.moveRightArm(0, amount, 0));
        ItemActionListener rightArmMovementZ = move((statue, amount) -> statue.moveRightArm(0, 0, amount));

        ItemActionListener leftLegMovementX = move((statue, amount) -> statue.moveLeftLeg(amount, 0, 0));
        ItemActionListener leftLegMovementY = move((statue, amount) -> statue.moveLeftLeg(0, amount, 0));
        ItemActionListener leftLegMovementZ = move((statue, amount) -> statue.moveLeftLeg(0, 0, amount));

        ItemActionListener rightLegMovementX = move((statue, amount) -> statue.moveRightLeg(amount, 0, 0));
        ItemActionListener rightLegMovementY = move((statue, amount) -> statue.moveRightLeg(0, amount, 0));
        ItemActionListener rightLegMovementZ = move((statue, amount) -> statue.moveRightLeg(0, 0, amount));

        ItemActionListener positionMovementX = move((statue, amount) -> statue.move(amount, 0, 0));
        ItemActionListener positionMovementY = move((statue, amount) -> statue.move(0, amount, 0));
        ItemActionListener positionMovementZ = move((statue, amount) -> statue.move(0, 0, amount));

        var items = plugin.itemInteractListener();

        items.register(MOVE_HEAD_X, headMovementX);
        items.register(MOVE_HEAD_Y, headMovementY);
        items.register(MOVE_HEAD_Z, headMovementZ);
        items.register(MOVE_BODY_X, bodyMovementX);
        items.register(MOVE_BODY_Y, bodyMovementY);
        items.register(MOVE_BODY_Z, bodyMovementZ);
        items.register(MOVE_LEFT_ARM_X, leftArmMovementX);
        items.register(MOVE_LEFT_ARM_Y, leftArmMovementY);
        items.register(MOVE_LEFT_ARM_Z, leftArmMovementZ);
        items.register(MOVE_RIGHT_ARM_X, rightArmMovementX);
        items.register(MOVE_RIGHT_ARM_Y, rightArmMovementY);
        items.register(MOVE_RIGHT_ARM_Z, rightArmMovementZ);
        items.register(MOVE_LEFT_LEG_X, leftLegMovementX);
        items.register(MOVE_LEFT_LEG_Y, leftLegMovementY);
        items.register(MOVE_LEFT_LEG_Z, leftLegMovementZ);
        items.register(MOVE_RIGHT_LEG_X, rightLegMovementX);
        items.register(MOVE_RIGHT_LEG_Y, rightLegMovementY);
        items.register(MOVE_RIGHT_LEG_Z, rightLegMovementZ);
        items.register(MOVE_POSITION_X, positionMovementX);
        items.register(MOVE_POSITION_Y, positionMovementY);
        items.register(MOVE_POSITION_Z, positionMovementZ);
    }

    private static ItemActionListener move(StatueMovement amountConsumer) {
        return (context) -> {
            Statue statue = context.plugin().statueInteractListener().get(context.player());
            if(statue == null) return;
            int amount = switch (context.action()) {
                case LEFT -> -10;
                case RIGHT -> 10;
                case SHIFT_LEFT -> -1;
                case SHIFT_RIGHT -> 1;
            };
            amountConsumer.move(statue, amount);
        };
    }

    interface StatueMovement {

        void move(Statue statue, int amount);

    }

}
