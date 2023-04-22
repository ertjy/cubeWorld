package local.simas.cubeworld.game;

import local.simas.cubeworld.engine.DisplayManager;
import local.simas.cubeworld.engine.ModelLoader;
import local.simas.cubeworld.engine.data.LoadedModel;
import local.simas.cubeworld.engine.Renderer;
import local.simas.cubeworld.engine.config.WindowConfig;
import local.simas.cubeworld.engine.data.RawModel;
import local.simas.cubeworld.engine.data.TexturedModel;
import local.simas.cubeworld.engine.shader.ShaderProgram;
import local.simas.cubeworld.engine.textures.ModelTexture;
import local.simas.cubeworld.game.shader.DefaultShaderProgram;
import org.joml.Vector3f;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        WindowConfig windowConfig = WindowConfig.builder()
                .width(1280)
                .height(720)
                .title("LWJGL Example")
                .shouldCenter(true)
                .build();

        DisplayManager.createWindow(windowConfig);
        DisplayManager.showWindow();

        ShaderProgram shaderProgram = null;

        try {
            shaderProgram = new DefaultShaderProgram();
        } catch (IOException ex) {
            System.exit(1);
        }

        Renderer renderer = Renderer.builder()
                .shaderProgram(shaderProgram)
                .build();

        ModelLoader modelLoader = new ModelLoader();
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

        LoadedModel model = modelLoader.loadRawModel(rawModel);

        ModelTexture texture = new ModelTexture(modelLoader.loadTexture("image"));
        TexturedModel texturedModel = new TexturedModel(rawModel, texture);



        while (!DisplayManager.windowShouldClose()) {
            renderer.prepare();
            renderer.render(texturedModel, model);
            DisplayManager.updateWindow();
        }

        modelLoader.cleanUp();
        renderer.cleanUp();
        DisplayManager.closeWindow();
    }
}
