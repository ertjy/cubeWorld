package local.simas.cubeworld.engine;

import local.simas.cubeworld.engine.config.WindowConfig;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.time.Instant;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class DisplayManager {
    private static WindowConfig windowConfig;
    private static Long windowHandle;
    private static float currentFrameTime;
    private static Instant lastFrameInstant;

    public static void createWindow(WindowConfig windowConfig) {
        GLFWErrorCallback.createPrint(System.err).set();

        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        windowHandle = glfwCreateWindow(windowConfig.getWidth(), windowConfig.getHeight(), windowConfig.getTitle(), NULL, NULL);
        if (windowHandle == NULL) {
            windowHandle = null;
            throw new RuntimeException("Failed to create the GLFW window");
        }

        if (windowConfig.getShouldCenter()) {
            try (MemoryStack stack = stackPush()) {
                IntBuffer pWidth = stack.mallocInt(1);
                IntBuffer pHeight = stack.mallocInt(1);

                glfwGetWindowSize(windowHandle, pWidth, pHeight);

                GLFWVidMode videoMode = Objects.requireNonNull(glfwGetVideoMode(glfwGetPrimaryMonitor()));

                glfwSetWindowPos(
                        windowHandle,
                        (videoMode.width() - pWidth.get(0)) / 2,
                        (videoMode.height() - pHeight.get(0)) / 2
                );
            }
        }

        glfwMakeContextCurrent(windowHandle);
        glfwSwapInterval(1);

        GL.createCapabilities();
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
        glViewport(0, 0, windowConfig.getWidth(), windowConfig.getHeight());
        glEnable(GL_DEPTH_TEST);

        DisplayManager.windowConfig = windowConfig;

        currentFrameTime = 0f;
        lastFrameInstant = Instant.now();
    }

    public static void showWindow() {
        if (windowHandle == null) {
            throw new IllegalStateException("Window is not created");
        }

        glfwShowWindow(windowHandle);
    }

    public static Boolean windowShouldClose() {
        if (windowHandle == null) {
            throw new IllegalStateException("Window is not created");
        }

        return glfwWindowShouldClose(windowHandle);
    }

    public static void updateWindow() {
        if (windowHandle == null) {
            throw new IllegalStateException("Window is not created");
        }
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();

        Instant currentFrameInstant = Instant.now();
        currentFrameTime = (currentFrameInstant.toEpochMilli() - lastFrameInstant.toEpochMilli()) / 1000f;
        lastFrameInstant = currentFrameInstant;
    }

    public static void closeWindow() {
        if (windowHandle == null) {
            throw new IllegalStateException("Window is not created");
        }

        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);

        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    public static int getKey(int key) {
        return glfwGetKey(windowHandle, key);
    }

    public static Vector2f getCursorPosition() {
        DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(windowHandle, xBuffer, yBuffer);
        float x = (float) xBuffer.get(0);
        float y = (float) yBuffer.get(0);

        return new Vector2f(x, y);
    }

    public static void setCursorPosition(Vector2f cursorPosition) {
        glfwSetCursorPos(windowHandle, cursorPosition.x(), cursorPosition.y());
    }

    public static WindowConfig getWindowConfig() {
        return windowConfig;
    }

    public static float getCurrentFrameTime() {
        return currentFrameTime;
    }
}
