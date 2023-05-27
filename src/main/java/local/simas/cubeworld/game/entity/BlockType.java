package local.simas.cubeworld.game.entity;

import local.simas.cubeworld.game.TexturedModelType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BlockType {
    AIR(null),
    WHITE(TexturedModelType.WHITE_BLOCK),
    GRASS(TexturedModelType.GRASS_BLOCK);

    private final TexturedModelType modelType;
}
