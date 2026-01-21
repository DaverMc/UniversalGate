package de.daver.unigate.core.util;

public class Vector3D {

    private double x, y, z;

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3D() {
        this(0.0, 0.0, 0.0);
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double x() {
        return this.x;
    }

    public double y() {
        return this.y;
    }

    public double z() {
        return this.z;
    }

    public void addX(double stepX) {
        this.x += stepX;
    }

    public void addY(double stepY) {
        this.y += stepY;
    }

    public void addZ(double stepZ) {
        this.z += stepZ;
    }

    public void add(double x, double y, double z) {
        addX(x);
        addY(y);
        addZ(z);
    }

}
