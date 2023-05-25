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
        Obj object;

        try (InputStream inputStream = FileHelper.getResourceAsStream(fileName)) {
            object = ObjUtils.convertToRenderable(ObjReader.read(inputStream));
        }

        RawModel model = new RawModel();
        float[] textureCoordinates = new float[object.getNumVertices() * 2];
        float[] normals = new float[object.getNumVertices() * 3];

        for (int vertexId = 0; vertexId < object.getNumVertices(); vertexId++) {
            FloatTuple positionTuple = object.getVertex(vertexId);
            model.addPosition(new Vector3f(positionTuple.getX(), positionTuple.getY(), positionTuple.getZ()));
        }

        for (int faceId = 0; faceId < object.getNumFaces(); faceId++) {
            ObjFace face = object.getFace(faceId);

            for (int faceVertexId = 0; faceVertexId < face.getNumVertices(); faceVertexId++) {
                int vertexId = face.getVertexIndex(faceVertexId);
                int textureCoordinateId = face.getTexCoordIndex(faceVertexId);
                int normalId = face.getNormalIndex(faceVertexId);

                FloatTuple textureCoordinateTuple = object.getTexCoord(textureCoordinateId);
                textureCoordinates[vertexId * 2] = textureCoordinateTuple.getX();
                textureCoordinates[vertexId * 2 + 1] = textureCoordinateTuple.getY();

                FloatTuple normalTuple = object.getNormal(normalId);
                normals[vertexId * 3] = normalTuple.getX();
                normals[vertexId * 3 + 1] = normalTuple.getY();
                normals[vertexId * 3 + 2] = normalTuple.getZ();

                model.addIndex(vertexId);
            }
        }

        for (int vertexId = 0; vertexId < object.getNumVertices(); vertexId++) {
            float textureCoordinateX = textureCoordinates[vertexId * 2];
            float textureCoordinateY = 1.0f - textureCoordinates[vertexId * 2 + 1];

            float normalX = normals[vertexId * 3];
            float normalY = normals[vertexId * 3 + 1];
            float normalZ = normals[vertexId * 3 + 2];

            model.addTextureCoordinate(new Vector2f(textureCoordinateX, textureCoordinateY));
            model.addNormal(new Vector3f(normalX, normalY, normalZ));
        }

        return model;
    }
}
