package local.simas.cubeworld.engine.shader;

import org.joml.Matrix4f;

import java.io.IOException;

public abstract class SkyboxShader extends Shader {
    public SkyboxShader(String vertexFile, String fragmentFile) throws IOException {
        super(vertexFile, fragmentFile);
    }

    public abstract void loadProjectionMatrix(Matrix4f projectionMatrix);
    public abstract void loadViewMatrix(Matrix4f viewMatrix);
}
