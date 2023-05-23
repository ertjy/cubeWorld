package local.simas.cubeworld.engine.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class RawModel {
    private List<Float> positions = new ArrayList<>();
    private List<Integer> indices = new ArrayList<>();
    private List<Float> textureCoordinates = new ArrayList<>();

    public void addPosition(Vector3f position) {
        positions.add(position.x());
        positions.add(position.y());
        positions.add(position.z());
    }

    public void addTextureCoordinate(Vector2f textureCoordinate) {
        textureCoordinates.add(textureCoordinate.x());
        textureCoordinates.add(textureCoordinate.y());
    }

    public void addIndex(Integer index) {
        indices.add(index);
    }
}
