import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.texture.Texture;
import gmaths.Mat4;
import gmaths.Mat4Transform;
import gmaths.Vec3;

import javax.naming.Name;

public class Robot1 {
    private Camera camera;
    private Light[] lights;
    private SGNode sgnode;
    private Texture t0, t1, t2, t3;
    private Model sphere1, sphere2, sphere3,  cube, cube1;
    private SGNode robotRoot;
    private TransformNode robotMoveTranslate, rotateAll, rotateUpper1, rotateUpper2, rotateUpper3;
    private float rotateAllAngleStart = 25, rotateAllAngle = rotateAllAngleStart;
    private float rotateUpperAngleStart = -60, rotateUpperAngle = rotateUpperAngleStart;


    public Robot1(GL3 gl, Camera c, Light[] l, Texture t0, Texture t1, Texture t2, Texture t3) {
        camera = c;
        lights = l;
        this.t0 = t0; // eye and hair
        this.t1 = t1; // base
        this.t2 = t2; // body 1
        this.t3 = t3; // body 2
    }

    public void initialise(GL3 gl) {
        sphere1 = makeSphere(gl, t2); // hands, body2, head
        sphere2 = makeSphere(gl, t3); // body1, body3
        sphere3 = makeSphere(gl, t0);
        cube = makeCube(gl, t1); // base
        cube1 = makeCube(gl, t0);

        float baseHeight = 0.5f;
        float baseScale = 2f;
        float bodyHeight = 1.5f;
        float bodyScale = 0.75f;
        float headScale = 1.5f;
        float armLength = 1.5f;
        float armScale = 0.5f;


        robotRoot = new NameNode("root");
        robotMoveTranslate = new TransformNode("robot transform",Mat4Transform.translate(0,0,0));

        TransformNode robotTranslate = new TransformNode("robot transform",Mat4Transform.translate(-4f,0,-12f));
        TransformNode translate1 = new TransformNode("translate(0, top1, 0)",Mat4Transform.translate(0,1.9f,0));
        TransformNode translate2 = new TransformNode("translate(0, top2, 0)",Mat4Transform.translate(0,1.5f,0));
        TransformNode translate3 = new TransformNode("translate(0, top2, 0)",Mat4Transform.translate(0,0.75f,0));
        rotateAll = new TransformNode("rotateAroundZ("+rotateAllAngle+")", Mat4Transform.rotateAroundZ(rotateAllAngle));
        rotateUpper1 = new TransformNode("rotateAroundZ(-"+rotateUpperAngle+")",Mat4Transform.rotateAroundZ(-rotateUpperAngle));
        rotateUpper2 = new TransformNode("rotateAroundZ("+rotateUpperAngle+")",Mat4Transform.rotateAroundZ(rotateUpperAngle));
        rotateUpper3 = new TransformNode("rotateAroundZ("+rotateUpperAngle+")",Mat4Transform.rotateAroundY(rotateUpperAngle));



        // make pieces
        NameNode base = makeBase(gl, baseHeight, baseScale, bodyHeight, cube);
        NameNode body1 = makeBody1(gl, bodyHeight, bodyScale, sphere2);
        NameNode body2 = makeBody2(gl, bodyHeight, bodyScale, sphere1);
        NameNode body3 = makeBody3(gl, bodyHeight, bodyScale, sphere2);
        NameNode head = makeHead(gl, headScale, bodyHeight, sphere1);
        NameNode rightHand = makeRightHand(gl, armLength, armScale, sphere1);
        NameNode leftHand = makeLeftHand(gl, armLength, armScale, sphere1);
        NameNode hair = makeHair(gl, cube1);
        NameNode rightEye = makeRightEye(gl, sphere3);
        NameNode leftEye = makeLeftEye(gl, sphere3);

        // create whole robot
        robotRoot.addChild(robotMoveTranslate);
            robotMoveTranslate.addChild(robotTranslate);
            robotTranslate.addChild(base);
            robotTranslate.addChild(rotateAll);
            rotateAll.addChild(body3);
                body3.addChild(translate1);
                    translate1.addChild(rotateUpper1);
                        rotateUpper1.addChild(body2);
                            body2.addChild(translate2);
                                translate2.addChild(rotateUpper2);
                                    rotateUpper2.addChild(body1);
                                        body1.addChild(head);
                                        head.addChild(hair);
                                        head.addChild(rightEye);
                                        head.addChild(leftEye);
                                        body1.addChild(translate3);
                                            translate3.addChild(rotateUpper3);
                                                rotateUpper3.addChild(rightHand);
                                                rotateUpper3.addChild(leftHand);
            // rotateAll.addChild(body1);
            // rotateAll.addChild(body2);
            // rotateAll.addChild(head);
            //rotateAll.addChild(rightHand);
            //rotateAll.addChild(leftHand);
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

    // base
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
        m = Mat4.multiply(m, Mat4Transform.translate(0,0f,0));
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
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
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
        m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f,0));
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
        m = Mat4.multiply(m, Mat4Transform.translate(0, 1.5f/2,0));
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
        m = Mat4.multiply(m, Mat4Transform.translate(0, 1.5f,0));
        TransformNode headTransform = new TransformNode("head transform", m);
        ModelNode headShape = new ModelNode("Sphere(head)", sphere);
        head.addChild(headTransform);
        headTransform.addChild(headShape);
        return head;
    }

    private NameNode makeRightHand(GL3 gl, float armLength, float armScale, Model sphere) {
        NameNode rightHand = new NameNode("rightHand");
        Mat4 m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.translate(-1f, 0f,0));
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
        m = Mat4.multiply(m, Mat4Transform.translate(1f, 0f,0));
        m = Mat4.multiply(m, Mat4Transform.scale(armLength, armScale, armScale));
        TransformNode headTransform = new TransformNode("left hand transform", m);
        ModelNode headShape = new ModelNode("Sphere(leftHand)", sphere);
        leftHand.addChild(headTransform);
        headTransform.addChild(headShape);
        return leftHand;
    }

    private NameNode makeHair(GL3 gl, Model cube) {
        NameNode hair = new NameNode("hair");
        Mat4 m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.translate(0f, 3f,0));
        m = Mat4.multiply(m, Mat4Transform.scale(0.25f, 1.5f, 1.5f));
        TransformNode headTransform = new TransformNode("hair transform", m);
        ModelNode headShape = new ModelNode("Cube(hair)", cube);
        hair.addChild(headTransform);
        headTransform.addChild(headShape);
        return hair;

    }

    private NameNode makeRightEye (GL3 gl, Model sphere) {
        NameNode rightEye = new NameNode("rightEye");
        Mat4 m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.translate(-0.5f, 2.5f,0.7f));
        m = Mat4.multiply(m, Mat4Transform.scale(0.5f, 0.5f, 0.5f));
        TransformNode headTransform = new TransformNode("rightEye transform", m);
        ModelNode headShape = new ModelNode("Sphere(rightEye)", sphere);
        rightEye.addChild(headTransform);
        headTransform.addChild(headShape);
        return rightEye;
    }

    private NameNode makeLeftEye (GL3 gl, Model sphere) {
        NameNode leftEye = new NameNode("leftEye");
        Mat4 m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.translate(0.5f, 2.5f,0.7f));
        m = Mat4.multiply(m, Mat4Transform.scale(0.5f, 0.5f, 0.5f));
        TransformNode headTransform = new TransformNode("leftEye transform", m);
        ModelNode headShape = new ModelNode("Sphere(leftEye)", sphere);
        leftEye.addChild(headTransform);
        headTransform.addChild(headShape);
        return leftEye;
    }


    private void updateBranches() {
        double elapsedTime = getSeconds()-startTime;
        rotateAllAngle = rotateAllAngleStart*(float)Math.sin(elapsedTime);
        rotateUpperAngle = rotateUpperAngleStart*(float)Math.sin(elapsedTime*2f);
        rotateAll.setTransform(Mat4Transform.rotateAroundZ(rotateAllAngle));
        rotateUpper1.setTransform(Mat4Transform.rotateAroundZ(-rotateUpperAngle));
        rotateUpper2.setTransform(Mat4Transform.rotateAroundZ(rotateUpperAngle));
        rotateUpper3.setTransform(Mat4Transform.rotateAroundY(rotateUpperAngle));

        robotRoot.update(); // IMPORTANT – the scene graph has changed
    }


    private double startTime;

    private double getSeconds() {
        return System.currentTimeMillis()/1000.0;
    }

    public void render(GL3 gl) {
        updateBranches();
        robotRoot.draw(gl);
    }

}
