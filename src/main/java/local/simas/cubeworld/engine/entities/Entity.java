package local.simas.cubeworld.engine.entities;

import local.simas.cubeworld.engine.data.TexturedModel;
import local.simas.cubeworld.engine.helper.MathHelper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joml.Matrix4f;
import org.joml.Vector3f;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Entity {
    private TexturedModel model;

    @Builder.Default
    private Vector3f position = new Vector3f(0f, 0f, 0f);

    @Builder.Default
    private Vector3f rotation = new Vector3f(0f, 0f, 0f);

    @Builder.Default
    private Vector3f scale = new Vector3f(1f, 1f, 1f);

    public Matrix4f getTransformationMatrix() {
        return MathHelper.createTransformationMatrix(position, rotation, scale);
    }
}
