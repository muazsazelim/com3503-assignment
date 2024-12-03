import gmaths.*;

import com.jogamp.opengl.*;

public class Spacecraft_GLEventListener implements GLEventListener {
  
  private static final boolean DISPLAY_SHADERS = false;
  private Camera camera;
    
  /* The constructor is not used to initialise anything */
  public Spacecraft_GLEventListener(Camera camera) {
    this.camera = camera;
    this.camera.setPosition(new Vec3(0f,8f,30f));
    this.camera.setTarget(new Vec3(0f,5f,0f));
  }
  
  // ***************************************************
  /*
   * METHODS DEFINED BY GLEventListener
   */

  /* Initialisation */
  public void init(GLAutoDrawable drawable) {   
    GL3 gl = drawable.getGL().getGL3();
    System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
    gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); 
    gl.glClearDepth(1.0f);
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(GL.GL_LESS);
    gl.glFrontFace(GL.GL_CCW);    // default is 'CCW'
    gl.glEnable(GL.GL_CULL_FACE); // default is 'not enabled' so needs to be enabled
    gl.glCullFace(GL.GL_BACK);   // default is 'back', assuming CCW
    initialise(gl);
    startTime = getSeconds();
  }
  
  /* Called to indicate the drawing surface has been moved and/or resized  */
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    GL3 gl = drawable.getGL().getGL3();
    gl.glViewport(x, y, width, height);
    float aspect = (float)width/(float)height;
    camera.setPerspectiveMatrix(Mat4Transform.perspective(45, aspect));
  }

  /* Draw */
  public void display(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    render(gl);
  }

  /* Clean up memory */
  public void dispose(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    room.dispose(gl);
    space.dispose(gl);
    globe.dispose(gl);
    lights[0].dispose(gl);
    lights[1].dispose(gl);
    textures.destroy(gl);
  }

  // ***************************************************
  /* THE SCENE
   * Now define all the methods to handle the scene.
   * This will be added to in later examples.
   */

  // textures
  private TextureLibrary textures;

  private Room room;
  private Space space;
  private Globe globe;
  private Robot1 robot1;
  private Light[] lights = new Light[2];

  private void loadTextures(GL3 gl) {
    textures = new TextureLibrary();
    textures.add(gl, "chequerboard", "assets/textures/chequerboard.jpg");
    textures.add(gl, "container_diffuse", "assets/textures/container2.jpg");
    textures.add(gl, "container_specular", "assets/textures/container2_specular.jpg");
    textures.add(gl, "square", "assets/textures/square.jpg");
    textures.add(gl, "cloud", "assets/textures/cloud.jpg");
    textures.add(gl, "sportrim", "assets/textures/sportrim.jpg");
    textures.add(gl, "sky", "assets/textures/sky.jpg");
    textures.add(gl, "back_wall_diffuse", "assets/textures/diffuse_muaz.jpg");
    textures.add(gl, "back_wall_specular", "assets/textures/specular_muaz.jpg");
    textures.add(gl, "space", "assets/textures/space.jpg");
    textures.add(gl, "globe", "assets/textures/globe.jpg");
    textures.add(gl, "box", "assets/textures/box.jpg");
    textures.add(gl, "base", "assets/textures/base.jpg");
    textures.add(gl, "black", "assets/textures/black.jpg");
    textures.add(gl, "r1body1", "assets/textures/r1body1.jpg");
    textures.add(gl, "r1body2", "assets/textures/r1body2.jpg");

  }

  public void initialise(GL3 gl) {
    loadTextures(gl);


    lights[0] = new Light(gl);
    lights[0].setCamera(camera);
    lights[1] = new Light(gl);
    lights[1].setCamera(camera);
    room = new Room(gl, camera, lights, textures.get("chequerboard"), textures.get("sportrim"), textures.get("sky"), textures.get("back_wall_diffuse"), textures.get("back_wall_specular"));
    space = new Space(gl, camera, lights);
    space.loadTextures(gl);
    globe = new Globe(gl, camera, lights, textures.get("box"), textures.get("globe"), textures.get("space"));
    robot1 = new Robot1(gl, camera, lights, textures.get("black"), textures.get("base"), textures.get("r1body1"), textures.get("r1body2"));
    robot1.initialise(gl);
  }


  public void render(GL3 gl) {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    room.render(gl);
    space.render(gl);
    globe.render(gl);
    robot1.render(gl);
  }

  
    // ***************************************************
  /* TIME
   */ 
  
  private double startTime;
  
  private double getSeconds() {
    return System.currentTimeMillis()/1000.0;
  }
  
}

