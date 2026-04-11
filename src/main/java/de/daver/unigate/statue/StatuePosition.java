package de.daver.unigate.statue;

import de.daver.unigate.core.util.Vector3D;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

public class StatuePosition extends Vector3D {

    private final Statue statue;
    private float yaw;

    public StatuePosition(Statue statue) {
        super();
        this.statue = statue;
        load(statue.getEntity());
    }

    private void load(ArmorStand stand) {
        var location = stand.getLocation();
        setX(location.getX());
        setY(location.getY());
        setZ(location.getZ());
        setYaw(location.getYaw());
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float yaw() {
        return this.yaw;
    }

    public void addYaw(float yaw) {
        this.yaw += yaw;
    }

    public void update() {
        var stand = this.statue.getEntity();
        if (stand == null) return;
        update(stand);
    }

    void update(ArmorStand stand) {
        var world = stand.getLocation().getWorld();
        var newLocation = new Location(world, x(), y(), z(), yaw(), 0.0f);
        stand.teleport(newLocation);
    }

    public void set(StatuePosition other) {
        setX(other.x());
        setY(other.y());
        setZ(other.z());
        setYaw(other.yaw());
    }

}
