package local.simas.cubeworld.engine;

import local.simas.cubeworld.engine.data.LoadedModel;
import local.simas.cubeworld.engine.data.TexturedModel;
import local.simas.cubeworld.engine.entity.Camera;
import local.simas.cubeworld.engine.entity.Entity;
import local.simas.cubeworld.engine.entity.Skybox;
import local.simas.cubeworld.engine.entity.light.Light;
import local.simas.cubeworld.engine.helper.MathHelper;
import local.simas.cubeworld.engine.loader.ModelLoader;
import local.simas.cubeworld.engine.shader.EntityShader;
import local.simas.cubeworld.engine.shader.SkyboxShader;
import lombok.Builder;
import org.joml.Matrix4f;

import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;

@Builder
public class Renderer {
    private static final float FOV = 90;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000f;

    private final Matrix4f projectionMatrix = new Matrix4f();

    private ModelLoader modelLoader;
    private EntityShader entityShader;
    private SkyboxShader skyboxShader;
    private Camera camera;

    public Renderer(ModelLoader modelLoader, EntityShader entityShader, SkyboxShader skyboxShader, Camera camera) {
        this.modelLoader = modelLoader;
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

    public void renderEntities(Map<TexturedModel, List<Entity>> entities, List<Light> lights) {
        entityShader.start();
        entityShader.loadViewMatrix(camera.getViewMatrix());
        entityShader.loadLights(lights);
//        TODO: Fix
        entityShader.loadReflectivity(1);
        entityShader.loadShineDamper(5);

        for (TexturedModel texturedModel : entities.keySet()) {
            glBindVertexArray(texturedModel.getModel().getVaoId());
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);
            glEnableVertexAttribArray(2);
            glEnableVertexAttribArray(3);
            glEnableVertexAttribArray(4);
            glEnableVertexAttribArray(5);
            glEnableVertexAttribArray(6);

            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, texturedModel.getTexture().getTextureId());

            List<Entity> similarEntities = entities.get(texturedModel);
            LoadedModel loadedModel = texturedModel.getModel();
            for (int i = 0; i <= similarEntities.size() / loadedModel.getMaxUsages(); i++) {
                List<Entity> entityBatch = similarEntities.subList(
                        i * loadedModel.getMaxUsages(),
                        Math.min((i + 1) * loadedModel.getMaxUsages(), similarEntities.size())
                );

                modelLoader.updateLoadedModelWithEntities(texturedModel.getModel(), entityBatch);
                glDrawElementsInstanced(GL_TRIANGLES, texturedModel.getModel().getVertexCount(), GL_UNSIGNED_INT, 0, entityBatch.size());
            }

            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glDisableVertexAttribArray(2);
            glDisableVertexAttribArray(3);
            glDisableVertexAttribArray(4);
            glDisableVertexAttribArray(5);
            glDisableVertexAttribArray(6);
            glBindVertexArray(0);
        }

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
        MathHelper.updateProjectionMatrix(DisplayManager.getWindowConfig(), camera, projectionMatrix);

        entityShader.start();
        entityShader.loadProjectionMatrix(projectionMatrix);
        entityShader.loadCamera(camera);
        entityShader.stop();

        skyboxShader.start();
        skyboxShader.loadProjectionMatrix(projectionMatrix);
        skyboxShader.stop();
    }
}
