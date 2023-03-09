package local.simas.cubeworld.game.shader;

import local.simas.cubeworld.engine.shader.ShaderProgram;

import java.io.IOException;

public class DefaultShaderProgram extends ShaderProgram {
    private static final String VERTEX_SHADER_FILE = "shaders/vertexShader.glsl";
    private static final String FRAGMENT_SHADER_FILE = "shaders/fragmentShader.glsl";

    public DefaultShaderProgram() throws IOException {
        super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
