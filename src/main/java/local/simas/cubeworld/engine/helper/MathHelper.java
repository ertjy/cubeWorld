package local.simas.cubeworld.engine.helper;

import local.simas.cubeworld.engine.config.WindowConfig;
import local.simas.cubeworld.engine.entity.Camera;
import local.simas.cubeworld.engine.entity.Entity;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class MathHelper {
    private static final Vector3f X_AXIS = new Vector3f(1, 0, 0);
    private static final Vector3f Y_AXIS = new Vector3f(0, 1, 0);
    private static final Vector3f Z_AXIS = new Vector3f(0, 0, 1);


    public static void updateTransformationMatrix(Entity entity, Matrix4f matrix) {
        matrix.identity();
        matrix.translate(entity.getPosition(), matrix);

        Vector3f rotation = entity.getRotation();

        if (rotation.x() != 0) {
            matrix.rotate((float) Math.toRadians(rotation.x), X_AXIS, matrix);
        }

        if (rotation.y() != 0) {
            matrix.rotate((float) Math.toRadians(rotation.y), Y_AXIS, matrix);
        }

        if (rotation.z() != 0) {
            matrix.rotate((float) Math.toRadians(rotation.z), Z_AXIS, matrix);
        }

        matrix.scale(entity.getScale(), matrix);
    }

    public static void updateProjectionMatrix(WindowConfig windowConfig, Camera camera, Matrix4f matrix) {
        float nearPlane = camera.getNearPlane();
        float farPlane = camera.getFarPlane();

        float aspectRatio = (float) windowConfig.getWidth() / (float) windowConfig.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(camera.getFov() / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = farPlane - nearPlane;

        matrix.identity();
        matrix.m00(x_scale);
        matrix.m11(y_scale);
        matrix.m22(-((farPlane + nearPlane) / frustum_length));
        matrix.m23(-1);
        matrix.m32(-((2 * farPlane * nearPlane) / frustum_length));
        matrix.m33(0);
    }

    public static void updateViewMatrix(Camera camera, Matrix4f matrix) {
        matrix.identity();

        Vector3f rotation = camera.getRotation();

        if (rotation.x() != 0) {
            matrix.rotate(((float) Math.toRadians(rotation.x)), X_AXIS);
        }

        if (rotation.y() != 0) {
            matrix.rotate(((float) Math.toRadians(rotation.y)), Y_AXIS);
        }

        if (rotation.z() != 0) {
            matrix.rotate(((float) Math.toRadians(rotation.z)), Z_AXIS);
        }

        matrix.translate(new Vector3f(camera.getPosition()).negate());
    }

    public static void updateViewMatrixForSkybox(Camera camera, Matrix4f matrix) {
        matrix.identity();

        Vector3f rotation = camera.getRotation();

        if (rotation.x() != 0) {
            matrix.rotate(((float) Math.toRadians(rotation.x)), X_AXIS);
        }

        if (rotation.y() != 0) {
            matrix.rotate(((float) Math.toRadians(rotation.y)), Y_AXIS);
        }

        if (rotation.z() != 0) {
            matrix.rotate(((float) Math.toRadians(rotation.z)), Z_AXIS);
        }
    }
}
