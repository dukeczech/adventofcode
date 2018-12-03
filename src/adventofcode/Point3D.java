/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventofcode;

/**
 *
 * @author hebik
 */
public class Point3D {

    private int x = 0;
    private int y = 0;
    private int z = 0;

    public Point3D(int xx, int yy, int zz) {
        x = xx;
        y = yy;
        z = zz;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public void add(Point3D o) {
        x += o.x;
        y += o.y;
        z += o.z;
    }

    public void add(int dx, int dy, int dz) {
        x += dx;
        y += dy;
        z += dz;
    }

    public int distance(Point3D other) {
        return (Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(z - other.z));
    }

    @Override
    public String toString() {
        return "[ " + x + ", " + y + ", " + z + " ]";
    }
}
