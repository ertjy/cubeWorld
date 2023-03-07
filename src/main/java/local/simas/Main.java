package local.simas;

import local.simas.engine.DisplayManager;
import local.simas.engine.Loader;
import local.simas.engine.RawModel;
import local.simas.engine.Renderer;
import local.simas.engine.config.WindowConfig;
import org.lwjgl.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

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

        float[] vertices = {
                // Left bottom triangle
                -0.5f, 0.5f, 0f,
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                // Right top triangle
                0.5f, -0.5f, 0f,
                0.5f, 0.5f, 0f,
                -0.5f, 0.5f, 0f
        };

        int[] indices = {
                0, 1, 3,     //top left triangle
                3, 1, 2      //bottom right triangle
        };

        RawModel model = loader.loadToVAO(vertices, indices);

        while (!DisplayManager.windowShouldClose()) {
            renderer.prepare();
            renderer.render(model);
            DisplayManager.updateWindow();
        }

        loader.cleanUp();
        DisplayManager.closeWindow();
    }
}
