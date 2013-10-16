package edu.miami.jgloo.view;

import javax.media.opengl.GL2;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.media.opengl.glu.GLU;

import edu.miami.jgloo.Viewport;
import edu.miami.math.vector.RVec3;
import edu.miami.math.vector.Vec3;

/**
 * Wraps a Camera instance and exposes additional methods for stereoscopic 3D viewing. The
 * projection style of the original camera is ignored, although its near and far parameters are used
 * here.
 * 
 * @author justin
 */
public class StereoCamera {

  private Camera baseCamera;
  private GLU    glu         = new GLU();

  private float  focalLength = 2.0f;
  private float  fovX        = 45.0f;
  private float  eyeSep      = 0.1f;

  public StereoCamera(Camera baseCamera) {
    this.baseCamera = baseCamera;
  }

  /**
   * Returns the distance between the camera's eye and the plane where objects are in focus (zero
   * parallax).
   */
  public float getFocalLength() {
    return focalLength;
  }

  public void setFocalLength(float focalLength) {
    this.focalLength = focalLength;
  }

  /** Returns the camera's aperture, or horizontal field of view (degrees). */
  public float getFovX() {
    return fovX;
  }

  public void setFovX(float fovX) {
    this.fovX = fovX;
  }

  /** Returns the distance between left and right eye positions. */
  public float getEyeSeparation() {
    return eyeSep;
  }

  public void setEyeSeparation(float eyeSep) {
    this.eyeSep = eyeSep;
  }

  public Camera getCamera() {
    return baseCamera;
  }

  public void setBaseCamera(Camera baseCamera) {
    this.baseCamera = baseCamera;
  }

  /** Applies right eye projection/modelview matrices */
  public void applyRight(GL2 gl) {
    Viewport vp = baseCamera.getViewport();
    float near = baseCamera.getFrustum().getZNear();
    float far = baseCamera.getFrustum().getZFar();

    gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
    gl.glLoadIdentity();
    vp.apply(gl);

    double wd2 = near * Math.tan(Math.toRadians(fovX) / 2);
    double top = wd2;
    double bottom = -wd2;
    double left = -vp.getAspect() * wd2 - 0.5 * eyeSep * near / focalLength;
    double right = vp.getAspect() * wd2 - 0.5 * eyeSep * near / focalLength;
    gl.glFrustum(left, right, bottom, top, near, far);

    gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
    gl.glLoadIdentity();
    Vec3 eye = baseCamera.getEye().plus(baseCamera.getRight().times(eyeSep / 2));
    RVec3 up = baseCamera.getUp();
    Vec3 center = eye.plus(baseCamera.getForward());
    glu.gluLookAt(eye.x, eye.y, eye.z, center.x, center.y, center.z, up.x(), up.y(), up.z());
  }

  /** Applies left eye projection/modelview matrices */
  public void applyLeft(GL2 gl) {
    Viewport vp = baseCamera.getViewport();
    float near = baseCamera.getFrustum().getZNear();
    float far = baseCamera.getFrustum().getZFar();

    gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
    gl.glLoadIdentity();
    vp.apply(gl);

    double wd2 = near * Math.tan(Math.toRadians(fovX) / 2);
    double top = wd2;
    double bottom = -wd2;
    double left = -vp.getAspect() * wd2 + 0.5 * eyeSep * near / focalLength;
    double right = vp.getAspect() * wd2 + 0.5 * eyeSep * near / focalLength;
    gl.glFrustum(left, right, bottom, top, near, far);

    gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
    gl.glLoadIdentity();
    Vec3 eye = baseCamera.getEye().plus(baseCamera.getLeft().times(eyeSep / 2));
    RVec3 up = baseCamera.getUp();
    Vec3 t = eye.plus(baseCamera.getForward());
    glu.gluLookAt(eye.x, eye.y, eye.z, t.x, t.y, t.z, up.x(), up.y(), up.z());
  }
}
