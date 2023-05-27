package local.simas.cubeworld.game;

import local.simas.cubeworld.engine.data.TexturedModel;
import local.simas.cubeworld.engine.helper.TexturedModelHelper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.io.IOException;

@Getter
@AllArgsConstructor
public enum TexturedModelType {
    CUBE(0, "models/cube.obj", "textures/white.jpg", 1, 5),
    DRAGON(1, "models/dragon.obj", "textures/white.jpg", 1, 5);

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
