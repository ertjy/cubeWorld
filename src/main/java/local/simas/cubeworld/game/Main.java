package local.simas.cubeworld.game;

import local.simas.cubeworld.engine.config.WindowConfig;

public class Main {
    public static void main(String[] args) {
        WindowConfig windowConfig = WindowConfig.builder()
                .width(720)
                .height(720)
                .title("LWJGL Example")
                .shouldCenter(true)
                .build();

        GameLoop gameLoop = new GameLoop(windowConfig);
        gameLoop.loop();
        gameLoop.cleanUp();
    }
}
