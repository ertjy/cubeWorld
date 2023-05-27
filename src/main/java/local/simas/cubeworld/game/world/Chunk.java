package local.simas.cubeworld.game.world;

import local.simas.cubeworld.game.entity.Block;
import local.simas.cubeworld.game.entity.BlockType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chunk {
    @Builder.Default
    private Map<BlockType, Map<Vector3i, Block>> blocks = new HashMap<>();

    public void saveToFile() {

    }

    public static Chunk loadFromFile(String fileName) {
        return generate(new Vector3i(0, 0, 0));
    }

    public static Chunk generate(Vector3i chunkPosition) {
        Map<BlockType, Map<Vector3i, Block>> blocks = new HashMap<>();

        for (int x = 0; x < World.CHUNK_SIZE; x++) {
            for (int z = 0; z < World.CHUNK_SIZE; z++) {
                for (int y = 0; y < World.WORLD_HEIGHT; y++) {
                    Vector3i blockPosition = new Vector3i(x, y, z);

                    Block block = generateBlock(blockPosition);

                    if (!blocks.containsKey(block.getBlockType())) {
                        blocks.put(block.getBlockType(), new HashMap<>());
                    }

                    blocks.get(block.getBlockType()).put(blockPosition, block);
                }
            }
        }

        return Chunk.builder()
                .blocks(blocks)
                .build();
    }

    private static Block generateBlock(Vector3i blockPosition) {
        return Block.builder()
                .blockType(BlockType.GRASS)
                .position(new Vector3f(blockPosition.x(), blockPosition.y(), blockPosition.z()))
                .build();
    }
}
