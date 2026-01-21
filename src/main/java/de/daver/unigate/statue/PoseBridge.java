package de.daver.unigate.statue;

import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;

public interface PoseBridge {

    void apply(EulerAngle angle, ArmorStand armorStand);

    EulerAngle loadPose(ArmorStand armorStand);

}
