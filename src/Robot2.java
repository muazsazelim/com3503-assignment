import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.texture.Texture;
import gmaths.Mat4;
import gmaths.Mat4Transform;
import gmaths.Vec3;

public class Robot2 {

    private Camera camera;
    private Light[] lights;
    private SGNode sgnode;
    private Texture t0, t1, t2, t3;
    private Model sphere1, sphere2, sphere3,  cube, cube1;
    private SGNode robotRoot;
    private TransformNode robotMoveTranslate, rotateAll, rotateUpper1, rotateUpper2, rotateUpper3, rotateUpper4, rotateUpper5, rotateUpper6;
    private float rotateAllAngleStart = 25, rotateAllAngle = rotateAllAngleStart;
    private float rotateUpperAngleStart = -60, rotateUpperAngle = rotateUpperAngleStart;

    public Robot2(GL3 gl, Camera c, Light[] l, Texture t0) {
        camera = c;
        lights = l;
        this.t0 = t0;
        //this.t1 = t1;
        //this.t2 = t2;
        //this.t3 = t3;
    }

    public void initialise(GL3 gl) {
        cube = makeCube(gl, t0); // body

        float bodyScale = 1.0f;
        float bodyLength = 3.0f;


        robotRoot = new NameNode("root");
        robotMoveTranslate = new TransformNode("robot transform",Mat4Transform.translate(0,0,0));

        NameNode body = makeBody(gl, bodyLength, bodyScale, cube);

        robotRoot.addChild(body);

        robotRoot.update();
    }

    private Model makeSphere(GL3 gl, Texture t1) {
        String name= "sphere";
        Mesh mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
        Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_2t.txt");
        Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
        Mat4 modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0,0));
        Model sphere = new Model(name, mesh, modelMatrix, shader, material, lights, camera, t1);
        return sphere;
    }

    // base
    private Model makeCube(GL3 gl, Texture t1) {
        String name= "cube";
        Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
        Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_2t.txt");
        Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
        Mat4 modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0,0));
        Model cube = new Model(name, mesh, modelMatrix, shader, material, lights, camera, t1);
        return cube;
    }

    private NameNode makeBody(GL3 gl, float bodyLength, float bodyScale, Model cube) {
        NameNode body = new NameNode("body");
        Mat4 m = Mat4Transform.scale(bodyScale, bodyScale, bodyLength);
        m = Mat4.multiply(m, Mat4Transform.translate(0,0f,0));
        TransformNode baseTransform = new TransformNode("body transform", m);
        ModelNode bodyShape = new ModelNode("Cube(body)", cube);
        body.addChild(baseTransform);
        baseTransform.addChild(bodyShape);
        return body;
    }













    private double startTime;

    private double getSeconds() {
        return System.currentTimeMillis()/1000.0;
    }

    public void render(GL3 gl) {
        // updateBranches();
        robotRoot.draw(gl);
    }

}
