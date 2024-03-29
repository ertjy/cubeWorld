package local.simas.cubeworld.engine.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TexturedModel {
    private LoadedModel model;
    private LoadedTexture texture;
}
