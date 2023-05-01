package local.simas.cubeworld.engine;

import local.simas.cubeworld.engine.data.TexturedModel;
import local.simas.cubeworld.engine.entities.Camera;
import local.simas.cubeworld.engine.entities.Entity;
import local.simas.cubeworld.engine.helper.MathHelper;
import local.simas.cubeworld.engine.shader.ShaderProgram;
import lombok.Builder;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

@Builder
public class Renderer {
    private static final float FOV = 90;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000f;

    private ShaderProgram shaderProgram;
    private Camera camera;

    public Renderer(ShaderProgram shaderProgram, Camera camera) {
        this.shaderProgram = shaderProgram;
        this.camera = camera;

        this.loadProjectionMatrix();
    }

    public void prepare() {
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL11.GL_COLOR_BUFFER_BIT);
    }

    public void render(Entity entity) {
        TexturedModel texturedModel = entity.getModel();
        Matrix4f transformationMatrix = entity.getTransformationMatrix();
        Matrix4f viewMatrix = camera.getViewMatrix();

        shaderProgram.start();

        glBindVertexArray(texturedModel.getModel().getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        shaderProgram.loadTransformationMatrix(transformationMatrix);
        shaderProgram.loadViewMatrix(viewMatrix);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texturedModel.getTexture().getTextureId());

        glDrawElements(GL_TRIANGLES, texturedModel.getModel().getVertexCount(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shaderProgram.stop();
    }

    public void setShaderProgram(ShaderProgram shaderProgram) {
        if (this.shaderProgram != null) {
            this.shaderProgram.cleanUp();
        }

        this.shaderProgram = shaderProgram;
        this.loadProjectionMatrix();
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
        this.loadProjectionMatrix();
    }

    public void cleanUp() {
        shaderProgram.cleanUp();
    }

    private void loadProjectionMatrix() {
        Matrix4f projectionMatrix = MathHelper.createProjectionMatrix(DisplayManager.getWindowConfig(), camera);

        shaderProgram.start();
        shaderProgram.loadProjectionMatrix(projectionMatrix);
        shaderProgram.stop();
    }
}
