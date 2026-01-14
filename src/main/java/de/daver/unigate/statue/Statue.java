package de.daver.unigate.statue;

import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;

public record Statue(ArmorStand stand) {

    public void move(int x, int y, int z) {
        stand.teleport(stand.getLocation().add(x, y, z));
    }

    public void moveHead(int x, int y, int z) {
        addEulerAngle(stand.getHeadPose(), x, y, z);
    }

    public void moveBody(int x, int y, int z)  {
        addEulerAngle(stand.getBodyPose(), x, y, z);
    }

    public void moveLeftArm(int x, int y, int z) {
        addEulerAngle(stand.getLeftArmPose(), x, y, z);
    }

    public void moveRightArm(int x, int y, int z) {
        addEulerAngle(stand.getRightArmPose(), x, y, z);
    }

    public void moveLeftLeg(int x, int y, int z) {
        addEulerAngle(stand.getLeftLegPose(), x, y, z);
    }

    public void moveRightLeg(int x, int y, int z) {
        addEulerAngle(stand.getRightLegPose(), x, y, z);
    }

    private void addEulerAngle(EulerAngle angle, int x, int y, int z) {
        angle.add(x, y, z);
    }
}
