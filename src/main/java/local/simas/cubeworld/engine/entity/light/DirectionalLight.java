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
public class DirectionalLight extends Light {
    @Builder.Default
    private Vector3f direction = new Vector3f(0f, -1f, 0f);

    @Override
    public LightType getType() {
        return LightType.DIRECTIONAL_LIGHT;
    }
}
