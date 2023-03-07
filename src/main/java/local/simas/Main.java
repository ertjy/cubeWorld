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

        RawModel model = loader.loadToVAO(vertices);

        while (!DisplayManager.windowShouldClose()) {
            //either renderer.render is not rendering the triangles or updateWindow is not updating the window correctly
            renderer.prepare();
            renderer.render(model);
            DisplayManager.updateWindow();
            //System.out.println("window updated");
        }

        loader.cleanUp();
        DisplayManager.closeWindow();
    }
}
