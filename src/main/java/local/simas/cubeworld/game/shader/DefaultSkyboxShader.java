package local.simas.cubeworld.game.shader;

import local.simas.cubeworld.engine.shader.SkyboxShader;
import org.joml.Matrix4f;

import java.io.IOException;

public class DefaultSkyboxShader extends SkyboxShader {
    private static final String VERTEX_SHADER_FILE = "shaders/skyboxVertexShader.glsl";
    private static final String FRAGMENT_SHADER_FILE = "shaders/skyboxFragmentShader.glsl";

    private int projectionMatrixLocation;
    private int viewMatrixLocation;

    public DefaultSkyboxShader() throws IOException {
        super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

    @Override
    protected void getAllUniformLocations() {
        projectionMatrixLocation = super.getUniformLocation("projectionMatrix");
        viewMatrixLocation = super.getUniformLocation("viewMatrix");
    }

    @Override
    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(projectionMatrixLocation, matrix);
    }

    @Override
    public void loadViewMatrix(Matrix4f matrix) {
        super.loadMatrix(viewMatrixLocation, matrix);
    }
}
