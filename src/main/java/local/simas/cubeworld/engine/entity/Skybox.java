package local.simas.cubeworld.engine.entity;

import local.simas.cubeworld.engine.data.TexturedModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Skybox {
    private TexturedModel model;
}
