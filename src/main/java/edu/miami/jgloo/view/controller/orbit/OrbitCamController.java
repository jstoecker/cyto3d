package edu.miami.jgloo.view.controller.orbit;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.KeyStroke;

import edu.miami.jgloo.view.Camera;
import edu.miami.jgloo.view.OrbitCamera;
import edu.miami.jgloo.view.controller.CameraController;
import edu.miami.jgloo.view.controller.CameraMotion;
import edu.miami.jgloo.view.controller.Translation;
import edu.miami.jgloo.view.controller.orbit.OrbitRotation.Angle;
import edu.miami.math.util.GMath;

/**
 * Provides and Orbit Camera and standard motions for it.
 * 
 * @author justin
 */
public class OrbitCamController extends CameraController implements OrbitCameraSource {

  public static final String         ROTATE_UP         = "Rotate Up";
  public static final String         ROTATE_DOWN       = "Rotate Down";
  public static final String         ROTATE_LEFT       = "Rotate Left";
  public static final String         ROTATE_RIGHT      = "Rotate Right";

  protected static final String      ROTATE_MOUSE      = "Rotate (Mouse)";
  protected static final float       PIXELS_TO_RADIANS = 0.005f;

  private int                        rotButtonMask     = MouseEvent.BUTTON1_DOWN_MASK;
  protected final OrbitMouseRotation rotateMouse;
  protected OrbitCamera              camera;
  protected float                    speed;
  protected Point                    mouseAnchor;
  protected float                    anchorAzimuth;
  protected float                    anchorAltitude;

  /**
   * Creates a new OrbitCamController.
   * 
   * @param speed - scales speed of rotations and translations.
   * @param useDefaultKeys - if true, default key bindings will be added.
   */
  public OrbitCamController(float speed, boolean useDefaultKeys) {

    newRotMotion(ROTATE_UP, Angle.POS_ALTITUDE, this);
    newRotMotion(ROTATE_DOWN, Angle.NEG_ALTITUDE, this);
    newRotMotion(ROTATE_LEFT, Angle.NEG_AZIMUTH, this);
    newRotMotion(ROTATE_RIGHT, Angle.POS_AZIMUTH, this);

    rotateMouse = new OrbitMouseRotation(ROTATE_MOUSE, this, speed, 0.2f, 0.2f);
    motionMap.put(ROTATE_MOUSE, rotateMouse);

    setSpeed(speed);

    if (useDefaultKeys) {
      keyMap.put(KeyStroke.getKeyStroke("W"), ROTATE_UP);
      keyMap.put(KeyStroke.getKeyStroke("A"), ROTATE_LEFT);
      keyMap.put(KeyStroke.getKeyStroke("S"), ROTATE_DOWN);
      keyMap.put(KeyStroke.getKeyStroke("D"), ROTATE_RIGHT);
      keyMap.put(KeyStroke.getKeyStroke("R"), MOVE_FWD);
      keyMap.put(KeyStroke.getKeyStroke("F"), MOVE_BACK);
    }
  }

  public void setCamera(OrbitCamera camera) {
    this.camera = camera;
  }

  public void setSpeed(float speed) {
    this.speed = speed;
    for (CameraMotion motion : motionMap.values()) {
      if (motion instanceof OrbitRotation)
        motion.setMaxSpeed(speed / 5);
      else
        motion.setMaxSpeed(speed);
    }
  }

  @Override
  public OrbitCamera getCamera() {
    return camera;
  }

  private static OrbitRotation newRotMotion(String name, Angle angle, OrbitCamController source) {
    OrbitRotation motion = new OrbitRotation(name, source, angle, source.speed, 0.25f, 0.25f,
        false, false);
    source.motionMap.put(motion.getName(), motion);
    return motion;
  }

  @Override
  public void mouseWheelMoved(MouseWheelEvent e) {
    if (e.getWheelRotation() > 0) {
      activeMotions.add(new Translation("zoom_in", this, Camera.Vector.LOCAL_BACK, speed / 4,
          0.25f, 0.25f, true));
    } else {
      activeMotions.add(new Translation("zoom_out", this, Camera.Vector.LOCAL_FWD, speed / 4,
          0.25f, 0.25f, true));
    }
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    if ((e.getModifiersEx() & rotButtonMask) == rotButtonMask) {
      Point curPt = e.getPoint();
      float dx = (mouseAnchor.x - curPt.x) * PIXELS_TO_RADIANS;
      float dy = (curPt.y - mouseAnchor.y) * PIXELS_TO_RADIANS;
      float tgtAlt = anchorAltitude + dy;
      float tgtAzi = (anchorAzimuth + dx) % GMath.PI_2;
      if (tgtAzi < 0) tgtAzi = GMath.PI_2 + tgtAzi;
      rotateMouse.setTarget(tgtAlt, tgtAzi);
    }
  }

  @Override
  public void mousePressed(MouseEvent e) {
    mouseAnchor = e.getPoint();
    anchorAltitude = camera.getAltitude();
    anchorAzimuth = camera.getAzimuth();
    rotateMouse.setAccelerating(true);
    activeMotions.add(rotateMouse);
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    rotateMouse.setAccelerating(false);
  }
}
