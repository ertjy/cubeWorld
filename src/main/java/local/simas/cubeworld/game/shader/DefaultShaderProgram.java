package local.simas.cubeworld.game.shader;

import local.simas.cubeworld.engine.shader.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.IOException;

public class DefaultShaderProgram extends ShaderProgram {
    private static final String VERTEX_SHADER_FILE = "shaders/vertexShader.glsl";
    private static final String FRAGMENT_SHADER_FILE = "shaders/fragmentShader.glsl";

    private int transformationMatrixLocation;
    private int projectionMatrixLocation;
    private int viewMatrixLocation;
    private int lightPositionLocation;
    private int lightColorLocation;

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
        transformationMatrixLocation = super.getUniformFromLocation("transformationMatrix");
        projectionMatrixLocation = super.getUniformFromLocation("projectionMatrix");
        viewMatrixLocation = super.getUniformFromLocation("viewMatrix");
        lightPositionLocation = super.getUniformFromLocation("lightPosition");
        lightColorLocation = super.getUniformFromLocation("lightColor");
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
    public void loadLightPosition(Vector3f lightPosition) {
        super.loadVector(lightPositionLocation, lightPosition);
    }

    @Override
    public void loadLightColor(Vector3f lightColor) {
        super.loadVector(lightColorLocation, lightColor);
    }
}
