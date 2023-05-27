package local.simas.cubeworld.engine.helper;

import local.simas.cubeworld.engine.config.WindowConfig;
import local.simas.cubeworld.engine.entity.Camera;
import local.simas.cubeworld.engine.entity.Entity;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class MathHelper {
    public static Matrix4f createTransformationMatrix(Entity entity) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity();

        matrix.translate(entity.getPosition(), matrix);

        matrix.rotate((float) Math.toRadians(entity.getRotation().x), new Vector3f(1, 0, 0), matrix);
        matrix.rotate((float) Math.toRadians(entity.getRotation().y), new Vector3f(0, 1, 0), matrix);
        matrix.rotate((float) Math.toRadians(entity.getRotation().z), new Vector3f(0, 0, 1), matrix);

        matrix.scale(entity.getScale(), matrix);

        return matrix;
    }

    public static Matrix4f createProjectionMatrix(WindowConfig windowConfig, Camera camera) {
        float aspectRatio = (float) windowConfig.getWidth() / (float) windowConfig.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(camera.getFov() / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = camera.getFarPlane() - camera.getNearPlane();

        Matrix4f matrix = new Matrix4f();
        matrix.identity();

        matrix.m00(x_scale);
        matrix.m11(y_scale);
        matrix.m22(-((camera.getFarPlane() + camera.getNearPlane()) / frustum_length));
        matrix.m23(-1);
        matrix.m32(-((2 * camera.getFarPlane() * camera.getNearPlane()) / frustum_length));
        matrix.m33(0);

        return matrix;
    }

    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity();

        matrix.rotate(((float) Math.toRadians(camera.getRotation().x)), new Vector3f(1, 0, 0));
        matrix.rotate(((float) Math.toRadians(camera.getRotation().y)), new Vector3f(0, 1, 0));
        matrix.rotate(((float) Math.toRadians(camera.getRotation().z)), new Vector3f(0, 0, 1));

        matrix.translate(new Vector3f(camera.getPosition()).negate());

        return matrix;
    }
}
