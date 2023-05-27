package local.simas.cubeworld.engine.helper;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.nio.FloatBuffer;

public class BufferHelper {
    public static void putVectorInBuffer(Vector3f v, FloatBuffer dest) {
        dest.put(v.x());
        dest.put(v.y());
        dest.put(v.z());
    }

    public static void putMatrixInBuffer(Matrix4f m, FloatBuffer dest) {
        dest.put(m.m00());
        dest.put(m.m01());
        dest.put(m.m02());
        dest.put(m.m03());
        dest.put(m.m10());
        dest.put(m.m11());
        dest.put(m.m12());
        dest.put(m.m13());
        dest.put(m.m20());
        dest.put(m.m21());
        dest.put(m.m22());
        dest.put(m.m23());
        dest.put(m.m30());
        dest.put(m.m31());
        dest.put(m.m32());
        dest.put(m.m33());
    }
}
