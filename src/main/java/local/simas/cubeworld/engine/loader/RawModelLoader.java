package local.simas.cubeworld.engine.loader;

import de.javagl.obj.FloatTuple;
import de.javagl.obj.Obj;
import de.javagl.obj.ObjFace;
import de.javagl.obj.ObjReader;
import de.javagl.obj.ObjUtils;
import local.simas.cubeworld.engine.data.RawModel;
import local.simas.cubeworld.engine.helper.FileHelper;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.IOException;
import java.io.InputStream;

public class RawModelLoader {
    public RawModel loadRawModelFromFile(String fileName) throws IOException {
        Obj object = null;

        try (InputStream inputStream = FileHelper.getResourceAsStream(fileName)) {
            object = ObjUtils.convertToRenderable(ObjReader.read(inputStream));
        }

        if (object.getNumVertices() != object.getNumTexCoords()) {
            System.out.println("dicks");
            throw new IllegalStateException("Vertex count and texture coordinate count do not match");
        }

        RawModel model = new RawModel();

        int vertexId = 0;
        int textureCoordinateId = 0;

        while (vertexId < object.getNumVertices() && textureCoordinateId < object.getNumTexCoords()) {
            FloatTuple positionTuple = object.getVertex(vertexId);
            FloatTuple textureCoordinateTuple = object.getTexCoord(textureCoordinateId);
            model.addPosition(
                    new Vector3f(positionTuple.getX(), positionTuple.getY(), positionTuple.getZ()),
                    new Vector2f(textureCoordinateTuple.getX(), textureCoordinateTuple.getY())
            );
            vertexId++;
            textureCoordinateId++;
        }

        for (int faceId = 0; faceId < object.getNumFaces(); faceId++) {
            ObjFace face = object.getFace(faceId);

            for (int faceVertexId = 0; faceVertexId < face.getNumVertices(); faceVertexId++) {
                model.addIndex(face.getVertexIndex(faceVertexId));
            }
        }

        return model;
    }
}
