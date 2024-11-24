public class TwoTrianglesRepeat {

    // ***************************************************
    /* THE DATA
     */
    // anticlockwise/counterclockwise ordering
    public static final float repeatFactor = 4.0f; // Controls how many times the texture repeats
    public static final float[] vertices = {
            // position         color          tex coords (scaled to repeat)
            -0.5f, 0.0f, -0.5f,  0.0f, 1.0f, 0.0f,  0.0f, repeatFactor,  // top left
            -0.5f, 0.0f,  0.5f,  0.0f, 1.0f, 0.0f,  0.0f, 0.0f,          // bottom left
            0.5f, 0.0f,  0.5f,  0.0f, 1.0f, 0.0f,  repeatFactor, 0.0f,  // bottom right
            0.5f, 0.0f, -0.5f,  0.0f, 1.0f, 0.0f,  repeatFactor, repeatFactor  // top right
    };


    public static final int[] indices = {         // Note that we start from 0!
            0, 1, 2,
            0, 2, 3
    };
}
