package local.simas.cubeworld.game;

import local.simas.cubeworld.engine.helper.TexturedModelHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;

@Getter
@AllArgsConstructor
public enum TexturedModelType {
    WHITE_BLOCK(0, "models/cube.obj", "textures/white.jpg", 1, 5),
    GRASS_BLOCK(1, "models/cube.obj", "textures/grass.png", 1, 5);

    private long type;
    private String modelPath;
    private String texturePath;
    float reflectivity;
    float shineDamper;

    public static void loadAll() throws IOException {
        for (TexturedModelType texturedModelType : TexturedModelType.values()) {
            TexturedModelHelper.loadTexturedModelForType(
                    texturedModelType.getType(),
                    texturedModelType.getModelPath(),
                    texturedModelType.getTexturePath(),
                    texturedModelType.getReflectivity(),
                    texturedModelType.getShineDamper()
            );
        }
    }
}
