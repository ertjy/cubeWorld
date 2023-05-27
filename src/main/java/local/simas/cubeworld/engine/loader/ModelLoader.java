package local.simas.cubeworld.engine.loader;

import local.simas.cubeworld.engine.data.LoadedModel;
import local.simas.cubeworld.engine.data.RawModel;
import local.simas.cubeworld.engine.entity.Entity;
import local.simas.cubeworld.engine.helper.BufferHelper;
import org.eclipse.collections.impl.utility.LazyIterate;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_STREAM_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

public class ModelLoader {
    private final List<Integer> vaos = new ArrayList<>();
    private final List<Integer> vbos = new ArrayList<>();

    public LoadedModel loadRawModel(RawModel rawModel, int maxUsages) {
        int vaoId = createVao();
        bindVao(vaoId);

        float[] positions = LazyIterate.adapt(rawModel.getPositions())
                .collectFloat(Float::floatValue)
                .toArray();

        createAttribute(
                prepareArrayBuffer(positions),
                0,
                3
        );

        if (rawModel.getTextureCoordinates().size() > 0) {
            float[] textureCoordinates = LazyIterate.adapt(rawModel.getTextureCoordinates())
                    .collectFloat(Float::floatValue)
                    .toArray();

            createAttribute(
                    prepareArrayBuffer(textureCoordinates),
                    1,
                    2
            );
        }

        if (rawModel.getNormals().size() > 0) {
            float[] normals = LazyIterate.adapt(rawModel.getNormals())
                    .collectFloat(Float::floatValue)
                    .toArray();

            createAttribute(
                    prepareArrayBuffer(normals),
                    2,
                    3
            );
        }

        int transformationMatrixVboId = 0;

        if (maxUsages > 1) {
            transformationMatrixVboId = prepareInstancedArrayBuffer(maxUsages * 16 * 4);

            createInstancedAttribute(transformationMatrixVboId, 3, 4, 16, 0);
            createInstancedAttribute(transformationMatrixVboId, 4, 4, 16, 4);
            createInstancedAttribute(transformationMatrixVboId, 5, 4, 16, 8);
            createInstancedAttribute(transformationMatrixVboId, 6, 4, 16, 12);
        }

        int[] indices = rawModel.getIndices()
                .stream()
                .mapToInt(Integer::intValue)
                .toArray();

        prepareElementArrayBuffer(indices);

        unbindVao();

        return LoadedModel.builder()
                .vaoId(vaoId)
                .vertexCount(rawModel.getIndices().size())
                .transformationMatrixVboId(transformationMatrixVboId)
                .transformationMatrixBuffer(maxUsages == 1 ? null : BufferUtils.createFloatBuffer(16 * maxUsages))
                .maxUsages(maxUsages)
                .build();
    }

    public void updateLoadedModelWithEntities(LoadedModel loadedModel, List<Entity> entities) {
        FloatBuffer buffer = loadedModel.getTransformationMatrixBuffer();

        buffer.clear();
        for (Entity entity : entities) {
            BufferHelper.putMatrixInBuffer(entity.getTransformationMatrix(), buffer);
        }
        buffer.flip();

        bindVbo(GL_ARRAY_BUFFER, loadedModel.getTransformationMatrixVboId());

        glBufferData(GL_ARRAY_BUFFER, buffer.capacity() * 4L, GL_STREAM_DRAW);
        glBufferSubData(GL_ARRAY_BUFFER, 0, buffer);

        unbindVbo(GL_ARRAY_BUFFER);
    }

    public void cleanUp() {
        for(int vao : vaos) {
            glDeleteVertexArrays(vao);
        }

        for(int vbo : vbos) {
            glDeleteBuffers(vbo);
        }
    }

    private int prepareArrayBuffer(float[] positions) {
        int vboId = createVbo();
        bindVbo(GL_ARRAY_BUFFER, vboId);

        FloatBuffer buffer = BufferUtils.createFloatBuffer(positions.length);
        buffer.put(positions);
        buffer.flip();

        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

        unbindVbo(GL_ARRAY_BUFFER);

        return vboId;
    }

    private int prepareInstancedArrayBuffer(int byteCount) {
        int vboId = createVbo();
        bindVbo(GL_ARRAY_BUFFER, vboId);

        glBufferData(GL_ARRAY_BUFFER, byteCount, GL_STREAM_DRAW);

        unbindVbo(GL_ARRAY_BUFFER);

        return vboId;
    }

    private void createAttribute(int vboId, int attributeIndex, int coordinateSize) {
        bindVbo(GL_ARRAY_BUFFER, vboId);

        glVertexAttribPointer(attributeIndex, coordinateSize, GL_FLOAT, false, 0,0);

        unbindVbo(GL_ARRAY_BUFFER);
    }

    private void createInstancedAttribute(int vboId, int attributeIndex, int coordinateSize, int stride, int offset) {
        bindVbo(GL_ARRAY_BUFFER, vboId);

        glVertexAttribPointer(attributeIndex, coordinateSize, GL_FLOAT, false, stride * 4, offset * 4L);
        glVertexAttribDivisor(attributeIndex, 1);

        unbindVbo(GL_ARRAY_BUFFER);
    }

    private void prepareElementArrayBuffer(int[] indices) {
        int vboId = createVbo();
        bindVbo(GL_ELEMENT_ARRAY_BUFFER, vboId);

        IntBuffer buffer = BufferUtils.createIntBuffer(indices.length);
        buffer.put(indices);
        buffer.flip();

        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
    }

    private int createVao() {
        int vaoId = glGenVertexArrays();
        vaos.add(vaoId);

        return vaoId;
    }

    private void bindVao(int vaoId) {
        glBindVertexArray(vaoId);
    }

    private void unbindVao() {
        glBindVertexArray(0);
    }

    private int createVbo() {
        int vboId = glGenBuffers();
        vbos.add(vboId);

        return vboId;
    }

    private void bindVbo(int target, int vboId) {
        glBindBuffer(target, vboId);
    }

    private void unbindVbo(int target) {
        glBindBuffer(target, 0);
    }
}
