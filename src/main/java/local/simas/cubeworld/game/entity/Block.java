package local.simas.cubeworld.game.entity;

import local.simas.cubeworld.engine.entity.Entity;
import local.simas.cubeworld.engine.helper.TexturedModelHelper;
import lombok.Builder;
import org.joml.Vector3f;

public class Block extends Entity {
    @Builder
    public Block(BlockType blockType, Vector3f position, Vector3f rotation, Vector3f scale) {
        super(
                TexturedModelHelper.getTexturedModelForType(blockType.getModelType().getType()),
                position == null ? new Vector3f(0f, 0f, 0f) : position,
                rotation == null ? new Vector3f(0f, 0f, 0f) : rotation,
                scale == null ? new Vector3f(1f, 1f, 1f) : scale
        );
    }
}
