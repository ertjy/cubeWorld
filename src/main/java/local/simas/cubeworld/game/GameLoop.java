package local.simas.cubeworld.game;

import local.simas.cubeworld.engine.DisplayManager;
import local.simas.cubeworld.engine.Renderer;
import local.simas.cubeworld.engine.config.WindowConfig;
import local.simas.cubeworld.engine.data.TexturedModel;
import local.simas.cubeworld.engine.entities.Camera;
import local.simas.cubeworld.engine.entities.Entity;
import local.simas.cubeworld.engine.entities.Light;
import local.simas.cubeworld.engine.loader.ModelLoader;
import local.simas.cubeworld.engine.loader.RawModelLoader;
import local.simas.cubeworld.engine.loader.TextureLoader;
import local.simas.cubeworld.game.shader.DefaultShaderProgram;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameLoop {
    private RawModelLoader rawModelLoader;
    private ModelLoader modelLoader;
    private TextureLoader textureLoader;
    private Renderer renderer;
    private Camera camera;
    private List<Entity> entities;
    private List<Light> lights;

    public GameLoop(WindowConfig windowConfig) {
        DisplayManager.createWindow(windowConfig);
        DisplayManager.showWindow();

        rawModelLoader = new RawModelLoader();
        modelLoader = new ModelLoader();
        textureLoader = new TextureLoader();
        DefaultShaderProgram shaderProgram = null;
        TexturedModel texturedModel = null;

        try {
            shaderProgram = new DefaultShaderProgram();

            texturedModel = TexturedModel.builder()
                    .model(modelLoader.loadRawModel(rawModelLoader.loadRawModelFromFile("models/dragon.obj")))
                    .texture(textureLoader.loadTextureFromFile("textures/white.jpg", 1, 5))
                    .build();
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

        entities.add(
                Entity.builder()
                        .position(new Vector3f(0f, 0f, 0f))
                        .scale(new Vector3f(0.1f))
                        .model(texturedModel)
                        .build()
        );

        lights.add(
                Light.builder()
                        .position(new Vector3f(5f, 0f, 0f))
                        .color(new Vector3f(1f, 0f, 0f))
                        .build()
        );

        lights.add(
                Light.builder()
                        .position(new Vector3f(-5f, 0f, 0f))
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
        textureLoader.cleanUp();
        modelLoader.cleanUp();
        renderer.cleanUp();
        DisplayManager.closeWindow();
    }
}
