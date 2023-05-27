package local.simas.cubeworld.engine;

import local.simas.cubeworld.engine.data.TexturedModel;
import local.simas.cubeworld.engine.entity.Camera;
import local.simas.cubeworld.engine.entity.Entity;
import local.simas.cubeworld.engine.entity.Skybox;
import local.simas.cubeworld.engine.entity.light.Light;
import local.simas.cubeworld.engine.helper.MathHelper;
import local.simas.cubeworld.engine.shader.EntityShader;
import local.simas.cubeworld.engine.shader.SkyboxShader;
import lombok.Builder;
import org.joml.Matrix4f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

@Builder
public class Renderer {
    private static final float FOV = 90;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000f;

    private EntityShader entityShader;
    private SkyboxShader skyboxShader;
    private Camera camera;

    public Renderer(EntityShader entityShader, SkyboxShader skyboxShader, Camera camera) {
        this.entityShader = entityShader;
        this.skyboxShader = skyboxShader;
        this.camera = camera;

        this.loadCamera();
    }

    public void prepare() {
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void renderSkybox(Skybox skybox) {
        TexturedModel texturedModel = skybox.getModel();
        Matrix4f viewMatrix = camera.getViewMatrixForSkybox();

        skyboxShader.start();

        glBindVertexArray(texturedModel.getModel().getVaoId());
        glEnableVertexAttribArray(0);

        skyboxShader.loadViewMatrix(viewMatrix);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_CUBE_MAP, texturedModel.getTexture().getTextureId());

        glDrawElements(GL_TRIANGLES, texturedModel.getModel().getVertexCount(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

        skyboxShader.stop();
    }

    public void renderEntity(Entity entity, List<Light> lights) {
        TexturedModel texturedModel = entity.getModel();
        Matrix4f transformationMatrix = entity.getTransformationMatrix();
        Matrix4f viewMatrix = camera.getViewMatrix();

        entityShader.start();

        glBindVertexArray(texturedModel.getModel().getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        entityShader.loadTransformationMatrix(transformationMatrix);
        entityShader.loadViewMatrix(viewMatrix);
        entityShader.loadLights(lights);
        entityShader.loadReflectivity(texturedModel.getTexture().getReflectivity());
        entityShader.loadShineDamper(texturedModel.getTexture().getShineDamper());

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texturedModel.getTexture().getTextureId());

        glDrawElements(GL_TRIANGLES, texturedModel.getModel().getVertexCount(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);

        entityShader.stop();
    }

    public void setEntityShader(EntityShader entityShader) {
        if (this.entityShader != null) {
            this.entityShader.cleanUp();
        }

        this.entityShader = entityShader;
        this.loadCamera();
    }

    public void setSkyboxShader(SkyboxShader skyboxShader) {
        if (this.skyboxShader != null) {
            this.skyboxShader.cleanUp();
        }

        this.skyboxShader = skyboxShader;
        this.loadCamera();
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
        this.loadCamera();
    }

    public void cleanUp() {
        entityShader.cleanUp();
        skyboxShader.cleanUp();
    }

    private void loadCamera() {
        Matrix4f projectionMatrix = MathHelper.createProjectionMatrix(DisplayManager.getWindowConfig(), camera);

        entityShader.start();
        entityShader.loadProjectionMatrix(projectionMatrix);
        entityShader.loadCamera(camera);
        entityShader.stop();

        skyboxShader.start();
        skyboxShader.loadProjectionMatrix(projectionMatrix);
        skyboxShader.stop();
    }
}
