package local.simas.cubeworld.game;

import local.simas.cubeworld.engine.helper.TexturedModelHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.util.List;

@Getter
@AllArgsConstructor
public enum TexturedModelType {
    SKYBOX(0, "models/skybox.obj", List.of(
            "textures/grass.png",
            "textures/grass.png",
            "textures/grass.png",
            "textures/grass.png",
            "textures/grass.png",
            "textures/grass.png"
    ), 0, 0, 1),
    WHITE_BLOCK(1, "models/cube.obj", List.of("textures/white.jpg"), 1, 5, 1000),
    GRASS_BLOCK(2, "models/cube.obj", List.of("textures/grass.png"), 1, 5, 1000);

    private final long type;
    private final String modelPath;
    private final List<String> texturePaths;
    private final float reflectivity;
    private final float shineDamper;
    private final int maxUsages;

    public static void loadAll() throws IOException {
        for (TexturedModelType texturedModelType : TexturedModelType.values()) {
            TexturedModelHelper.loadTexturedModelForType(
                    texturedModelType.getType(),
                    texturedModelType.getModelPath(),
                    texturedModelType.getTexturePaths(),
                    texturedModelType.getReflectivity(),
                    texturedModelType.getShineDamper(),
                    texturedModelType.getMaxUsages()
            );
        }
    }
}
