package local.simas.cubeworld.engine.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.FloatBuffer;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoadedModel {
    private int vaoId;
    private int vertexCount;
    private int transformationMatrixVboId;
    private FloatBuffer transformationMatrixBuffer;
    private int maxUsages;
}
