package local.simas;

import local.simas.engine.DisplayManager;
import local.simas.engine.Loader;
import local.simas.engine.data.LoadedModel;
import local.simas.engine.Renderer;
import local.simas.engine.config.WindowConfig;
import local.simas.engine.data.RawModel;
import local.simas.engine.shaders.StaticShader;
import org.joml.Vector3f;
import org.lwjgl.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.BufferUtils.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");
        //System.out.println("OpenGL version " + glGetString(GL_VERSION));
        WindowConfig windowConfig = WindowConfig.builder()
                .width(1280)
                .height(720)
                .title("LWJGL Example")
                .shouldCenter(true)
                .build();

        DisplayManager.createWindow(windowConfig);
        DisplayManager.showWindow();

        Loader loader = new Loader();
        Renderer renderer = new Renderer();

        StaticShader shader = new StaticShader();

        RawModel rawModel = new RawModel();
        rawModel.addPosition(new Vector3f(-0.5f, 0.5f, 0f));
        rawModel.addPosition(new Vector3f(-0.5f, -0.5f, 0f));
        rawModel.addPosition(new Vector3f(0.5f, -0.5f, 0f));
        rawModel.addPosition(new Vector3f(0.5f, 0.5f, 0f));

        // bottom left triangle
        rawModel.addIndex(0);
        rawModel.addIndex(1);
        rawModel.addIndex(2);

        // top right triangle
        rawModel.addIndex(0);
        rawModel.addIndex(2);
        rawModel.addIndex(3);

        LoadedModel model = loader.loadRawModel(rawModel);

        while (!DisplayManager.windowShouldClose()) {
            renderer.prepare();
            shader.start();
            renderer.render(model);
            shader.start();
            DisplayManager.updateWindow();
        }
        shader.cleanUp();
        loader.cleanUp();
        DisplayManager.closeWindow();
    }
}
