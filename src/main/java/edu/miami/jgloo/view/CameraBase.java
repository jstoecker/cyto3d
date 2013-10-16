package edu.miami.jgloo.view;

import javax.media.opengl.GL2;
import javax.media.opengl.fixedfunc.GLMatrixFunc;

import edu.miami.jgloo.Viewport;
import edu.miami.jgloo.util.Graphics;
import edu.miami.math.geom.Ray;
import edu.miami.math.vector.Mat4;
import edu.miami.math.vector.RVec3;
import edu.miami.math.vector.Vec3;

/**
 * Base class that implements the general methods of a 3D camera. The camera is oriented with
 * respect to a world coordinate system that the user defines as either y-up or z-up. <br>
 * <br>
 * A y-up coordinate system is defined as<br>
 * right = (1,0,0)<br>
 * up = (0,1,0)<br>
 * forward = (0,0,-1)<br>
 * <br>
 * A z-up system is simply the y-up system rotated -pi/2 around the x-axis:<br>
 * right = (1,0,0)<br>
 * up = (0,0,1)<br>
 * forward = (0,1,0)<br>
 * 
 * @author Justin Stoecker
 */
public abstract class CameraBase implements Camera {

  /** up axis of world coordinate system */
  protected final Vec3    worldUp;

  /** right axis of world coordinate system */
  protected final Vec3    worldRight;

  /** forward axis of world coordinate system */
  protected final Vec3    worldForward;

  /** the up vector in world coordinates is (0,1,0); if false, it is (0,0,1) */
  protected final boolean yUp;

  /** position of the camera's center of projection in world space */
  protected Vec3          eye;

  /** forward vector of the camera in world space */
  protected Vec3          forward;

  /** right vector of the camera in world space */
  protected Vec3          right;

  /** up vector of the camera in world space */
  protected Vec3          up;

  /** projection type; the projection matrix transforms eye space -> clip space */
  protected Frustum       frustum    = new PerspectiveFrustum(45, 1, 0.1f, 300);

  /** transforms world space -> eye space */
  protected Mat4          view;

  /** transforms normalized device coordinates -> window coordinates */
  protected Viewport      viewport   = new Viewport(0, 0, 1, 1);

  /** if true, the projection's aspect ratio will always be updated to match the viewport aspect */
  protected boolean       widescreen = true;

  /**
   * Constructs the camera base.
   * 
   * @param frustum - initial projection style.
   * @param viewport - initial viewport.
   * @param yUp - if true, the y-axis is used as up; otherwise, the z-axis is up.
   */
  public CameraBase(boolean yUp) {
    this.yUp = yUp;

    if (yUp) {
      worldRight = new Vec3(1, 0, 0);
      worldUp = new Vec3(0, 1, 0);
      worldForward = new Vec3(0, 0, -1);
    } else {
      worldRight = new Vec3(1, 0, 0);
      worldUp = new Vec3(0, 0, 1);
      worldForward = new Vec3(0, 1, 0);
    }
  }

  @Override
  public RVec3 getEye() {
    return eye;
  }

  @Override
  public RVec3 getForward() {
    return forward;
  }

  @Override
  public RVec3 getBackward() {
    return forward.times(-1);
  }

  @Override
  public RVec3 getRight() {
    return right;
  }

  @Override
  public RVec3 getLeft() {
    return right.times(-1);
  }

  @Override
  public RVec3 getUp() {
    return up;
  }

  @Override
  public RVec3 getDown() {
    return up.times(-1);
  }

  @Override
  public RVec3 getForwardProjected() {
    Vec3 v = getForward().clone();
    if (yUp)
      v.y = 0;
    else
      v.z = 0;
    return v.normalize();
  }

  @Override
  public RVec3 getBackwardProjected() {
    return getForwardProjected().times(-1);
  }

  @Override
  public RVec3 getWorldUp() {
    return worldUp;
  }

  @Override
  public RVec3 getWorldDown() {
    return worldUp.times(-1);
  }

  @Override
  public RVec3 getWorldLeft() {
    return worldRight.times(-1);
  }

  @Override
  public RVec3 getWorldRight() {
    return worldRight;
  }

  @Override
  public RVec3 getWorldForward() {
    return worldForward;
  }

  @Override
  public RVec3 getWorldBackward() {
    return worldForward.times(-1);
  }

  @Override
  public Frustum getFrustum() {
    return frustum;
  }

  @Override
  public Mat4 getViewMatrix() {
    return view;
  }

  @Override
  public Mat4 getProjMatrix() {
    return frustum.getMatrix();
  }

  @Override
  public Viewport getViewport() {
    return viewport;
  }

  @Override
  public RVec3 getVector(Vector axis) {
    switch (axis) {
    case WORLD_UP:
      return getWorldUp();
    case WORLD_DOWN:
      return getWorldDown();
    case WORLD_LEFT:
      return getWorldLeft();
    case WORLD_RIGHT:
      return getWorldRight();
    case WORLD_FWD:
      return getWorldForward();
    case WORLD_BACK:
      return getWorldBackward();
    case LOCAL_UP:
      return getUp();
    case LOCAL_DOWN:
      return getDown();
    case LOCAL_LEFT:
      return getLeft();
    case LOCAL_RIGHT:
      return getRight();
    case LOCAL_FWD:
      return getForward();
    case LOCAL_BACK:
      return getBackward();
    case LOCAL_FWD_HORIZ:
      return getForwardProjected();
    case LOCAL_BACK_HORIZ:
      return getBackwardProjected();
    default:
      return null;
    }
  }

  @Override
  public Ray unproject(float winX, float winY, boolean yDown) {
    if (yDown) winY = viewport.h - winY - 1;
    return Graphics.unproject(winX, winY, view, frustum.getMatrix(), viewport);
  }

  @Override
  public Vec3 project(RVec3 p) {
    return Graphics.project(p, view, frustum.getMatrix(), viewport);
  }

  @Override
  public boolean isWidescreen() {
    return widescreen;
  }

  @Override
  public boolean isYUp() {
    return yUp;
  }

  @Override
  public void setEye(RVec3 eye) {
    this.eye.set(eye);
    updateView();
  }

  @Override
  public void setFrustum(Frustum projection) {
    this.frustum = projection;
  }

  @Override
  public void setViewport(Viewport viewport) {
    this.viewport = viewport;
    if (widescreen) setFrustum(frustum.withAspect(viewport.aspect));
  }

  @Override
  public void setWidescreen(boolean widescreen) {
    this.widescreen = widescreen;
    if (widescreen) setFrustum(frustum.withAspect(viewport.aspect));
  }

  @Override
  public void translateEye(RVec3 t) {
    setEye(eye.plus(t));
  }

  @Override
  public void apply(GL2 gl) {
    viewport.apply(gl);
    gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
    gl.glLoadMatrixf(frustum.getMatrix().values(), 0);
    gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
    gl.glLoadMatrixf(view.values(), 0);
  }

  @Override
  public Vec3[] calcFrustumCorners() {
    return frustum.calcCorners(eye, forward, right, up);
  }

  /** Calculates the view matrix. */
  protected abstract void updateView();
}
