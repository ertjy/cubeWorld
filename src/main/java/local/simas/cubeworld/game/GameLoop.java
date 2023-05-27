package local.simas.cubeworld.game;

import local.simas.cubeworld.engine.DisplayManager;
import local.simas.cubeworld.engine.Renderer;
import local.simas.cubeworld.engine.config.WindowConfig;
import local.simas.cubeworld.engine.entity.Camera;
import local.simas.cubeworld.engine.entity.Entity;
import local.simas.cubeworld.engine.entity.Light;
import local.simas.cubeworld.engine.helper.TexturedModelHelper;
import local.simas.cubeworld.game.entity.Block;
import local.simas.cubeworld.game.shader.DefaultShaderProgram;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameLoop {
    private Renderer renderer;
    private Camera camera;
    private List<Entity> entities;
    private List<Light> lights;

    public GameLoop(WindowConfig windowConfig) {
        DisplayManager.createWindow(windowConfig);
        DisplayManager.showWindow();

        DefaultShaderProgram shaderProgram = null;

        try {
            shaderProgram = new DefaultShaderProgram();
            TexturedModelType.loadAll();
        } catch (IOException ex) {
            System.exit(1);
        }

        camera = Camera.builder()
                .position(new Vector3f(0f, 0f, 0f))
                .rotation(new Vector3f(0f, 0f, 0f))
                .build();

        renderer = Renderer.builder()
                .shaderProgram(shaderProgram)
                .camera(camera)
                .build();

        entities = new ArrayList<>();
        lights = new ArrayList<>();

        for (int x = 0; x <= 10; x++) {
            entities.add(
                    Block.builder()
                            .texturedModelType(TexturedModelType.WHITE_BLOCK)
                            .position(new Vector3f(x, 0f, 0f))
                            .build()
            );
        }

        for (int y = 0; y <= 10; y += 2) {
            entities.add(
                    Block.builder()
                            .texturedModelType(TexturedModelType.GRASS_BLOCK)
                            .position(new Vector3f(0f, y, 0f))
                            .build()
            );
        }

        for (int z = 0; z <= 10; z += 5) {
            entities.add(
                    Block.builder()
                            .texturedModelType(TexturedModelType.GRASS_BLOCK)
                            .position(new Vector3f(0f, 0f, z))
                            .build()
            );
        }

        lights.add(
                Light.builder()
                        .position(new Vector3f(5f, 0f, 0f))
                        .color(new Vector3f(1f, 0f, 0f))
                        .build()
        );

        lights.add(
                Light.builder()
                        .position(new Vector3f(0f, 5f, 0f))
                        .color(new Vector3f(0f, 1f, 0f))
                        .build()
        );

        lights.add(
                Light.builder()
                        .position(new Vector3f(0f, 0f, 5f))
                        .color(new Vector3f(0f, 0f, 1f))
                        .build()
        );
    }

    public void loop() {
        while (!DisplayManager.windowShouldClose()) {
            camera.move();

            renderer.prepare();

            for (Entity entity : entities) {
                renderer.render(entity, lights);
            }

            DisplayManager.updateWindow();
        }
    }

    public void cleanUp() {
        renderer.cleanUp();
        TexturedModelHelper.cleanUp();
        DisplayManager.closeWindow();
    }
}
