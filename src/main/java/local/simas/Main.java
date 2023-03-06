package local.simas;

import local.simas.engine.DisplayManager;
import local.simas.engine.config.WindowConfig;
import org.lwjgl.*;

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
