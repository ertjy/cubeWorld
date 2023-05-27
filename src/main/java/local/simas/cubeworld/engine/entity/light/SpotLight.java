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
public class SpotLight extends Light {
    @Builder.Default
    private Vector3f position = new Vector3f(1f, 1f, 1f);

    @Override
    public LightType getType() {
        return LightType.SPOT_LIGHT;
    }
}
