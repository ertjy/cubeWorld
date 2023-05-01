package local.simas.cubeworld.engine.entities;

import local.simas.cubeworld.engine.DisplayManager;
import local.simas.cubeworld.engine.helper.MathHelper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joml.Matrix4f;
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
    @Builder.Default
    private Vector3f position = new Vector3f(0f, 0f, 0f);

    @Builder.Default
    private Vector3f rotation = new Vector3f(0f, 0f, 0f);

    @Builder.Default
    private float fov = 90f;

    @Builder.Default
    private float nearPlane = 0.1f;

    @Builder.Default
    private float farPlane = 1000f;

    public void move() {
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

        velocity.mul(DisplayManager.getCurrentFrameTime());
        position.add(velocity);
    }

    public Matrix4f getViewMatrix() {
        return MathHelper.createViewMatrix(this);
    }
}
