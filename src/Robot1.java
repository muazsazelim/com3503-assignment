import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.texture.Texture;
import gmaths.Mat4;
import gmaths.Mat4Transform;
import gmaths.Vec3;

public class Robot1 {
    private Camera camera;
    private Light[] lights;
    private SGNode sgnode;
    private Texture t0;
    private Model sphere1, sphere2,  cube;
    private SGNode robotRoot;
    private TransformNode robotMoveTranslate;


    public Robot1(GL3 gl, Camera c, Light[] l, Texture t0) {
        camera = c;
        lights = l;
        this.t0 = t0;
    }

    public void initialise(GL3 gl) {
        sphere1 = makeSphere(gl, t0);
        sphere2 = makeSphere(gl, t0);
        cube = makeCube(gl, t0);

        float baseHeight = 1f;
        float baseScale = 2f;
        float bodyHeight = 2f;
        float bodyScale = 1f;
        float headScale = 2f;
        float armLength = 2f;
        float armScale = 0.5f;


        robotRoot = new NameNode("root");
        robotMoveTranslate = new TransformNode("robot transform",Mat4Transform.translate(0,0,0));

        TransformNode robotTranslate = new TransformNode("robot transform",Mat4Transform.translate(-5f,bodyHeight * 2.5f + baseHeight/2,-12f));

        // make pieces
        NameNode base = makeBase(gl, baseHeight, baseScale, bodyHeight, cube);
        NameNode body1 = makeBody1(gl, bodyHeight, bodyScale, sphere1);
        NameNode body2 = makeBody2(gl, bodyHeight, bodyScale, sphere1);
        NameNode body3 = makeBody3(gl, bodyHeight, bodyScale, sphere1);
        NameNode head = makeHead(gl, headScale, bodyHeight, sphere1);
        NameNode rightHand = makeRightHand(gl, armLength, armScale, sphere1);
        NameNode leftHand = makeLeftHand(gl, armLength, armScale, sphere1);

        // create whole robot
        robotRoot.addChild(robotMoveTranslate);
            robotMoveTranslate.addChild(robotTranslate);
            robotTranslate.addChild(body1);
            robotTranslate.addChild(body2);
            robotTranslate.addChild(body3);
            robotTranslate.addChild(base);
            robotTranslate.addChild(head);
            robotTranslate.addChild(rightHand);
            robotTranslate.addChild(leftHand);
        robotRoot.update();
    }

    private Model makeSphere(GL3 gl, Texture t1) {
        String name= "sphere";
        Mesh mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
        Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_2t.txt");
        Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
        Mat4 modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
        Model sphere = new Model(name, mesh, modelMatrix, shader, material, lights, camera, t1);
        return sphere;
    }

    private Model makeCube(GL3 gl, Texture t1) {
        String name= "cube";
        Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
        Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_2t.txt");
        Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
        Mat4 modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
        Model cube = new Model(name, mesh, modelMatrix, shader, material, lights, camera, t1);
        return cube;
    }

    private NameNode makeBase(GL3 gl, float baseHeight, float baseScale, float bodyHeight, Model cube) {
        NameNode base = new NameNode("base");
        Mat4 m = Mat4Transform.scale(baseScale, baseHeight, baseScale);
        m = Mat4.multiply(m, Mat4Transform.translate(0,-5.5f,0));
        TransformNode baseTransform = new TransformNode("base transform", m);
        ModelNode bodyShape = new ModelNode("Cube(base)", cube);
        base.addChild(baseTransform);
        baseTransform.addChild(bodyShape);
        return base;
    }

    private NameNode makeBody1(GL3 gl, float bodyHeight, float bodyScale, Model sphere) {
        NameNode body = new NameNode("body");
        Mat4 m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(bodyScale,bodyHeight,bodyScale));
        m = Mat4.multiply(m, Mat4Transform.translate(0,0,0));
        TransformNode bodyTransform = new TransformNode("body transform", m);
        ModelNode headShape = new ModelNode("Sphere(body)", sphere);
        body.addChild(bodyTransform);
        bodyTransform.addChild(headShape);
        return body;
    }

    private NameNode makeBody2(GL3 gl, float bodyHeight, float bodyScale, Model sphere) {
        NameNode body = new NameNode("body");
        Mat4 m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(bodyScale,bodyHeight,bodyScale));
        m = Mat4.multiply(m, Mat4Transform.translate(0, -bodyHeight/2,0));
        TransformNode bodyTransform = new TransformNode("body transform", m);
        ModelNode headShape = new ModelNode("Sphere(body)", sphere);
        body.addChild(bodyTransform);
        bodyTransform.addChild(headShape);
        return body;
    }

    private NameNode makeBody3(GL3 gl, float bodyHeight, float bodyScale, Model sphere) {
        NameNode body = new NameNode("body");
        Mat4 m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(bodyScale,bodyHeight,bodyScale));
        m = Mat4.multiply(m, Mat4Transform.translate(0, -bodyHeight,0));
        TransformNode bodyTransform = new TransformNode("body transform", m);
        ModelNode headShape = new ModelNode("Sphere(body)", sphere);
        body.addChild(bodyTransform);
        bodyTransform.addChild(headShape);
        return body;
    }

    private NameNode makeHead(GL3 gl, float headScale, float bodyHeight, Model sphere) {
        NameNode head = new NameNode("head");
        Mat4 m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(headScale,headScale,headScale));
        m = Mat4.multiply(m, Mat4Transform.translate(0, bodyHeight/2,0));
        TransformNode headTransform = new TransformNode("head transform", m);
        ModelNode headShape = new ModelNode("Sphere(head)", sphere);
        head.addChild(headTransform);
        headTransform.addChild(headShape);
        return head;
    }

    private NameNode makeRightHand(GL3 gl, float armLength, float armScale, Model sphere) {
        NameNode rightHand = new NameNode("rightHand");
        Mat4 m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.translate(-1.5f, 0,0));
        m = Mat4.multiply(m, Mat4Transform.scale(armLength, armScale, armScale));
        TransformNode headTransform = new TransformNode("right hand transform", m);
        ModelNode headShape = new ModelNode("Sphere(rightHand)", sphere);
        rightHand.addChild(headTransform);
        headTransform.addChild(headShape);
        return rightHand;
    }

    private NameNode makeLeftHand(GL3 gl, float armLength, float armScale, Model sphere) {
        NameNode leftHand = new NameNode("leftHand");
        Mat4 m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.translate(1.5f, 0,0));
        m = Mat4.multiply(m, Mat4Transform.scale(armLength, armScale, armScale));
        TransformNode headTransform = new TransformNode("left hand transform", m);
        ModelNode headShape = new ModelNode("Sphere(leftHand)", sphere);
        leftHand.addChild(headTransform);
        headTransform.addChild(headShape);
        return leftHand;
    }







    private double startTime;

    private double getSeconds() {
        return System.currentTimeMillis()/1000.0;
    }

    public void render(GL3 gl) {
        robotRoot.draw(gl);
    }

}
