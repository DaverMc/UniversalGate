package de.daver.unigate.statue;

import de.daver.unigate.core.util.Vector3D;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

public class StatuePosition extends Vector3D {

    private final ArmorStand stand;
    private float yaw;

    public StatuePosition(ArmorStand stand) {
        super();
        this.stand = stand;
        this.yaw = 0.0f;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float yaw() {
        return this.yaw;
    }

    public void addYaw(float yaw) {
        this.yaw = yaw;
    }

    public void update() {
        var world = this.stand.getLocation().getWorld();
        var newLocation = new Location(world, x(), y(), z(), yaw(), 0.0f);
        this.stand.teleport(newLocation);
    }

}
