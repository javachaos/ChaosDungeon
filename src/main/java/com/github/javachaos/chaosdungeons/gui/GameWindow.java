package com.github.javachaos.chaosdungeons.gui;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
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
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import com.github.javachaos.chaosdungeons.constants.Constants;
import com.github.javachaos.chaosdungeons.ecs.GameLoop;
import com.github.javachaos.chaosdungeons.exceptions.ShaderLoadException;
import com.github.javachaos.chaosdungeons.shaders.UiShader;
import com.github.javachaos.chaosdungeons.shaders.WorldShader;
import java.io.PrintStream;
import java.nio.IntBuffer;
import java.util.Objects;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

/**
 * Game window.
 */
@SuppressWarnings("unused")
public class GameWindow {

  private static final int TARGET_FPS = 60;
  private static final long OPTIMAL_TIME = 1000000000 / TARGET_FPS; // Time per frame in nanoseconds
  private static final Logger LOGGER = LogManager.getLogger(GameWindow.class);
  private static WorldShader shaderProgram;
  private static UiShader uiShader;
  private static WindowSize windowSize;
  private long window;

  /**
   * Get the bounds of this Window as a Rectangle2D.
   *
   * @return the bounds of this window.
   */
  public static WindowSize getWindowSize() {
    return windowSize;
  }

  public static WorldShader getWorldShader() {
    return shaderProgram;
  }

  public static UiShader getUiShader() {
    return uiShader;
  }

  /**
   * Run the game!.
   */
  @SuppressWarnings("all")
  public void run(GameLoop gameLoop) throws ShaderLoadException, InterruptedException {
    System.out.println("Hello LWJGL " + Version.getVersion() + "!");

    init(gameLoop);
    loop(gameLoop);

    // Free the window callbacks and destroy the window
    glfwFreeCallbacks(window);
    glfwDestroyWindow(window);

    // Terminate GLFW and free the error callback
    glfwTerminate();
    Objects.requireNonNull(glfwSetErrorCallback(null)).free();
  }

  @SuppressWarnings("all")
  private void init(GameLoop gameLoop) {
    setupLogging();
    window = createGlfwWindow();
    setupInputCallbacks(gameLoop);
    addWindowResizeCallback();
    windowSize = new WindowSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
    showWindow(window);
  }

  @SuppressWarnings("all")
  private void loop(GameLoop gameLoop) throws ShaderLoadException, InterruptedException {
    GL.createCapabilities();
    uiShader = new UiShader();
    shaderProgram = new WorldShader();
    initView();
    long lastUpdateTime = System.nanoTime();
    long lastRenderTime = System.nanoTime();
    int frameCount = 0;
    long fpsTimer = System.currentTimeMillis();
    while (!glfwWindowShouldClose(window)) {
      if (!gameLoop.isInitialized()) {
        shaderProgram.init();
        gameLoop.init(this);
      }
      GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
      long now = System.nanoTime();
      double dt = (now - lastUpdateTime) / 1_000_000_000.0; // Convert to seconds
      lastUpdateTime = now;

      gameLoop.update(dt);
      //shaderProgram.bind();
      // Render the game
      gameLoop.render();
      //shaderProgram.unbind();
      frameCount++;

      // Sleep to maintain desired FPS
      long renderTime = System.nanoTime() - lastRenderTime;
      long sleepTime = (OPTIMAL_TIME - renderTime) / 1000000; // Convert to milliseconds

      if (sleepTime > 0) {
        Thread.sleep(sleepTime);
      }
      glfwSwapBuffers(window);
      glfwPollEvents();
      lastRenderTime = System.nanoTime();

      // Calculate FPS every second
      if (System.currentTimeMillis() - fpsTimer >= 1000) {
        double fps = (double) frameCount / (System.currentTimeMillis() - fpsTimer) * 1000;
        LOGGER.debug("FPS: {}", fps);
        frameCount = 0;
        fpsTimer = System.currentTimeMillis();
      }

    }
  }

  private void initView() {
    int w = windowSize.getWidth();
    int h = windowSize.getHeight();

    glEnable(GL_MULTISAMPLE);
    GL11.glViewport(0, 0, w, h);
    GL11.glClearColor(0.3f, 0.3f, 0.3f, 0.0f);
    GL11.glMatrixMode(GL11.GL_PROJECTION);
    GL11.glLoadIdentity();
    GL11.glOrtho(0, w, 0, h, 1, -1);
    GL11.glMatrixMode(GL11.GL_MODELVIEW);
    glEnable(GL_TEXTURE_2D);
    GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    glEnable(GL_BLEND);

  }

  private void setupLogging() {
    // Setup an error callback.
    PrintStream log = IoBuilder.forLogger(GameWindow.class)
        .setLevel(Level.DEBUG).buildPrintStream();
    GLFWErrorCallback.createPrint(log).set();
  }

  private long createGlfwWindow() {
    // Initialize GLFW. Most GLFW functions will not work before doing this.
    if (!glfwInit()) {
      throw new IllegalStateException("Unable to initialize GLFW");
    }
    // Configure GLFW
    glfwDefaultWindowHints(); // optional, the current window hints are already the default
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

    // Create the window
    window = glfwCreateWindow(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT,
        Constants.TITLE, NULL, NULL);
    if (window == NULL) {
      throw new RuntimeException("Failed to create the GLFW window");
    }
    return window;
  }

  private void addWindowResizeCallback() {
    glfwSetWindowSizeCallback(window, new GLFWWindowSizeCallback() {
      @Override
      public void invoke(long window, int width, int height) {
        windowSize.setWidth(width);
        windowSize.setHeight(height);
        //Updates the matrices
        initView();
      }
    });
  }

  private void setupInputCallbacks(GameLoop gameLoop) {
    // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
      if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
        glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        gameLoop.shutdown();
      }
    });
  }

  private void showWindow(long window) {
    // Get the thread stack and push a new frame
    try (MemoryStack stack = stackPush()) {
      IntBuffer pwidth = stack.mallocInt(1); // int*
      IntBuffer pheight = stack.mallocInt(1); // int*

      // Get the window size passed to glfwCreateWindow
      glfwGetWindowSize(window, pwidth, pheight);

      // Get the resolution of the primary monitor
      GLFWVidMode vidmode = Objects.requireNonNull(glfwGetVideoMode(glfwGetPrimaryMonitor()));

      // Center the window
      glfwSetWindowPos(
          window,
          (vidmode.width() - pwidth.get(0)) / 2,
          (vidmode.height() - pheight.get(0)) / 2
      );
    } // the stack frame is popped automatically

    glfwMakeContextCurrent(window);
    // Enable v-sync
    glfwSwapInterval(1);

    // Make the window visible
    glfwShowWindow(window);
  }
}
