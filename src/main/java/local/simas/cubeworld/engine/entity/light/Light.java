package local.simas.cubeworld.engine.entity.light;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.joml.Vector3f;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class Light {
    @Builder.Default
    private Vector3f color = new Vector3f(1f, 1f, 1f);

    @Builder.Default
    private float ambientBrightness = 0.1f;

    public abstract LightType getType();
}
