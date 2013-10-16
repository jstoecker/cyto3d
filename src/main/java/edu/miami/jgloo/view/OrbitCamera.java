package edu.miami.jgloo.view;

import edu.miami.math.util.GMath;
import edu.miami.math.vector.Mat4;
import edu.miami.math.vector.RVec3;
import edu.miami.math.vector.Vec3;

/**
 * Creates a view derived from an eye point and a reference point indicating the center of the
 * scene. The user can interact with the camera using a spherical coordinate system (radius,
 * altitude, azimuth), a Cartesian coordinate system (x,y,z), or both as needed.
 * 
 * @author Justin Stoecker
 */
public class OrbitCamera extends CameraBase {

  private static final float MIN_ALTITUDE = -GMath.PI_OVER_2;
  private static final float MAX_ALTITUDE = GMath.PI_OVER_2;

  /** the center point in the scene that projects to the center of the window */
  protected Vec3             center;

  /** rotation angle in radians around the world up axis */
  protected float            azimuth;

  /** rotation angle in radians between the eye and the horizontal reference plane */
  protected float            altitude;

  /** distance from eye to center */
  protected float            radius;

  /**
   * protected float azimuth; protected float altitude; protected float distance; /** Creates a new
   * LookAtCamera.
   * 
   * @param eye - eye position.
   * @param center - center position the camera is looking at.
   * @param projection - projection style.
   * @param viewport - window viewport.
   * @param yUp - if true, the y-axis is used as up; otherwise, the z-axis is up.
   */
  public OrbitCamera(Vec3 eye, Vec3 center, boolean yUp) {
    super(yUp);
    this.center = center;
    setEye(eye);
  }

  /**
   * Creates a new LookAtCamera.
   * 
   * @param radius - distance from eye to center.
   * @param altitude - rotation angle in radians between the eye and the horizon plane.
   * @param azimuth - rotation angle in radians around the world up axis.
   * @param center - center position the camera is looking at.
   * @param projection - projection style.
   * @param viewport - window viewport.
   * @param yUp - if true, the y-axis is used as up; otherwise, the z-axis is up.
   */
  public OrbitCamera(float radius, float altitude, float azimuth, Vec3 center, boolean yUp) {
    super(yUp);
    this.center = center;
    setEye(radius, altitude, azimuth);
  }

  /** Returns the central point of the scene. */
  public Vec3 getCenter() {
    return center;
  }

  /** Returns the angle (in radians) between the eye vector and the horizontal reference plane. */
  public float getAltitude() {
    return altitude;
  }

  /** Returns the radius of the eye in spherical coordinates. */
  public float getRadius() {
    return radius;
  }

  /** Returns the angle (in radians) of rotation around the world up axis */
  public float getAzimuth() {
    return azimuth;
  }

  /**
   * Sets the center of the scene.
   * 
   * @param center - sets a new center of the scene.
   * @param fixEye - if true, the eye position will remain the same; otherwise, it will be
   *          repositioned to retain the same direction vector from its current center.
   */
  public void setCenter(Vec3 center, boolean fixEye) {
    Vec3 prevCenter = this.center;
    this.center = center;
    setEye(fixEye ? eye : eye.minus(prevCenter).plus(center));
  }

  /** Sets the angle (in radians) of rotation around the world up axis */
  public void setAzimuth(float azimuth) {
    setEye(radius, altitude, azimuth);
  }

  /** Sets the angle (in radians) between the eye vector and the horizontal reference plane. */
  public void setAltitude(float altitude) {
    setEye(radius, altitude, azimuth);
  }

  /** Sets the radius of the eye in spherical coordinates. */
  public void setRadius(float radius) {
    setEye(radius, altitude, azimuth);
  }

  /**
   * Moves the eye position by transforming its spherical coordinates.
   * 
   * @param radius - change in radius.
   * @param altitude - change in altitude.
   * @param azimuth - change in azimuth.
   */
  public void translateEye(float radius, float altitude, float azimuth) {
    setEye(this.radius + radius, this.altitude + altitude, this.azimuth + azimuth);
  }

  @Override
  public void setEye(RVec3 eye) {
    if (this.eye == null)
      this.eye = eye.clone();
    else
      this.eye.set(eye);

    Vec3 eyeV = eye.minus(center);
    Vec3 spherical = GMath.cartesianToSpherical(eyeV.x, eyeV.y, eyeV.z, yUp);
    this.radius = spherical.x;
    this.altitude = spherical.y;

    // special case: preserve previous azimuth if eye is directly above or below center; otherwise,
    // it will be reset to 0 and the camera won't be able to rotate while moving up/down
    if (Math.abs(eyeV.normalize().dot(worldUp)) != 1) {
      this.azimuth = spherical.z;
    }

    updateVectors();
    updateView();
  }

  /**
   * Sets the eye position using angles and distance from center.
   * 
   * @param radius - distance from the center to the eye.
   * @param altitude - degrees rotation in the vertical reference plane.
   * @param azimuth - degrees rotation in the horizontal reference plane.
   */
  public void setEye(float radius, float altitude, float azimuth) {
    this.radius = radius;
    this.altitude = GMath.clamp(altitude, MIN_ALTITUDE, MAX_ALTITUDE);
    this.azimuth = azimuth % GMath.PI_2;
    if (this.azimuth < 0) this.azimuth = GMath.PI_2 + azimuth;
    eye = GMath.sphericalToCartesian(radius, this.altitude, this.azimuth, yUp).plus(center);
    updateVectors();
    updateView();
  }

  /** Updates the camera local coordinate space vectors. */
  private void updateVectors() {
    forward = center.minus(eye).normalize();

    float ar = altitude / GMath.PI_OVER_2;
    if (Math.abs(ar) == 1) {
      up = GMath.sphericalToCartesian(1, 0, azimuth, yUp).times(-ar);
      right = forward.cross(up).normalize();
    } else {
      right = forward.cross(worldUp).normalize();
      up = right.cross(forward).normalize();
    }
  }

  @Override
  protected void updateView() {
    view = Mat4.createLookAt(eye.x, eye.y, eye.z, center.x, center.y, center.z, up.x, up.y, up.z);
  }

  @Override
  public String toString() {
    return String.format("eye: {%.2f,%.2f,%.2f}, spherical: {%.3f, %.3f, %.3f}", eye.x, eye.y,
        eye.z, radius, altitude, azimuth);
  }
}
