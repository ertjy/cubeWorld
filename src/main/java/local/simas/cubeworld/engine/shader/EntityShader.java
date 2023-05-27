package local.simas.cubeworld.engine.shader;

import local.simas.cubeworld.engine.entity.Camera;
import local.simas.cubeworld.engine.entity.light.Light;
import org.joml.Matrix4f;

import java.io.IOException;
import java.util.List;

public abstract class EntityShader extends Shader {
    public EntityShader(String vertexFile, String fragmentFile) throws IOException {
        super(vertexFile, fragmentFile);
    }

    public abstract void loadProjectionMatrix(Matrix4f projectionMatrix);
    public abstract void loadViewMatrix(Matrix4f viewMatrix);
    public abstract void loadCamera(Camera camera);
    public abstract void loadLights(List<Light> lights);
    public abstract void loadReflectivity(float reflectivity);
    public abstract void loadShineDamper(float shineDamper);
}
