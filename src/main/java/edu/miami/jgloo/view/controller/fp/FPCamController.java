package edu.miami.jgloo.view.controller.fp;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.KeyStroke;

import edu.miami.jgloo.view.Camera;
import edu.miami.jgloo.view.FPCamera;
import edu.miami.jgloo.view.controller.CameraController;
import edu.miami.jgloo.view.controller.Translation;

public class FPCamController extends CameraController implements FPCameraSource {

  private static final String   ROTATE_MOUSE    = "Rotate (Mouse)";
  private float                 pixelsToRadians = 0.01f;
  private int                   rotButtonMask   = MouseEvent.BUTTON1_DOWN_MASK;
  private final FPMouseRotation rotateMouse;
  private FPCamera              camera;
  private Point                 mouseAnchor;
  private float                 anchorYaw;
  private float                 anchorPitch;

  public FPCamController(boolean useDefaultKeys) {
    rotateMouse = new FPMouseRotation(ROTATE_MOUSE, this, 25, 0.05f, 0.1f);
    motionMap.put(ROTATE_MOUSE, rotateMouse);

    if (useDefaultKeys) {
      keyMap.put(KeyStroke.getKeyStroke("W"), MOVE_FWD);
      keyMap.put(KeyStroke.getKeyStroke("A"), MOVE_LEFT);
      keyMap.put(KeyStroke.getKeyStroke("S"), MOVE_BACK);
      keyMap.put(KeyStroke.getKeyStroke("D"), MOVE_RIGHT);
      keyMap.put(KeyStroke.getKeyStroke("R"), MOVE_UP);
      keyMap.put(KeyStroke.getKeyStroke("F"), MOVE_DOWN);
    }
  }

  public FPCamController(FPCamera camera, boolean useDefaultKeys) {
    this(useDefaultKeys);
    setCamera(camera);
  }

  public void setCamera(FPCamera camera) {
    this.camera = camera;
  }

  @Override
  public FPCamera getCamera() {
    return camera;
  }

  public void setRotateMask(int rotateMask) {
    this.rotButtonMask = rotateMask;
  }

  public void setPixelsToRadians(float pixelsToRadians) {
    this.pixelsToRadians = pixelsToRadians;
  }

  @Override
  public void mouseWheelMoved(MouseWheelEvent e) {
    if (e.getWheelRotation() > 0) {
      activeMotions.add(new Translation("zoom_in", this, Camera.Vector.LOCAL_BACK, 5, 0.25f, 0.1f,
          true));
    } else {
      activeMotions.add(new Translation("zoom_out", this, Camera.Vector.LOCAL_FWD, 5, 0.25f, 0.1f,
          true));
    }
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    if ((e.getModifiersEx() & rotButtonMask) == rotButtonMask) {
      Point curPt = e.getPoint();
      float dx = (mouseAnchor.x - curPt.x) * pixelsToRadians;
      float dy = (mouseAnchor.y - curPt.y) * pixelsToRadians;
      rotateMouse.setTarget(anchorPitch + dy, anchorYaw + dx);
    }
  }

  @Override
  public void mousePressed(MouseEvent e) {
    mouseAnchor = e.getPoint();
    anchorPitch = camera.getPitch();
    anchorYaw = camera.getYaw();
    rotateMouse.setAccelerating(true);
    activeMotions.add(rotateMouse);
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    rotateMouse.setAccelerating(false);
  }
}
