package edu.miami.jgloo.view.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.media.opengl.awt.GLCanvas;
import javax.swing.InputMap;
import javax.swing.KeyStroke;

import edu.miami.jgloo.view.Camera;
import edu.miami.jgloo.view.Camera.Vector;
import edu.miami.util.MouseCombo;

/**
 * Generic interface for smooth motion control of 3D cameras. This class addresses timing issues
 * that arise when using the event methods of an AWT KeyListener and MouseListener interfaces:<br>
 * <br>
 * In a KeyListener, each KeyEvent is received at a discrete interval chosen by the OS that is often
 * much higher than the rendering framerate. If a camera translation, for instance, is put directly
 * inside a keyPressed() method, the camera speed will be tied to the OS' key event speed; this
 * leads to inconsistent camera speed on different machines and if the interval is high enough the
 * camera motion will seem choppy.<br>
 * <br>
 * Instead of placing camera motion code in the event methods themselves, these methods simply set a
 * boolean state "pressed" to true or false. To resolve the choppy camera motions, this class has an
 * update() method that may be called before each rendering pass. Each update, the key states can be
 * queried and the camera moved. By synchronizing the camera motion with the framerate of the OpenGL
 * program, the camera may be moved more often than the OS key event interval.<br>
 * <br>
 * Having the camera speed tied to the framerate is still a problem, because this framerate can
 * change as the program runs and will vary on other machines. Instead, the camera speeds are
 * defined in terms of real time: rotation speed in radians per second and translation speed in 3D
 * units per second. The update() method will measure the elapsed time since its last call and scale
 * the motions accordingly.
 * 
 * @author justin
 */
