package local.simas.cubeworld.game.shader;

import local.simas.cubeworld.engine.shader.ShaderProgram;
import org.joml.Matrix4f;

import java.io.IOException;

public class DefaultShaderProgram extends ShaderProgram {
    private static final String VERTEX_SHADER_FILE = "shaders/vertexShader.glsl";
    private static final String FRAGMENT_SHADER_FILE = "shaders/fragmentShader.glsl";

    private int transformationMatrixLocation;

    public DefaultShaderProgram() throws IOException {
        super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }

    @Override
    protected void getAllUniformLocations() {
        transformationMatrixLocation = super.getUniformFromLocation("transformationMatrix");
    }

    @Override
    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(transformationMatrixLocation, matrix);
    }
}
