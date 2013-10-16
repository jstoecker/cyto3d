package edu.miami.jgloo.view;

import javax.media.opengl.GL2;

import edu.miami.jgloo.Viewport;
import edu.miami.math.geom.Ray;
import edu.miami.math.vector.Mat4;
import edu.miami.math.vector.RVec3;
import edu.miami.math.vector.Vec3;

/**
 * Interface that describes an OpenGL camera used for 3D viewing. The camera exposes several useful
 * properties that reveal its location and orientation. The "eye" position is the center of
 * projection, and the orientation of the eye's view is described by the up, forward, and right
 * vectors. Each camera also is associated with a particular projection type and viewport for
 * transforming 3D coordinates to window coordinates.
 * 
 * @author justin
 */
public interface Camera {

  /** Camera enum for querying the current state of a common vector with getVector method. */
  public enum Vector {
    WORLD_UP,
    WORLD_DOWN,
    WORLD_LEFT,
    WORLD_RIGHT,
    WORLD_FWD,
    WORLD_BACK,
    LOCAL_UP,
    LOCAL_DOWN,
    LOCAL_LEFT,
    LOCAL_RIGHT,
    LOCAL_FWD,
    LOCAL_BACK,
    LOCAL_FWD_HORIZ,
    LOCAL_BACK_HORIZ,
  }

  /** Returns the eye position. */
  RVec3 getEye();

  /** Returns the forward vector in world space coordinates. */
  RVec3 getForward();

  /** Returns the negative forward vector in world space coordinates. */
  RVec3 getBackward();

  /** Returns the right vector in world space coordinates. */
  RVec3 getRight();

  /** Returns the negative right vector in world space coordinates. */
  RVec3 getLeft();

  /** Returns the up vector in world space coordinates. */
  RVec3 getUp();

  /** Returns the negative up vector in world space coordinates. */
  RVec3 getDown();

  /** Returns the forward vector projected onto the horizontal world plane and normalized. */
  RVec3 getForwardProjected();

  /** Returns the backward vector projected onto the horizontal world plane and normalized. */
  RVec3 getBackwardProjected();

  /** Returns the up axis of the world coordinate system. */
  RVec3 getWorldUp();

  /** Returns the negative up axis of the world coordinate system. */
  RVec3 getWorldDown();

  /** Returns the negative right axis of the world coordinate system. */
  RVec3 getWorldLeft();

  /** Returns the right axis of the world coordinate system. */
  RVec3 getWorldRight();

  /** Returns the forward axis of the world coordinate system. */
  RVec3 getWorldForward();

  /** Returns the negative forward axis of the world coordinate system. */
  RVec3 getWorldBackward();

  /**
   * Returns a commonly used camera vector. This returns the same vectors as the specialized getter
   * methods, but it can be used in a switch statement.
   */
  RVec3 getVector(Vector axis);

  /** Returns the viewing frustum. */
  Frustum getFrustum();

  /** Returns the view matrix. */
  Mat4 getViewMatrix();

  /** Returns the projection matrix. This is the same as getFrustum().getMatrix(). */
  Mat4 getProjMatrix();

  /** Returns the viewport. */
  Viewport getViewport();
  
  /** The aspect ratio of the projection matches the viewport. */
  boolean isWidescreen();
  
  /** Y is used as the up vector; if false, it is Z. */
  boolean isYUp();
  
  /**
   * Transforms 2D window coordinates to a 3D ray in world space coordinates.
   * 
   * @param winX - window coordinate x
   * @param winY - window coordinate y
   * @param yDown - true if the y-axis of the window originates from the top of the screen
   */
  Ray unproject(float winX, float winY, boolean yDown);

  /** Transforms a 3D point from world space coordinates to 2D window coordinates. */
  Vec3 project(RVec3 p);

  /** Sets the eye position. */
  void setEye(RVec3 eye);

  /** Sets the projection style. */
  void setFrustum(Frustum frustum);

  /** Sets the viewport. */
  void setViewport(Viewport viewport);

  /**
   * If enabled, the aspect ratio of the projection will match the viewport; otherwise, the image
   * will simply scretch to fit the viewport.
   */
  void setWidescreen(boolean widescreen);

  /** Translates the eye position by a vector in world space coordinates. */
  void translateEye(RVec3 t);

  /** Sets OpenGL states for viewing. */
  void apply(GL2 gl);
  
  /** Returns the frustum corners. See Frustum.calcCorners() for more detail. */
  Vec3[] calcFrustumCorners();
}
