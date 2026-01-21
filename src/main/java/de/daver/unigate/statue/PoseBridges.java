package de.daver.unigate.statue;

import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;

class PoseBridges {

    static class Head implements PoseBridge {

        @Override
        public void apply(EulerAngle angle, ArmorStand armorStand) {
            armorStand.setHeadPose(angle);
        }

        @Override
        public EulerAngle loadPose(ArmorStand armorStand) {
            return armorStand.getHeadPose();
        }

    }

    static class Body implements PoseBridge {

        @Override
        public void apply(EulerAngle angle, ArmorStand armorStand) {
            armorStand.setBodyPose(angle);
        }

        @Override
        public EulerAngle loadPose(ArmorStand armorStand) {
            return armorStand.getBodyPose();
        }

    }

    static class LeftArm implements PoseBridge {


        @Override
        public void apply(EulerAngle angle, ArmorStand armorStand) {

        }

        @Override
        public EulerAngle loadPose(ArmorStand armorStand) {
            return null;
        }
    }

    static class RightArm implements PoseBridge {

        @Override
        public void apply(EulerAngle angle, ArmorStand armorStand) {

        }

        @Override
        public EulerAngle loadPose(ArmorStand armorStand) {
            return null;
        }
    }

    static class LeftLeg implements PoseBridge {

        @Override
        public void apply(EulerAngle angle, ArmorStand armorStand) {

        }

        @Override
        public EulerAngle loadPose(ArmorStand armorStand) {
            return null;
        }
    }

    static class RightLeg implements PoseBridge {

        @Override
        public void apply(EulerAngle angle, ArmorStand armorStand) {

        }

        @Override
        public EulerAngle loadPose(ArmorStand armorStand) {
            return null;
        }
    }
}
