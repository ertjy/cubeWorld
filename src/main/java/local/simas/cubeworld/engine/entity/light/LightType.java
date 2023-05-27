package local.simas.cubeworld.engine.entity.light;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LightType {
    SPOT_LIGHT(0),
    DIRECTIONAL_LIGHT(1);

    private int type;
}
