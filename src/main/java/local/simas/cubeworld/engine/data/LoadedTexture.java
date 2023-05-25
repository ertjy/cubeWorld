package local.simas.cubeworld.engine.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoadedTexture {
    private int textureId;
    private float reflectivity;
    private float shineDamper;
}
