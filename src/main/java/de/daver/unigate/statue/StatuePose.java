package de.daver.unigate.statue;

import de.daver.unigate.core.util.Vector3D;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;

public class StatuePose extends Vector3D {

    private final Statue statue;
    private final PoseBridge applier;

    StatuePose(Statue statue, double x, double y, double z) {
        super(x,y,z);
        this.statue = statue;
        this.applier = new PoseBridges.Empty();
    }

    StatuePose(Statue statue, PoseBridge bridge) {
        super();
        this.statue = statue;
        this.applier = bridge;
        load(bridge.loadPose(statue.getEntity()));
    }

    public void update() {
        var entity = this.statue.getEntity();
        if(entity == null) return;
        update(entity);
    }

    void update(ArmorStand stand) {
        this.applier.apply(toEulerAngle(), stand);
    }

    private void load(EulerAngle angle) {
        var x = Math.toDegrees(angle.getX());
        var y = Math.toDegrees(angle.getY());
        var z = Math.toDegrees(angle.getZ());
        setX(x);
        setY(y);
        setZ(z);
    }

    public EulerAngle toEulerAngle() {
        return new EulerAngle(Math.toRadians(this.x()), Math.toRadians(this.y()), Math.toRadians(this.z()));
    }

    public void set(StatuePose other) {
        setX(other.x());
        setY(other.y());
        setZ(other.z());
    }
}
