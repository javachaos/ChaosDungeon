package com.github.javachaos.chaosdungeons;

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
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import com.github.javachaos.chaosdungeons.constants.Constants;
import com.github.javachaos.chaosdungeons.ecs.GameLoop;
import com.github.javachaos.chaosdungeons.utils.ShaderUtils;
import java.nio.IntBuffer;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

/**
 * Main entry point.
 */
public class MainGame {

  private int shaderProgram;
  private static final int TARGET_FPS = 60;
  private static final long OPTIMAL_TIME = 1000000000 / TARGET_FPS; // Time per frame in nanoseconds
  private static final long MAX_SKIP_FRAMES = 10;
  private static final Logger LOGGER = LogManager.getLogger(MainGame.class);

  // The window handle
  private long window;

  public static void main(String[] args) {
    GameLoop gl = new GameLoop();
    new MainGame().run(gl);
  }

  /**
   * Initialize shaders.
   */
  public void initShaders() {
    // Create the vertex shader
    int vertexShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
    GL20.glShaderSource(vertexShader,
        Objects.requireNonNull(ShaderUtils.readShaderSource("/vertex.glsl")));
    GL20.glCompileShader(vertexShader);

    // Create the fragment shader
    int fragmentShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
    GL20.glShaderSource(fragmentShader,
        Objects.requireNonNull(ShaderUtils.readShaderSource("/fragment.glsl")));
    GL20.glCompileShader(fragmentShader);

    // Create the shader program and attach the shaders
    shaderProgram = GL20.glCreateProgram();
    GL20.glAttachShader(shaderProgram, vertexShader);
    GL20.glAttachShader(shaderProgram, fragmentShader);

    // Link the shader program
    GL20.glLinkProgram(shaderProgram);

    // Validate the shader program (optional but recommended)
    GL20.glValidateProgram(shaderProgram);

    // Delete the individual shaders (they are already linked to the program)
    GL20.glDeleteShader(vertexShader);
    GL20.glDeleteShader(fragmentShader);

  }

  /**
   * Run the game!.
   */
  @SuppressWarnings("all")
  public void run(GameLoop gameLoop) {
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
    // Setup an error callback. The default implementation
    // will print the error message in System.err.
    GLFWErrorCallback.createPrint(System.err).set();

    // Initialize GLFW. Most GLFW functions will not work before doing this.
    if (!glfwInit()) {
      throw new IllegalStateException("Unable to initialize GLFW");
    }

    // Configure GLFW
    glfwDefaultWindowHints(); // optional, the current window hints are already the default
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

    // Create the window
    window = glfwCreateWindow(300, 300, Constants.TITLE, NULL, NULL);
    if (window == NULL) {
      throw new RuntimeException("Failed to create the GLFW window");
    }
    // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
      if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
        glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        gameLoop.shutdown();
      }
    });

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

  private void loop(GameLoop gameLoop) {
    GL.createCapabilities();
    glClearColor(0.2f, 0.2f, 0.2f, 0.0f);
    // Enable blending
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

    // Enable depth testing
    GL11.glEnable(GL11.GL_DEPTH_TEST);
    long lastUpdateTime = System.nanoTime();
    long lastRenderTime = System.nanoTime();
    int frameCount = 0;
    long fpsTimer = System.currentTimeMillis();
    double accumulator = 0.0;
    while (!glfwWindowShouldClose(window)) {
      if (!gameLoop.isInitialized()) {
        initShaders();
        gameLoop.init();
      }
      GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
      long now = System.nanoTime();
      double dt = (now - lastUpdateTime) / 1_000_000_000.0; // Convert to seconds
      lastUpdateTime = now;
      accumulator += dt;

      // Determine the number of update steps to perform
      int updateSteps = 0;
      while (accumulator >= dt && updateSteps < MAX_SKIP_FRAMES) {
        gameLoop.update(accumulator);
        accumulator -= dt;
        updateSteps++;
      }
      GL20.glUseProgram(shaderProgram);
      // Render the game
      gameLoop.render();
      GL20.glUseProgram(0);
      frameCount++;

      // Sleep to maintain desired FPS
      long renderTime = System.nanoTime() - lastRenderTime;
      long sleepTime = (OPTIMAL_TIME - renderTime) / 1000000; // Convert to milliseconds
      if (sleepTime > 0) {
        GLFW.glfwWaitEventsTimeout(sleepTime);
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

}

