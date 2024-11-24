import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.texture.Texture;
import gmaths.Mat4;
import gmaths.Mat4Transform;
import gmaths.Vec3;

public class Globe {
    private Camera camera;
    private Light[] lights;
    private Texture t0, t1, t2;
    private float size = 16f;
    private Model[] globes;


    public Globe(GL3 gl, Camera c, Light[] l, Texture t0, Texture t1, Texture t2) {
        camera = c;
        lights = l;
        this.t0 = t0; // box texture
        this.t1 = t1; // globe
        this.t2 = t2; // stand
        globes = new Model[3];
        globes[0] = makeCube(gl);
        globes[1] = makeGlobe(gl);
        globes[2] = makeStand(gl);
        startTime = getSeconds();
    }

    private Model makeCube(GL3 gl) {
        float cubeHeight = 2.0f;  // *** added variable to make subsequent calculations easier

        String name = "cube";
        Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
        Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_1t.txt");
        Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);

        // *** scale by cubeHeight
        Mat4 modelMatrix = Mat4.multiply(Mat4Transform.scale(cubeHeight,cubeHeight,cubeHeight), Mat4Transform.translate(2f,0.5f,5f));
        Model cube = new Model(name, mesh, modelMatrix, shader, material, lights, camera, t0);
        return cube;
    }

    private Model makeGlobe(GL3 gl) {
        float cubeHeight = 2.0f;
        float sphereHeight = 2f;

        String name = "sphere";
        Mesh mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
        Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_2t.txt");
        Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);

        // *** translate to top  of cube, and also scale by sphereHeight
        Mat4 modelMatrix = Mat4Transform.translate(0,cubeHeight,0);
        modelMatrix = Mat4.multiply(modelMatrix, Mat4Transform.scale(sphereHeight, sphereHeight, sphereHeight));
        modelMatrix = Mat4.multiply(modelMatrix, Mat4Transform.translate(2f,0.5f,5f));
        Model sphere = new Model(name, mesh, modelMatrix, shader, material, lights, camera, t1);
        return sphere;
    }

    private Model makeStand(GL3 gl) {
        float cubeHeight = 2.0f;
        float sphereHeight = 2f;

        String name = "stand";
        Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
        Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_2t.txt");
        Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);

        // *** translate to top  of cube, and also scale by sphereHeight
        Mat4 modelMatrix = Mat4Transform.translate(0,cubeHeight,0);
        modelMatrix = Mat4.multiply(modelMatrix, Mat4Transform.scale(sphereHeight, sphereHeight, sphereHeight));
        modelMatrix = Mat4.multiply(modelMatrix, Mat4Transform.translate(2f,0.5f,5f));
        modelMatrix = Mat4.multiply(modelMatrix, Mat4Transform.scale(0.1f, 1.2f, 0.1f));
        Model stand = new Model(name, mesh, modelMatrix, shader, material, lights, camera, t2);
        return stand;
    }

    private double startTime;

    private double getSeconds() {
        return System.currentTimeMillis()/1000.0;
    }

    private Mat4 getModelMatrix() {
        float cubeHeight = 2.0f;
        float sphereHeight = 2f;
        double elapsedTime = getSeconds()-startTime;
        Mat4 m;
        float yAngle = (float)(elapsedTime*100);
        m = Mat4Transform.translate(0,cubeHeight,0);
        m = Mat4.multiply(m, Mat4Transform.scale(sphereHeight, sphereHeight, sphereHeight));
        m = Mat4.multiply(m, Mat4Transform.translate(2f,0.5f,5f));
        m = Mat4.multiply(m, Mat4Transform.rotateAroundY(yAngle));
        return m;
    }


    public void render(GL3 gl) {
        for (int i=0; i<3; i++) {
            if (i == 1) {
                globes[i].setModelMatrix(getModelMatrix());
            }
            globes[i].render(gl);
        }
    }

    public void dispose(GL3 gl) {
        for (int i=0; i<3; i++) {
            globes[i].dispose(gl);
        }
    }
}


