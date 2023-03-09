package local.simas.cubeworld.engine;

import local.simas.cubeworld.engine.config.WindowConfig;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class DisplayManager {
    private static Long windowHandle;

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
            try ( MemoryStack stack = stackPush() ) {
                IntBuffer pWidth = stack.mallocInt(1);
                IntBuffer pHeight = stack.mallocInt(1);

                glfwGetWindowSize(windowHandle, pWidth, pHeight);

                GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

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
    }

    public static void closeWindow() {
        if (windowHandle == null) {
            throw new IllegalStateException("Window is not created");
        }

        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
