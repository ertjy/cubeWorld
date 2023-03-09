package local.simas.cubeworld.engine;

import local.simas.cubeworld.engine.data.LoadedModel;
import local.simas.cubeworld.engine.shader.ShaderProgram;
import lombok.Builder;
import lombok.Setter;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawElements;
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

    public void render(LoadedModel model) {
        shaderProgram.start();

        glBindVertexArray(model.getVaoId());
        glEnableVertexAttribArray(0);

        glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
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
