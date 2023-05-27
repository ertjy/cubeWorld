package local.simas.cubeworld.game;

import local.simas.cubeworld.engine.Renderer;
import local.simas.cubeworld.engine.entity.light.Light;
import local.simas.cubeworld.game.entity.Block;
import local.simas.cubeworld.game.world.Chunk;
import local.simas.cubeworld.game.world.World;
import lombok.Builder;
import org.joml.Vector3i;

import java.util.List;

@Builder
public class WorldRenderer {
    private Renderer renderer;

    public void renderWorld(World world, List<Light> lights) {
        for (Vector3i chunkPosition : world.getChunks().keySet()) {
            Chunk chunk = world.getChunks().get(chunkPosition);

            for (Vector3i blockPosition : chunk.getBlocks().keySet()) {
                Block block = chunk.getBlocks().get(blockPosition);
                renderer.renderEntity(block, lights);
            }
        }
    }
}
