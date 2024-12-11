import com.jogamp.opengl.GL3;

public class ModelNode extends SGNode {

  protected ModelMultipleLights model;
  protected Light spotlight;

  public ModelNode(String name, ModelMultipleLights m) {
    super(name);
    model = m; 
  }

  public ModelNode(String name, Light spotlight2) {
    super(name);
    spotlight = spotlight2;
  }

  public void draw(GL3 gl) {

    if (spotlight != null) {
      spotlight.render(gl, worldTransform);

    } else {
      model.render(gl, worldTransform);
    }

    for (int i = 0; i < children.size(); i++) {
      children.get(i).draw(gl);
    }
  }
}