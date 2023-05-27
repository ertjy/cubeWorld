package local.simas.cubeworld.engine.loader;

import local.simas.cubeworld.engine.data.LoadedTexture;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_X;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Y;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Z;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class TextureLoader {
    private static final Integer[] CUBE_MAP_TEXTURE_ORDER = new Integer[] {
            GL_TEXTURE_CUBE_MAP_POSITIVE_X,
            GL_TEXTURE_CUBE_MAP_NEGATIVE_X,
            GL_TEXTURE_CUBE_MAP_POSITIVE_Y,
            GL_TEXTURE_CUBE_MAP_NEGATIVE_Y,
            GL_TEXTURE_CUBE_MAP_POSITIVE_Z,
            GL_TEXTURE_CUBE_MAP_NEGATIVE_Z
    };
    private final List<Integer> textures = new ArrayList<>();


    public LoadedTexture loadRawTexture(BufferedImage rawTexture, float reflectivity, float shineDamper) {
        int textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);

        textures.add(textureId);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, rawTexture.getWidth(), rawTexture.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, convertImageToBuffer(rawTexture));

        return LoadedTexture.builder()
                .textureId(textureId)
                .shineDamper(shineDamper)
                .reflectivity(reflectivity)
                .build();
    }

    public LoadedTexture loadRawTexturesForCubeMap(List<BufferedImage> rawTextures) {
        if (rawTextures.size() != 6) {
            throw new IllegalArgumentException("Cube map must have 6 raw textures");
        }

        int textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_CUBE_MAP, textureId);

        textures.add(textureId);

        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_REPEAT);

        for (int i = 0; i < 6; i++) {
            BufferedImage rawTexture = rawTextures.get(i);
            glTexImage2D(CUBE_MAP_TEXTURE_ORDER[i], 0, GL_RGBA, rawTexture.getWidth(), rawTexture.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, convertImageToBuffer(rawTexture));
        }

        return LoadedTexture.builder()
                .textureId(textureId)
                .shineDamper(0f)
                .reflectivity(0f)
                .build();
    }

    public void cleanUp() {
        for(int texture : textures) {
            glDeleteTextures(texture);
        }
    }

    private ByteBuffer convertImageToBuffer(BufferedImage image) {
        int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        ByteBuffer buffer = ByteBuffer.allocateDirect(pixels.length * 4);

        for (int pixel : pixels) {
            buffer.put((byte) ((pixel >> 16) & 0xFF));
            buffer.put((byte) ((pixel >> 8) & 0xFF));
            buffer.put((byte) (pixel & 0xFF));
            buffer.put((byte) ((pixel >> 24) & 0xFF));
        }

        buffer.flip();
        return buffer;
    }
}
