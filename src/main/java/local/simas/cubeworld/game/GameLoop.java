package local.simas.cubeworld.game;

import local.simas.cubeworld.engine.DisplayManager;
import local.simas.cubeworld.engine.Renderer;
import local.simas.cubeworld.engine.config.WindowConfig;
import local.simas.cubeworld.engine.entity.Camera;
import local.simas.cubeworld.engine.entity.Entity;
import local.simas.cubeworld.engine.entity.Skybox;
import local.simas.cubeworld.engine.entity.light.DirectionalLight;
import local.simas.cubeworld.engine.entity.light.Light;
import local.simas.cubeworld.engine.helper.TexturedModelHelper;
import local.simas.cubeworld.game.entity.Block;
import local.simas.cubeworld.game.entity.BlockType;
import local.simas.cubeworld.game.shader.DefaultEntityShader;
import local.simas.cubeworld.game.shader.DefaultSkyboxShader;
import local.simas.cubeworld.game.world.Chunk;
import local.simas.cubeworld.game.world.World;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameLoop {
    private Renderer renderer;
    private WorldRenderer worldRenderer;
    private Camera camera;
    private Skybox skybox;
    private World world;
    private List<Entity> entities;
    private List<Light> lights;

    public GameLoop(WindowConfig windowConfig) {
        DisplayManager.createWindow(windowConfig);
        DisplayManager.showWindow();

        DefaultEntityShader entityShader = null;
        DefaultSkyboxShader skyboxShader = null;

        try {
            entityShader = new DefaultEntityShader();
            skyboxShader = new DefaultSkyboxShader();
            TexturedModelType.loadAll();
        } catch (IOException ex) {
            System.exit(1);
        }

        camera = Camera.builder()
                .position(new Vector3f(0f, 0f, 0f))
                .rotation(new Vector3f(0f, 0f, 0f))
                .build();

        renderer = Renderer.builder()
                .entityShader(entityShader)
                .skyboxShader(skyboxShader)
                .camera(camera)
                .build();

        worldRenderer = WorldRenderer.builder()
                .renderer(renderer)
                .build();

        skybox = Skybox.builder()
                .model(TexturedModelHelper.getTexturedModelForType(TexturedModelType.SKYBOX.getType()))
                .build();

        world = World.builder()
                .chunks(Map.of(
                        new Vector3i(0, 0, 0),
                        Chunk.generate(new Vector3i(0, 0, 0))
                ))
                .build();

        entities = new ArrayList<>();
        lights = new ArrayList<>();

        lights.add(
                DirectionalLight.builder()
                        .direction(new Vector3f(-1f, -1f, -1f))
                        .color(new Vector3f(1f, 1f, 1f))
                        .build()
        );
    }

    public void loop() {
        while (!DisplayManager.windowShouldClose()) {
            camera.move();

            renderer.prepare();

            renderer.renderSkybox(skybox);

            for (Entity entity : entities) {
                renderer.renderEntity(entity, lights);
            }

            worldRenderer.renderWorld(world, lights);

            DisplayManager.updateWindow();
        }
    }

    public void cleanUp() {
        renderer.cleanUp();
        TexturedModelHelper.cleanUp();
        DisplayManager.closeWindow();
    }
}
