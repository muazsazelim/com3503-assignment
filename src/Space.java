import gmaths.*;

import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;
import com.jogamp.opengl.util.texture.*;

public class Space {
    private Camera camera;
    private Light[] lights;
    private TextureLibrary t;
    private Texture[] tl;
    private float size = 16f;

    public Space(GL3 gl, Camera c, Light[] l) {
        camera = c;
        lights = l;
    }

    public void loadTextures(GL3 gl) {
        t = new TextureLibrary();
        t.add(gl,"00", "assets/textures/space/00.jpg");
        t.add(gl,"01", "assets/textures/space/01.jpg");
        t.add(gl,"02", "assets/textures/space/02.jpg");
        t.add(gl,"03", "assets/textures/space/03.jpg");
        t.add(gl,"04", "assets/textures/space/04.jpg");
        t.add(gl,"05", "assets/textures/space/05.jpg");
        t.add(gl,"06", "assets/textures/space/06.jpg");
        t.add(gl,"07", "assets/textures/space/07.jpg");
        t.add(gl,"08", "assets/textures/space/08.jpg");
        t.add(gl,"09", "assets/textures/space/09.jpg");
        t.add(gl,"10", "assets/textures/space/10.jpg");
        t.add(gl,"11", "assets/textures/space/11.jpg");

        tl = new Texture[12];
        tl[0] = t.get("00");
        tl[1] = t.get("01");
        tl[2] = t.get("02");
        tl[3] = t.get("03");
        tl[4] = t.get("04");
        tl[5] = t.get("05");
        tl[6] = t.get("06");
        tl[7] = t.get("07");
        tl[8] = t.get("08");
        tl[9] = t.get("09");
        tl[10] = t.get("10");
        tl[11] = t.get("11");

    }

    private Model spaceWall(GL3 gl, Texture t) {
        String name="space wall";
        // side wall - right
        Vec3 basecolor = new Vec3(0.5f, 0.5f, 0.5f); // grey
        Material material = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);    // side wall - right
        Mat4 modelMatrix = new Mat4(1);
        modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f, size), modelMatrix);
        modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
        modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), modelMatrix);
        modelMatrix = Mat4.multiply(Mat4Transform.translate(-size*0.51f,size*0.5f,0), modelMatrix);
        Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
        Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_1t.txt");
        Model model = new Model(name, mesh, modelMatrix, shader, material, lights, camera, t);

        tl[0].bind(gl);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
        return model;
    }

    private int index = 0;
    private int index2 = 0;
    public Texture currentTexture;

    public void render(GL3 gl) {
        if (index > 10) {
            index2++;
            if (index2 > 11) {
                index2 = 0;
            }
            currentTexture = tl[index2];
            index = 0;
        }

        index++;
        spaceWall(gl, currentTexture).render(gl);
    }

    public void dispose(GL3 gl) {
        spaceWall(gl, currentTexture).dispose(gl);
    }
}
