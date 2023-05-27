package local.simas.cubeworld.game;

import local.simas.cubeworld.engine.Renderer;
import local.simas.cubeworld.engine.data.TexturedModel;
import local.simas.cubeworld.engine.entity.Entity;
import local.simas.cubeworld.engine.entity.light.Light;
import local.simas.cubeworld.engine.helper.TexturedModelHelper;
import local.simas.cubeworld.game.entity.Block;
import local.simas.cubeworld.game.entity.BlockType;
import local.simas.cubeworld.game.world.Chunk;
import local.simas.cubeworld.game.world.World;
import lombok.Builder;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
public class WorldRenderer {
    private Renderer renderer;

    public void renderWorld(World world, List<Light> lights) {
        Map<TexturedModel, List<Entity>> entities = new HashMap<>();

        for (Vector3i chunkPosition : world.getChunks().keySet()) {
            Chunk chunk = world.getChunks().get(chunkPosition);

            for (BlockType blockType : chunk.getBlocks().keySet()) {
                Map<Vector3i, Block> blocks = chunk.getBlocks().get(blockType);
                TexturedModel texturedModel = TexturedModelHelper.getTexturedModelForType(blockType.getModelType().getType());
                entities.put(
                        texturedModel,
                        blocks.values()
                                .stream()
                                .map(b -> (Entity) b)
                                .toList()
                );
            }
        }

        renderer.renderEntities(entities, lights);
    }
}
