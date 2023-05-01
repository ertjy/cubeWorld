package local.simas.cubeworld.game;

import local.simas.cubeworld.engine.DisplayManager;
import local.simas.cubeworld.engine.entities.Camera;
import local.simas.cubeworld.engine.entities.Entity;
import local.simas.cubeworld.engine.loader.ModelLoader;
import local.simas.cubeworld.engine.Renderer;
import local.simas.cubeworld.engine.config.WindowConfig;
import local.simas.cubeworld.engine.data.RawModel;
import local.simas.cubeworld.engine.data.TexturedModel;
import local.simas.cubeworld.engine.loader.TextureLoader;
import local.simas.cubeworld.game.shader.DefaultShaderProgram;
import org.joml.Vector3f;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        WindowConfig windowConfig = WindowConfig.builder()
                .width(720)
                .height(720)
                .title("LWJGL Example")
                .shouldCenter(true)
                .build();

        DisplayManager.createWindow(windowConfig);
        DisplayManager.showWindow();

        DefaultShaderProgram shaderProgram = null;

        try {
            shaderProgram = new DefaultShaderProgram();
        } catch (IOException ex) {
            System.exit(1);
        }

        Camera camera = Camera.builder().build();

        Renderer renderer = Renderer.builder()
                .shaderProgram(shaderProgram)
                .camera(camera)
                .build();

        ModelLoader modelLoader = new ModelLoader();
        TextureLoader textureLoader = new TextureLoader();

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

        float[] textureCoords = {
                0,0,    //V0
                0,1,    //V1
                1,1,    //V2
                1,0,    //V3
        };

        TexturedModel texturedModel = TexturedModel.builder()
                .model(modelLoader.loadRawModel(rawModel, textureCoords))
                .texture(textureLoader.loadTexture("res/image.png"))
                .build();

        Entity entity = Entity.builder()
                .position(new Vector3f(0, 0, -2))
                .model(texturedModel)
                .build();

        while (!DisplayManager.windowShouldClose()) {
            camera.move();
            entity.getPosition().add(new Vector3f(0f, 0f, 0.1f * DisplayManager.getCurrentFrameTime()));
            entity.getRotation().add(new Vector3f(10f, 20f, 30f).mul(DisplayManager.getCurrentFrameTime()));

            renderer.prepare();
            renderer.render(entity);

            DisplayManager.updateWindow();
        }

        textureLoader.cleanUp();
        modelLoader.cleanUp();
        renderer.cleanUp();
        DisplayManager.closeWindow();
    }
}
