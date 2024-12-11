import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JFrame;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

public class Spacecraft extends JFrame implements ActionListener{
  
  private static final int WIDTH = 1024;
  private static final int HEIGHT = 768;
  private static final Dimension dimension = new Dimension(WIDTH, HEIGHT);
  private GLCanvas canvas;
  private Spacecraft_GLEventListener glEventListener;
  private final FPSAnimator animator; 

  public static void main(String[] args) {
    Spacecraft b1 = new Spacecraft("Spacecraft");
    b1.getContentPane().setPreferredSize(dimension);
    b1.pack();
    b1.setVisible(true);
    b1.canvas.requestFocusInWindow();
  }

  public Spacecraft(String textForTitleBar) {
    super(textForTitleBar);
    setUpCanvas();
    getContentPane().add(canvas, BorderLayout.CENTER);
    addWindowListener(new windowHandler());
    animator = new FPSAnimator(canvas, 60);
    animator.start();

  }

  private void setUpCanvas() {
    GLCapabilities glcapabilities = new GLCapabilities(GLProfile.get(GLProfile.GL3));
    canvas = new GLCanvas(glcapabilities);
    Camera camera = new Camera(Camera.DEFAULT_POSITION,
        Camera.DEFAULT_TARGET, Camera.DEFAULT_UP);
    glEventListener = new Spacecraft_GLEventListener(camera);
    canvas.addGLEventListener(glEventListener);
    canvas.addMouseMotionListener(new MyMouseInput(camera));
    canvas.addKeyListener(new MyKeyboardInput(camera));
    getContentPane().add(canvas, BorderLayout.CENTER);


    // User Interface

    JMenuBar menuBar=new JMenuBar();
    this.setJMenuBar(menuBar);
      JMenu fileMenu = new JMenu("File");
        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(this);
        fileMenu.add(quitItem);
    menuBar.add(fileMenu);

    JPanel mainPanel = new JPanel(new GridLayout(2, 6, 10, 5));
    JPanel p = new JPanel();
    JPanel p2 = new JPanel();

    JButton b = new JButton("On General Light");
      b.addActionListener(this);
    p.add(b);
    b = new JButton("Dim General Light");
      b.addActionListener(this);
    p.add(b);
    b = new JButton("Off General Light");
      b.addActionListener(this);
    p.add(b);
    b = new JButton("On Spotlight");
    b.addActionListener(this);
    p.add(b);
    b = new JButton("Dim Spotlight");
    b.addActionListener(this);
    p.add(b);
    b = new JButton("Off Spotlight");
    b.addActionListener(this);
    p.add(b);
    b = new JButton("Move Robot 1");
      b.addActionListener(this);
    p2.add(b);
    b = new JButton("Stop Robot 1");
      b.addActionListener(this);
    p2.add(b);
    b = new JButton("Move/Stop Robot 2");
      b.addActionListener(this);
    p2.add(b);

    mainPanel.add(p);
    mainPanel.add(p2);

    this.add(mainPanel, BorderLayout.SOUTH);

  }

  // Function for User Interface

  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equalsIgnoreCase("quit")) {
      System.exit(0);
    }
    else if (e.getActionCommand().equalsIgnoreCase("move robot 1")){
      glEventListener.robot1Move();
    }
    else if (e.getActionCommand().equalsIgnoreCase("stop robot 1")){
      glEventListener.robot1Stop();
    }
    else if (e.getActionCommand().equalsIgnoreCase("move/stop robot 2")){
      glEventListener.robot2Move();
    }
    else if (e.getActionCommand().equalsIgnoreCase("dim general light")){
      glEventListener.dimGeneralLight();
    }
    else if (e.getActionCommand().equalsIgnoreCase("off general light")){
      glEventListener.offGeneralLight();
    }
    else if (e.getActionCommand().equalsIgnoreCase("on general light")){
      glEventListener.onGeneralLight();
    }
    else if (e.getActionCommand().equalsIgnoreCase("dim spotlight")){
      glEventListener.dimSpotlight();
    }
    else if (e.getActionCommand().equalsIgnoreCase("off spotlight")){
      glEventListener.offSpotlight();
    }
    else if (e.getActionCommand().equalsIgnoreCase("on spotlight")){
      glEventListener.onSpotlight();
    }
  }

  private class windowHandler extends WindowAdapter {
    public void windowClosing(WindowEvent e) {
      animator.stop();
      remove(canvas);
      dispose();
      System.exit(0);
    }
  }
}

class MyKeyboardInput extends KeyAdapter  {
  private Camera camera;
  
  public MyKeyboardInput(Camera camera) {
    this.camera = camera;
  }
  
  public void keyPressed(KeyEvent e) {
    Camera.Movement m = Camera.Movement.NO_MOVEMENT;
    switch (e.getKeyCode()) {
      case KeyEvent.VK_LEFT:  m = Camera.Movement.LEFT;  break;
      case KeyEvent.VK_RIGHT: m = Camera.Movement.RIGHT; break;
      case KeyEvent.VK_UP:    m = Camera.Movement.UP;    break;
      case KeyEvent.VK_DOWN:  m = Camera.Movement.DOWN;  break;
      case KeyEvent.VK_A:  m = Camera.Movement.FORWARD;  break;
      case KeyEvent.VK_Z:  m = Camera.Movement.BACK;  break;
    }
    camera.keyboardInput(m);
  }
}

class MyMouseInput extends MouseMotionAdapter {
  private Point lastpoint;
  private Camera camera;
  
  public MyMouseInput(Camera camera) {
    this.camera = camera;
  }
  
    /**
   * mouse is used to control camera position
   *
   * @param e  instance of MouseEvent
   */    
  public void mouseDragged(MouseEvent e) {
    Point ms = e.getPoint();
    float sensitivity = 0.001f;
    float dx=(float) (ms.x-lastpoint.x)*sensitivity;
    float dy=(float) (ms.y-lastpoint.y)*sensitivity;
    //System.out.println("dy,dy: "+dx+","+dy);
    if (e.getModifiersEx()==MouseEvent.BUTTON1_DOWN_MASK)
      camera.updateYawPitch(dx, -dy);
    lastpoint = ms;
  }

  /**
   * mouse is used to control camera position
   *
   * @param e  instance of MouseEvent
   */  
  public void mouseMoved(MouseEvent e) {   
    lastpoint = e.getPoint(); 
  }

}