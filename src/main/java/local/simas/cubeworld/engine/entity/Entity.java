package local.simas.cubeworld.engine.entity;

import local.simas.cubeworld.engine.data.TexturedModel;
import local.simas.cubeworld.engine.helper.MathHelper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joml.Matrix4f;
import org.joml.Vector3f;

@Data
@Builder(builderMethodName = "entityBuilder")
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

    private final Matrix4f transformationMatrix = new Matrix4f();

    public Matrix4f getTransformationMatrix() {
        MathHelper.updateTransformationMatrix(this, transformationMatrix);
        return transformationMatrix;
    }
}
