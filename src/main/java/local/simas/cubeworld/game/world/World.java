package local.simas.cubeworld.game.world;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joml.Vector3i;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class World {
    public static Integer CHUNK_SIZE = 16;
    public static Integer WORLD_HEIGHT = 256;

    @Builder.Default
    private Map<Vector3i, Chunk> chunks = new HashMap<>();
}
