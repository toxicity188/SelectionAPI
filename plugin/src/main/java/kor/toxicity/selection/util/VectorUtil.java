package kor.toxicity.selection.util;

import org.bukkit.util.Vector;

public class VectorUtil {
    private VectorUtil() {
        throw new SecurityException();
    }

    public static Vector rotatePitch(Vector vector, double pitch) {
        var x = vector.getX();
        var y = vector.getY();

        var cos = Math.cos(pitch);
        var sin = Math.sin(pitch);

        return vector.setX(x * cos - y * sin).setY(x * sin + y * cos);
    }
    public static Vector rotateYaw(Vector vector, double pitch) {
        var x = vector.getX();
        var z = vector.getZ();

        var cos = Math.cos(pitch);
        var sin = Math.sin(pitch);

        return vector.setX(x * cos - z * sin).setZ(x * sin + z * cos);
    }
    public static double yawToRadian(double yaw) {
        return Math.toRadians((yaw < 0 ? 360 + yaw : yaw) + 90);
    }
    public static double pitchToRadian(double pitch) {
        return Math.toRadians(-pitch);
    }
}
