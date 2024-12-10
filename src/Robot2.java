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
    private TransformNode robotMoveTranslate, robotMove, rotateAll;
    private float rotateAllAngleStart = 25, rotateAllAngle = rotateAllAngleStart;
    private float rotateUpperAngleStart = -60, rotateUpperAngle = rotateUpperAngleStart;

    public Robot2(GL3 gl, Camera c, Light[] l, Texture t0, Texture t1) {
        camera = c;
        lights = l;
        this.t0 = t0; // body
        this.t1 = t1; // eye
        //this.t2 = t2;
        //this.t3 = t3;
    }

    public void initialise(GL3 gl) {
        cube = makeCube(gl, t0); // body
        sphere1 = makeSphere(gl, t1); // eye

        float bodyScale = 1.0f;
        float bodyLength = 3.0f;
        float antennaHeight = 2.5f;
        float antennaScale = 0.25f;


        robotRoot = new NameNode("root");
        robotMoveTranslate = new TransformNode("robot transform", Mat4Transform.translate(0f,0.5f,0f));

        robotMove = new TransformNode("robot move", Mat4Transform.translate(0f,0f,0f));

        NameNode body = makeBody(gl, bodyLength, bodyScale, cube);
        NameNode rightEye = makeRightEye(gl, sphere1);
        NameNode leftEye = makeLeftEye(gl, sphere1);
        NameNode antenna = makeAntenna(gl, antennaHeight, antennaScale, cube);

        robotRoot.addChild(robotMoveTranslate);
            robotMoveTranslate.addChild(robotMove);
                robotMove.addChild(body);
                robotMove.addChild(rightEye);
                robotMove.addChild(leftEye);
                robotMove.addChild(antenna);

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

    private NameNode makeRightEye (GL3 gl, Model sphere) {
        NameNode rightEye = new NameNode("rightEye");
        Mat4 m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.translate(-0.5f, 0f,1.5f));
        m = Mat4.multiply(m, Mat4Transform.scale(0.5f, 0.5f, 0.5f));
        m = Mat4.multiply(m, Mat4Transform.rotateAroundX(180));
        // m = Mat4.multiply(m, Mat4Transform.rotateAroundZ(90));
        TransformNode headTransform = new TransformNode("rightEye transform", m);
        ModelNode headShape = new ModelNode("Sphere(rightEye)", sphere);
        rightEye.addChild(headTransform);
        headTransform.addChild(headShape);
        return rightEye;
    }

    private NameNode makeLeftEye (GL3 gl, Model sphere) {
        NameNode leftEye = new NameNode("leftEye");
        Mat4 m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.translate(0.5f, 0f,1.5f));
        m = Mat4.multiply(m, Mat4Transform.scale(0.5f, 0.5f, 0.5f));
        m = Mat4.multiply(m, Mat4Transform.rotateAroundX(180));
        // m = Mat4.multiply(m, Mat4Transform.rotateAroundZ(90));
        TransformNode headTransform = new TransformNode("leftEye transform", m);
        ModelNode headShape = new ModelNode("Sphere(leftEye)", sphere);
        leftEye.addChild(headTransform);
        headTransform.addChild(headShape);
        return leftEye;
    }

    private NameNode makeAntenna(GL3 gl, float antennaHeight, float antennaScale, Model cube) {
        NameNode antenna = new NameNode("antenna");
        Mat4 m = Mat4Transform.scale(antennaScale, antennaHeight, antennaScale);
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        TransformNode baseTransform = new TransformNode("antenna transform", m);
        ModelNode bodyShape = new ModelNode("Cube(antenna)", cube);
        antenna.addChild(baseTransform);
        baseTransform.addChild(bodyShape);
        return antenna;
    }


    private double startTime;

    private double getSeconds() {
        return System.currentTimeMillis()/1000.0;
    }

    private static final float SPEED = 0.2f;        // Movement speed
    private static final float ROTATION_SPEED = 2f; // Degrees per frame for smooth rotation
    private static final float PAUSE_DURATION = 1.0f; // Pause duration in seconds

    public float x = 6f;        // Current x position
    public float z = -7f;       // Current z position
    private int state = 0;       // Current movement state (0=up, 1=left, 2=down, 3=right)
    private float rotationAngle = 0f;  // Current rotation angle
    private float targetRotationAngle = 0f; // Target rotation angle
    private boolean isPaused = false;   // Pause state flag
    private double pauseStartTime = 0;  // Time when the pause started

    private void updateMovement() {
        double currentTime = getSeconds();

        if (isPaused) {
            handlePause(currentTime);
            return;
        }

        // Update position based on the current state
        switch (state) {
            case 0: // Move from (6, -7) to (6, 7)
                z += SPEED;
                if (z >= 7f) {
                    z = 7f; // Snap to target
                    prepareForPause((rotationAngle - 90f)); // Turn left
                    state = 1; // Next state
                }
                break;
            case 1: // Move from (6, 7) to (-6, 7)
                x -= SPEED;
                if (x <= -6f) {
                    x = -6f; // Snap to target
                    prepareForPause((rotationAngle - 90f)); // Turn left
                    state = 2; // Next state
                }
                break;
            case 2: // Move from (-6, 7) to (-6, -7)
                z -= SPEED;
                if (z <= -7f) {
                    z = -7f; // Snap to target
                    prepareForPause((rotationAngle - 90f)); // Turn left
                    state = 3; // Next state
                }
                break;
            case 3: // Move from (-6, -7) to (6, -7)
                x += SPEED;
                if (x >= 6f) {
                    x = 6f; // Snap to target
                    prepareForPause((rotationAngle - 90f)); // Turn left
                    state = 0; // Loop back to the first state
                }
                break;
        }

        // Update transformation to reflect movement
        updateTransform();
    }

    private void prepareForPause(float newTargetAngle) {
        isPaused = true; // Enter pause state
        pauseStartTime = getSeconds(); // Record pause start time
        targetRotationAngle = newTargetAngle; // Set the target rotation angle
    }

    private void handlePause(double currentTime) {
        // Rotate slowly toward the target rotation angle
        if (Math.abs(rotationAngle - targetRotationAngle) > ROTATION_SPEED) {
            if (rotationAngle < targetRotationAngle) {
                rotationAngle += ROTATION_SPEED;
            } else {
                rotationAngle -= ROTATION_SPEED;
            }
        } else {
            rotationAngle = targetRotationAngle; // Snap to the target
        }

        // Wait for the pause duration to complete
        if (currentTime - pauseStartTime >= PAUSE_DURATION && rotationAngle == targetRotationAngle) {
            isPaused = false; // Resume movement
        }

        // Update transformation to reflect the current rotation
        updateTransform();
    }

    private void updateTransform() {
        Mat4 translation = Mat4Transform.translate(x, 0f, z);
        Mat4 rotation = Mat4Transform.rotateAroundY(rotationAngle);
        robotMove.setTransform(Mat4.multiply(translation, rotation));

        robotRoot.update(); // IMPORTANT – the scene graph has changed
    }

    public int robot2Move = 0; // 0 - Move, 1 - Stop

    public void render(GL3 gl) {
        if (robot2Move == 0) {

            updateMovement();
        }
        robotRoot.draw(gl);
    }

}
