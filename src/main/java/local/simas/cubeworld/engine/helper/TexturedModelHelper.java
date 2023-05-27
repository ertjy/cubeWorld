package local.simas.cubeworld.engine.helper;

import local.simas.cubeworld.engine.data.LoadedModel;
import local.simas.cubeworld.engine.data.LoadedTexture;
import local.simas.cubeworld.engine.data.RawModel;
import local.simas.cubeworld.engine.data.TexturedModel;
import local.simas.cubeworld.engine.loader.ModelLoader;
import local.simas.cubeworld.engine.loader.RawModelLoader;
import local.simas.cubeworld.engine.loader.RawTextureLoader;
import local.simas.cubeworld.engine.loader.TextureLoader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TexturedModelHelper {
    private static RawModelLoader rawModelLoader = new RawModelLoader();
    private static ModelLoader modelLoader = new ModelLoader();
    private static RawTextureLoader rawTextureLoader = new RawTextureLoader();
    private static TextureLoader textureLoader = new TextureLoader();
    private static Map<Long, TexturedModel> texturedModelMap = new HashMap<>();

    public static void setTexturedModelForType(Long type, TexturedModel texturedModel) {
        if (texturedModelMap.containsKey(type)) {
            throw new IllegalArgumentException(String.format("Textured model overriding is not allowed, fix double-setting of type '%d'", type));
        }

        texturedModelMap.put(type, texturedModel);
    }

    public static void loadTexturedModelForType(Long type, String modelPath, List<String> texturePaths, float reflectivity, float shineDamper, int maxUsages) throws IOException {
        RawModel rawModel = rawModelLoader.loadRawModelFromFile(modelPath);
        LoadedModel loadedModel = modelLoader.loadRawModel(rawModel, maxUsages);

        List<BufferedImage> rawTextures = new ArrayList<>();

        for (String texturePath : texturePaths) {
            rawTextures.add(rawTextureLoader.loadRawTextureFromFile(texturePath));
        }

        LoadedTexture loadedTexture;

        switch (rawTextures.size()) {
            case 1 -> {
                loadedTexture = textureLoader.loadRawTexture(rawTextures.get(0), reflectivity, shineDamper);
            }
            case 6 -> {
                loadedTexture = textureLoader.loadRawTexturesForCubeMap(rawTextures);
            }
            default -> throw new IllegalStateException(String.format("Can't load textured model with %d textures", rawTextures.size()));
        }

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

    public static void cleanUp() {
        textureLoader.cleanUp();
        modelLoader.cleanUp();
    }
}
