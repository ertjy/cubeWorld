package local.simas.cubeworld.engine;

import local.simas.cubeworld.engine.data.LoadedModel;
import local.simas.cubeworld.engine.data.TexturedModel;
import local.simas.cubeworld.engine.shader.ShaderProgram;
import lombok.Builder;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

@Builder
public class Renderer {
    private ShaderProgram shaderProgram;

    public void prepare() {
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL11.GL_COLOR_BUFFER_BIT);
    }

    public void render(TexturedModel texturedModel, LoadedModel loadedModel) {
        shaderProgram.start();

        glBindVertexArray(loadedModel.getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texturedModel.getTexture().getId());

        glDrawElements(GL_TRIANGLES, loadedModel.getVertexCount(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shaderProgram.stop();
    }

    public void setShaderProgram(ShaderProgram shaderProgram) {
        this.shaderProgram.cleanUp();
        this.shaderProgram = shaderProgram;
    }

    public void cleanUp() {
        shaderProgram.cleanUp();
    }
}
