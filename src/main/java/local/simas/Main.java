package local.simas;

import local.simas.engine.DisplayManager;
import local.simas.engine.config.WindowConfig;
import org.lwjgl.*;

import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawPixels;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        WindowConfig windowConfig = WindowConfig.builder()
                .width(600)
                .height(400)
                .title("LWJGL Example")
                .shouldCenter(true)
                .build();

        DisplayManager.createWindow(windowConfig);
        DisplayManager.showWindow();

        while (!DisplayManager.windowShouldClose()) {

            DisplayManager.updateWindow();
        }

        DisplayManager.closeWindow();
    }
}
