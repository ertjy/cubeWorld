package local.simas.cubeworld.engine;

import local.simas.cubeworld.engine.data.LoadedModel;
import local.simas.cubeworld.engine.data.RawModel;
import org.eclipse.collections.impl.utility.LazyIterate;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_load;

public class ModelLoader {
    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();
    private List<Integer> textures = new ArrayList<Integer>();

    public LoadedModel loadRawModel(RawModel rawModel, float[] textureCoords) {
        int vaoId = createVao();

        float[] positions = LazyIterate.adapt(rawModel.getPositions())
                .collectFloat(Float::floatValue)
                .toArray();

        int[] indices = rawModel.getIndices()
                .stream()
                .mapToInt(Integer::intValue)
                .toArray();

        bindPositionBuffer(0, 3, positions);
        bindPositionBuffer(1, 2, textureCoords);
        bindIndexBuffer(indices);

        glBindVertexArray(0);

        return LoadedModel.builder()
                .vaoId(vaoId)
                .vertexCount(rawModel.getIndices().size())
                .build();
    }

    public int loadTexture(String path) {

        int textureID;
        int width, height;
        ByteBuffer image;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            image = stbi_load("res/"+path+".png", w, h, comp, 4);
            if (image == null) {
                System.out.println("Failed to load texture file: "+path+"\n" +
                        stbi_failure_reason()
                );
            }
            width = w.get();
            height = h.get();
        }

        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);
        textures.add(textureID);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST); //sets MINIFICATION filtering to nearest
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST); //sets MAGNIFICATION filtering to nearest
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);

        return textureID;
    }

    public void cleanUp() {
        for(int vao:vaos) {
            glDeleteVertexArrays(vao);
        }

        for(int vbo:vbos) {
            glDeleteBuffers(vbo);
        }
    }

    private void bindPositionBuffer(int attributeIndex, int coordinateSize, float[] positions) {
        createVbo(GL_ARRAY_BUFFER);

        FloatBuffer buffer = BufferUtils.createFloatBuffer(positions.length);
        buffer.put(positions);
        buffer.flip();

        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(attributeIndex, coordinateSize, GL_FLOAT, false, 0,0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private void bindIndexBuffer(int[] indices) {
        createVbo(GL_ELEMENT_ARRAY_BUFFER);

        IntBuffer buffer = BufferUtils.createIntBuffer(indices.length);
        buffer.put(indices);
        buffer.flip();

        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
    }

    private int createVao() {
        int vaoId = glGenVertexArrays();
        vaos.add(vaoId);

        glBindVertexArray(vaoId);

        return vaoId;
    }

    private int createVbo(int target) {
        int vboId = glGenBuffers();
        vbos.add(vboId);

        glBindBuffer(target, vboId);

        return vboId;
    }
}
