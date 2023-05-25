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

public class GameLoop {
    private RawModelLoader rawModelLoader;
    private ModelLoader modelLoader;
    private TextureLoader textureLoader;
    private Renderer renderer;
    private Camera camera;
    private Entity entity;
    private Light light;

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
                    .model(modelLoader.loadRawModel(rawModelLoader.loadRawModelFromFile("models/monkey.obj")))
                    .texture(textureLoader.loadTextureFromFile("textures/monkey.jpg", 1, 5))
                    .build();
        } catch (IOException ex) {
            System.exit(1);
        }

        camera = Camera.builder().build();

        renderer = Renderer.builder()
                .shaderProgram(shaderProgram)
                .camera(camera)
                .build();

        entity = Entity.builder()
                .position(new Vector3f(0f, 0f, -2f))
                .scale(new Vector3f(0.2f))
                .model(texturedModel)
                .build();

        light = Light.builder()
                .position(new Vector3f(0f, 0f, 5f))
                .color(new Vector3f(1f, 1f, 1f))
                .build();
    }

    public void loop() {
        while (!DisplayManager.windowShouldClose()) {
            camera.move();
            entity.getPosition().add(new Vector3f(0f, 0f, 0.1f * DisplayManager.getCurrentFrameTime()));
            entity.getRotation().add(new Vector3f(10f, 20f, 30f).mul(DisplayManager.getCurrentFrameTime()));

            renderer.prepare();
            renderer.render(entity, light);

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