public abstract class CameraController implements KeyListener, MouseListener, MouseMotionListener,
    MouseWheelListener, CameraSource {

  public static final String  MOVE_UP          = "Move Up";
  public static final String  MOVE_DOWN        = "Move Down";
  public static final String  MOVE_LEFT        = "Move Left";
  public static final String  MOVE_RIGHT       = "Move Right";
  public static final String  MOVE_FWD         = "Move Forward";
  public static final String  MOVE_BACK        = "Move Back";
  public static final String  MOVE_WORLD_UP    = "Move Up (World)";
  public static final String  MOVE_WORLD_DOWN  = "Move Down (World)";
  public static final String  MOVE_WORLD_LEFT  = "Move Left (World)";
  public static final String  MOVE_WORLD_RIGHT = "Move Right (World)";
  public static final String  MOVE_WORLD_FWD   = "Move Forward (World)";
  public static final String  MOVE_WORLD_BACK  = "Move Back (World)";
  public static final String  MOVE_FWD_HORIZ   = "Move Forward (Horizontal)";
  public static final String  MOVE_BACK_HORIZ  = "Move Back (Horizontal)";

  // bindings from keys to motion names
  protected final InputMap                      keyMap         = new InputMap();

  // bindings from mouse input to motion names
  protected final HashMap<MouseCombo, String>   mouseMap       = new HashMap<MouseCombo, String>();

  // contains permanent motions provided by the controller
  protected final HashMap<String, CameraMotion> motionMap      = new HashMap<String, CameraMotion>();

  // motions that are being updated
  protected final Set<CameraMotion>             activeMotions  = new HashSet<CameraMotion>();

  private long                                  lastUpdateTime = -1;
  private float                                 elapsedSec;
  
  protected final Translation moveU            = newT(MOVE_UP, Vector.LOCAL_UP, this);
  protected final Translation moveD            = newT(MOVE_DOWN, Vector.LOCAL_DOWN, this);
  protected final Translation moveL            = newT(MOVE_LEFT, Vector.LOCAL_LEFT, this);
  protected final Translation moveR            = newT(MOVE_RIGHT, Vector.LOCAL_RIGHT, this);
  protected final Translation moveF            = newT(MOVE_FWD, Vector.LOCAL_FWD, this);
  protected final Translation moveB            = newT(MOVE_BACK, Vector.LOCAL_BACK, this);
  protected final Translation moveWU           = newT(MOVE_WORLD_UP, Vector.WORLD_UP, this);
  protected final Translation moveWD           = newT(MOVE_WORLD_DOWN, Vector.WORLD_DOWN, this);
  protected final Translation moveWL           = newT(MOVE_WORLD_LEFT, Vector.WORLD_LEFT, this);
  protected final Translation moveWR           = newT(MOVE_WORLD_RIGHT, Vector.WORLD_RIGHT, this);
  protected final Translation moveWF           = newT(MOVE_WORLD_FWD, Vector.WORLD_FWD, this);
  protected final Translation moveWB           = newT(MOVE_WORLD_BACK, Vector.WORLD_BACK, this);
  protected final Translation moveFH           = newT(MOVE_FWD_HORIZ, Vector.LOCAL_FWD_HORIZ, this);
  protected final Translation moveBH           = newT(MOVE_BACK_HORIZ, Vector.LOCAL_BACK_HORIZ,
                                                   this);

  public void add(CameraMotion motion) {
    activeMotions.add(motion);
  }

  public final void update() {
    if (lastUpdateTime > 0) {
      elapsedSec = (float) (System.currentTimeMillis() - lastUpdateTime) / 1000.0f;

      preUpdate();
      
      if (elapsedSec > 0) {
        for (Iterator<CameraMotion> i = activeMotions.iterator(); i.hasNext();) {
          if (!i.next().update(elapsedSec)) i.remove();
        }
      }
      
      postUpdate();
    }
    lastUpdateTime = System.currentTimeMillis();
  }
  
  protected void preUpdate() {
  }
  
  protected void postUpdate() {
  }

  @Override
  public void keyPressed(KeyEvent e) {
    Object motionKey = keyMap.get(KeyStroke.getKeyStroke(e.getKeyCode(), e.getModifiers()));
    if (motionKey != null) {
      CameraMotion motion = motionMap.get(motionKey);
      motion.setAccelerating(true);
      activeMotions.add(motion);
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    Object motionKey = keyMap.get(KeyStroke.getKeyStroke(e.getKeyCode(), e.getModifiers()));
    if (motionKey != null) {
      motionMap.get(motionKey).setAccelerating(false);
    }
  }

  public void put(KeyStroke keystroke, String motion) {
    keyMap.put(keystroke, motion);
  }

  public void remove(KeyStroke keystroke) {
    keyMap.remove(keystroke);
  }

  public void removeAll() {
    keyMap.clear();
  }

  public ArrayList<KeyStroke> getKeyStrokes(String motion) {
    ArrayList<KeyStroke> keystrokes = new ArrayList<KeyStroke>();
    for (KeyStroke ks : keyMap.keys()) {
      String mappedMotion = keyMap.get(ks).toString();
      if (mappedMotion.equals(motion)) {
        keystrokes.add(ks);
      }
    }
    return keystrokes;
  }
  
  public void attach(GLCanvas canvas) {
    canvas.addKeyListener(this);
    canvas.addMouseListener(this);
    canvas.addMouseMotionListener(this);
    canvas.addMouseWheelListener(this);
  }

  public void detach(GLCanvas canvas) {
    canvas.removeKeyListener(this);
    canvas.removeMouseListener(this);
    canvas.removeMouseMotionListener(this);
    canvas.removeMouseWheelListener(this);
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  @Override
  public void mouseWheelMoved(MouseWheelEvent e) {
  }

  @Override
  public void mouseDragged(MouseEvent e) {
  }

  @Override
  public void mouseMoved(MouseEvent e) {
  }

  @Override
  public void mouseClicked(MouseEvent e) {
  }

  @Override
  public void mouseEntered(MouseEvent e) {
  }

  @Override
  public void mouseExited(MouseEvent e) {
  }

  @Override
  public void mousePressed(MouseEvent e) {
  }

  @Override
  public void mouseReleased(MouseEvent e) {
  }
  
  private static Translation newT(String name, Camera.Vector vector, CameraController source) {
    Translation motion = new Translation(name, source, vector, 5, 0.25f, 0.25f, false);
    source.motionMap.put(motion.name, motion);
    return motion;
  }
}
