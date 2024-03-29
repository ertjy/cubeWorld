package local.simas.cubeworld.engine.shader;

import local.simas.cubeworld.engine.entity.Camera;
import local.simas.cubeworld.engine.entity.light.Light;
import local.simas.cubeworld.engine.helper.BufferHelper;
import local.simas.cubeworld.engine.helper.FileHelper;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.io.*;
import java.nio.FloatBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public abstract class Shader {
    private int programId;
    private int vertexShaderId;
    private int fragmentShaderId;

    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    public Shader(String vertexFile, String fragmentFile) throws IOException {
        vertexShaderId = loadShader(vertexFile, GL_VERTEX_SHADER);
        fragmentShaderId = loadShader(fragmentFile, GL_FRAGMENT_SHADER);

        programId = glCreateProgram();
        glAttachShader(programId, vertexShaderId);
        glAttachShader(programId, fragmentShaderId);

        bindAttributes();

        glLinkProgram(programId);
        glValidateProgram(programId);
        getAllUniformLocations();
    }

    protected abstract void getAllUniformLocations();

    protected int getUniformLocation(String uniformName) {
        return glGetUniformLocation(programId, uniformName);
    }

    public void start(){
        glUseProgram(programId);
    }

    public void stop(){
        glUseProgram(0);
    }

    public void cleanUp() {
        stop();

        glDetachShader(programId, vertexShaderId);
        glDetachShader(programId, fragmentShaderId);

        glDeleteShader(vertexShaderId);
        glDeleteShader(fragmentShaderId);
        glDeleteProgram(programId);
    }

    protected abstract void bindAttributes();

    protected void bindAttribute(int attribute, String name) {
        glBindAttribLocation(programId, attribute, name);
    }

    protected void loadInt(int location, int value) {
        glUniform1i(location, value);
    }

    protected void loadFloat(int location, float value) {
        glUniform1f(location, value);
    }

    protected void loadVector(int location, Vector3f vector) {
        glUniform3f(location, vector.x, vector.y, vector.z);
    }

    protected void loadVectors(int location, List<Vector3f> vectors) {
        FloatBuffer vectorBuffer = BufferUtils.createFloatBuffer(3 * vectors.size());

        for (Vector3f vector : vectors) {
            BufferHelper.putVectorInBuffer(vector, vectorBuffer);
        }

        vectorBuffer.flip();

        glUniform3fv(location, vectorBuffer);
    }

    protected void loadBoolean(int location, boolean value) {
        int valueToLoad = 0;

        if (value) {
            valueToLoad = 1;
        }

        glUniform1i(location, valueToLoad);
    }

    protected void loadMatrix(int location, Matrix4f matrix) {
        BufferHelper.putMatrixInBuffer(matrix, matrixBuffer);
        matrixBuffer.flip();
        glUniformMatrix4fv(location, false, matrixBuffer);
    }

    private int loadShader(String filename, int type) throws IOException {
        String shaderSource = FileHelper.getResourceAsString(filename);

        int shaderId = glCreateShader(type);
        glShaderSource(shaderId, shaderSource);
        glCompileShader(shaderId);

        System.out.println(glGetShaderInfoLog(shaderId,GL20.glGetShaderi(shaderId,GL20.GL_INFO_LOG_LENGTH)));
        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new IllegalStateException("Failed to compile shader");
        }

        return shaderId;
    }
}
