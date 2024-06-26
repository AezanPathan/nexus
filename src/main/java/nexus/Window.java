package nexus;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import util.Time;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
  private  int width,height;
  private  String title;
  private long glfwWindow;
public  float r,g,b,a;
private  boolean fadeToBlack = false;

  private static Window window = null;
private static Scene currentscene;
    private Window(){
        this.width = 580;
        this.height = 420;
        this.title = "Nexuse";
        r = 1;
        g = 1;
        b = 1;
        a = 1;

    }
    public static void changeScene(int newScene){
        switch (newScene){
            case 0:
                currentscene = new LevelEditorScene();
                currentscene.init();
                break;
            case  1:
                currentscene = new LevelScene();
                currentscene.init();
                break;
            default:
                assert false:"unkown scene" + newScene;
                break;
        }
    }

    public  static Window get(){
        if (Window.window == null){
            Window.window = new Window();
        }
        return Window.window;
    }

    public void run(){
        System.out.println("Hello lwgjl" + Version.getVersion() + "!");

        init();
        loop();

        //free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //terminate glfw and the free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public  void init(){
        //setup error callback
        GLFWErrorCallback.createPrint(System.err).set();

        //initialize Glfw
        if(!glfwInit()){
            throw  new IllegalStateException("unable to initialize glfw");
        }

        //configure glfw
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE,GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE,GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED,GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR,3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR,3);


        //crate the window
        glfwWindow = glfwCreateWindow(this.width,this.height,this.title,NULL,NULL);

        if(glfwWindow == NULL){
            throw  new IllegalStateException("failed to load the glfw window");
        }
        glfwSetCursorPosCallback(glfwWindow,MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow,MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow,MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow,KeyListener::keyCallback);
        // make the opengl context current
        glfwMakeContextCurrent(glfwWindow);
        //enable v-sync
        glfwSwapInterval(1);

        ///make the window visible
        glfwShowWindow(glfwWindow);

        GL.createCapabilities();
        Window.changeScene(0);
    }
    public  void loop(){
        float beginTime = Time.getTime();
        float endTime;
        float dt = -1.0f;
        while (!glfwWindowShouldClose(glfwWindow)){
    //poll system
            glfwPollEvents();

             glClearColor(r,g,b,a);
             glClear(GL_COLOR_BUFFER_BIT);

        if (dt >= 0) {
            currentscene.update(dt);
        }
             glfwSwapBuffers(glfwWindow);

             endTime = Time.getTime();
              dt = endTime - beginTime;
             beginTime = endTime;
        }
    }
}
