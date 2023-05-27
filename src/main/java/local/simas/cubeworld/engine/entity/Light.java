package local.simas.cubeworld.engine.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joml.Vector3f;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Light {
    @Builder.Default
    private Vector3f position = new Vector3f(0f, 0f, 0f);

    @Builder.Default
    private Vector3f color = new Vector3f(1f, 1f, 1f);
}
