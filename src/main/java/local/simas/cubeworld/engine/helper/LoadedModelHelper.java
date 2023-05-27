package local.simas.cubeworld.engine.helper;

import local.simas.cubeworld.engine.data.LoadedModel;
import local.simas.cubeworld.engine.data.LoadedTexture;
import local.simas.cubeworld.engine.data.RawModel;
import local.simas.cubeworld.engine.data.TexturedModel;
import local.simas.cubeworld.engine.loader.ModelLoader;
import local.simas.cubeworld.engine.loader.RawModelLoader;
import local.simas.cubeworld.engine.loader.TextureLoader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoadedModelHelper {
    private static RawModelLoader rawModelLoader = new RawModelLoader();
    private static ModelLoader modelLoader = new ModelLoader();
    private static TextureLoader textureLoader = new TextureLoader();
    private static Map<Long, TexturedModel> texturedModelMap = new HashMap<>();

    public static void setTexturedModelForType(Long type, TexturedModel texturedModel) {
        if (texturedModelMap.containsKey(type)) {
            throw new IllegalArgumentException(String.format("Textured model overriding is not allowed, fix double-setting of type '%d'", type));
        }

        texturedModelMap.put(type, texturedModel);
    }

    public static void loadLoadedModelForType(Long type, String modelPath, String texturePath, float reflectivity, float shineDamper) throws IOException {
        RawModel rawModel = rawModelLoader.loadRawModelFromFile(modelPath);
        LoadedModel loadedModel = modelLoader.loadRawModel(rawModel);

        LoadedTexture loadedTexture = textureLoader.loadTextureFromFile(texturePath, reflectivity, shineDamper);

        setTexturedModelForType(
                type,
                TexturedModel.builder()
                        .model(loadedModel)
                        .texture(loadedTexture)
                        .build()
        );
    }

    public static TexturedModel getTexturedModelForType(Long type) {
        if (!texturedModelMap.containsKey(type)) {
            throw new IllegalArgumentException(String.format("Textured model is not present in the map, set or load textured model of type '%d'", type));
        }

        return texturedModelMap.get(type);
    }
}
