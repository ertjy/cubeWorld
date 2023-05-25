package local.simas.cubeworld.game.shader;

import local.simas.cubeworld.engine.entities.Camera;
import local.simas.cubeworld.engine.entities.Light;
import local.simas.cubeworld.engine.shader.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DefaultShaderProgram extends ShaderProgram {
    private static final String VERTEX_SHADER_FILE = "shaders/vertexShader.glsl";
    private static final String FRAGMENT_SHADER_FILE = "shaders/fragmentShader.glsl";
    private static final int LIGHT_COUNT = 16;

    private int transformationMatrixLocation;
    private int projectionMatrixLocation;
    private int viewMatrixLocation;
    private int cameraPositionLocation;
    private int lightCountLocation;
    private int lightsLocation;
    private int reflectivityLocation;
    private int shineDamperLocation;

    public DefaultShaderProgram() throws IOException {
        super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoordinates");
        super.bindAttribute(2, "normal");
    }

    @Override
    protected void getAllUniformLocations() {
        transformationMatrixLocation = super.getUniformLocation("transformationMatrix");
        projectionMatrixLocation = super.getUniformLocation("projectionMatrix");
        viewMatrixLocation = super.getUniformLocation("viewMatrix");
        cameraPositionLocation = super.getUniformLocation("cameraPosition");
        lightCountLocation = super.getUniformLocation("lightCount");
        lightsLocation = super.getUniformLocation("lights");
        reflectivityLocation = super.getUniformLocation("reflectivity");
        shineDamperLocation = super.getUniformLocation("shineDamper");
    }

    @Override
    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(transformationMatrixLocation, matrix);
    }

    @Override
    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(projectionMatrixLocation, matrix);
    }

    @Override
    public void loadViewMatrix(Matrix4f matrix) {
        super.loadMatrix(viewMatrixLocation, matrix);
    }

    @Override
    public void loadCamera(Camera camera) {
        super.loadVector(cameraPositionLocation, camera.getPosition());
    }

    @Override
    public void loadLights(List<Light> lights) {
        int lightCount = Math.min(LIGHT_COUNT, lights.size());
        List<Vector3f> vectors = new ArrayList<>();

        for (int i = 0; i < LIGHT_COUNT; i++) {
            if (lights.size() <= i) {
                vectors.add(new Vector3f());
                vectors.add(new Vector3f());
            } else {
                vectors.add(lights.get(i).getPosition());
                vectors.add(lights.get(i).getColor());
            }
        }

        super.loadInt(lightCountLocation, lightCount);
        super.loadVectors(lightsLocation, vectors);
    }

    @Override
    public void loadReflectivity(float reflectivity) {
        super.loadFloat(reflectivityLocation, reflectivity);
    }

    @Override
    public void loadShineDamper(float shineDamper) {
        super.loadFloat(shineDamperLocation, shineDamper);
    }
}
