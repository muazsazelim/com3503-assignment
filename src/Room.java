import gmaths.*;

import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;
import com.jogamp.opengl.util.texture.*;

public class Room {

  private ModelMultipleLights[] wall;
  private Camera camera;
  private Light[] lights;
  private Texture t0, t1, t2, t3, t4, t5;
  private float size = 16f;

  public Room(GL3 gl, Camera c, Light[] l, Texture t0, Texture t1, Texture t2, Texture t3, Texture t4, Texture t5) {
    camera = c;
    lights = l;
    this.t0 = t0; // square
    this.t1 = t1; // sportrim
    this.t2 = t2; // sky
    this.t3 = t3; // wall back diffuse
    this.t4 = t4; // wall back specular
    this.t5 = t5; // left wall
    wall = new ModelMultipleLights[10];
    wall[0] = makeWall0(gl);
    wall[1] = makeWall1(gl);
    wall[2] = makeWall2(gl);
    wall[3] = makeWall3(gl);
    wall[4] = makeWall4(gl);
    wall[5] = makeWall5(gl);
    wall[6] = makeWall6(gl);
    wall[7] = makeWall7(gl);

  }

  // There is repetition in each of the following methods
  // An alternative would attempt to remove the repetition
 
  private ModelMultipleLights makeWall0(GL3 gl) {
    String name="floor";
    Vec3 basecolor = new Vec3(0.5f, 0.5f, 0.5f); // grey
    Material material = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    // create floor
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,2 * size), modelMatrix);
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_1t.txt");
    ModelMultipleLights model = new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, t0);
    return model;
  }

  private ModelMultipleLights makeWall1(GL3 gl) {
    String name="wall";
    Vec3 basecolor = new Vec3(0.5f, 0.5f, 0.5f); // grey
    Material material = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    // back wall
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0,size*0.5f,-size), modelMatrix);
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_2t.txt");
    ModelMultipleLights model = new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, t3, t4);
    return model;
  }

  private ModelMultipleLights makeWall2(GL3 gl) {
    String name="wall";
    Material material = new Material(new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    // side wall - left - top
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(2 * size,1f,2f), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-size*0.5f,15f,0), modelMatrix);
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_1t.txt");
    // no texture on this model
    ModelMultipleLights model = new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, t5);
    return model;
  }

  private ModelMultipleLights makeWall3(GL3 gl) {
    String name="wall";
    // side wall - right
    Vec3 basecolor = new Vec3(0.5f, 0.5f, 0.5f); // grey
    Material material = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);    // side wall - right
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(2 * size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(size*0.5f,size*0.5f,0), modelMatrix);
    Mesh mesh = new Mesh(gl, TwoTrianglesRepeat.vertices.clone(), TwoTrianglesRepeat.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_1t.txt");
    ModelMultipleLights model = new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, t1);

    t1.bind(gl);
    gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
    gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
    return model;
  }

  private ModelMultipleLights makeWall4(GL3 gl) {
    String name="roof";
    Vec3 basecolor = new Vec3(0.5f, 0.5f, 0.5f); // grey
    Material material = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    // roof
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,2 * size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(180), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0,size,0), modelMatrix);
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_1t.txt");
    ModelMultipleLights model = new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, t2);
    return model;
  }

  private ModelMultipleLights makeWall5(GL3 gl) {
    String name="wall";
    Material material = new Material(new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    // side wall - left - bottom
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(2 * size,1f,2f), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-size*0.5f,1f,0), modelMatrix);
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_1t.txt");
    // no texture on this model
    ModelMultipleLights model = new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, t5);
    return model;
  }

  private ModelMultipleLights makeWall6(GL3 gl) {
    String name="wall";
    Material material = new Material(new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    // side wall - left - left
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(4f,1f,12f), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-size*0.5f,8f,-14f), modelMatrix);
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_1t.txt");
    // no texture on this model
    ModelMultipleLights model = new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, t5);
    return model;
  }

  private ModelMultipleLights makeWall7(GL3 gl) {
    String name="wall";
    Material material = new Material(new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    // side wall - left - right
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(4f,1f,12f), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-size*0.5f,8f,14f), modelMatrix);
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_1t.txt");
    // no texture on this model
    ModelMultipleLights model = new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, t5);
    return model;
  }


  public void render(GL3 gl) {
    for (int i=0; i<8; i++) {
       wall[i].render(gl);
    }
  }

  public void dispose(GL3 gl) {
    for (int i=0; i<8; i++) {
      wall[i].dispose(gl);
    }
  }
}