package local.simas.engine.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static org.lwjgl.opengl.GL30.glBindVertexArray;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoadedModel {
    private int vaoId;
    private int vertexCount;
}
