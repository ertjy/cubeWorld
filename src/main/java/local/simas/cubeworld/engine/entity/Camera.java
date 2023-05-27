package local.simas.cubeworld.engine.entity;

import local.simas.cubeworld.engine.DisplayManager;
import local.simas.cubeworld.engine.helper.MathHelper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Camera {
    private static final float LOOK_SPEED = 0.25f;

    @Builder.Default
    private Vector3f position = new Vector3f(0f, 0f, 0f);

    @Builder.Default
    private Vector3f rotation = new Vector3f(0f, 0f, 0f);

    @Builder.Default
    private float fov = 90f;

    @Builder.Default
    private float nearPlane = 0.1f;

    @Builder.Default
    private float farPlane = 5000f;

    private Vector2f lastCursorPosition;

    public void move() {
        if (lastCursorPosition == null) {
            lastCursorPosition = DisplayManager.getCursorPosition();
            return;
        }

        Vector3f velocity = new Vector3f(0, 0, 0);

        if(DisplayManager.getKey(GLFW_KEY_W) == GLFW_PRESS) {
            velocity.add(0f, 0f, -1f);
        }

        if(DisplayManager.getKey(GLFW_KEY_S) == GLFW_PRESS) {
            velocity.add(0f, 0f, 1f);
        }

        if(DisplayManager.getKey(GLFW_KEY_A) == GLFW_PRESS) {
            velocity.add(-1f, 0f, 0f);
        }

        if(DisplayManager.getKey(GLFW_KEY_D) == GLFW_PRESS) {
            velocity.add(1f, 0f, 0f);
        }

        velocity.rotateAxis((float) Math.toRadians(-rotation.x()), 1, 0, 0, velocity);
        velocity.rotateAxis((float) Math.toRadians(-rotation.y()), 0, 1, 0, velocity);
        velocity.rotateAxis((float) Math.toRadians(-rotation.z()), 0, 0, 1, velocity);

        velocity.mul(DisplayManager.getCurrentFrameTime());
        position.add(velocity);

        Vector2f currentCursorPosition = DisplayManager.getCursorPosition();
        Vector2f deltaCursorPosition = new Vector2f(currentCursorPosition).sub(lastCursorPosition);

        Vector2f newCursorPosition = new Vector2f(DisplayManager.getWindowConfig().getWidth(), DisplayManager.getWindowConfig().getHeight()).div(2);
        DisplayManager.setCursorPosition(newCursorPosition);

        lastCursorPosition = new Vector2f(newCursorPosition);

        rotation.add(new Vector3f(deltaCursorPosition.y() * LOOK_SPEED, deltaCursorPosition.x() * LOOK_SPEED, 0f));
    }

    public Matrix4f getViewMatrix() {
        return MathHelper.createViewMatrix(this);
    }

    public Matrix4f getViewMatrixForSkybox() {
        return MathHelper.createViewMatrixForSkybox(this);
    }
}
