package edu.miami.math.util;

import java.util.List;

import edu.miami.math.vector.RVec3;
import edu.miami.math.vector.Vec2;
import edu.miami.math.vector.Vec3;

/**
 * Miscellaneous graphics math methods.
 * 
 * @author Justin Stoecker
 */
public class GMath {

  public static final float PI        = (float) (Math.PI);
  public static final float PI_OVER_2 = (float) (Math.PI / 2);
  public static final float PI_OVER_3 = (float) (Math.PI / 3);
  public static final float PI_OVER_4 = (float) (Math.PI / 4);
  public static final float PI_2      = (float) (Math.PI * 2);

  public static float sin(float radians) {
    return (float) Math.sin(radians);
  }

  public static float cos(float radians) {
    return (float) Math.cos(radians);
  }

  public static float tan(float radians) {
    return (float) Math.tan(radians);
  }

  public static float toRadians(float degrees) {
    return PI * degrees / 180f;
  }

  /** Returns angle in radians between two vectors a and b in [0,pi] */
  public static double vecAngle(RVec3 a, RVec3 b, RVec3 up) {
    Vec3 n = a.cross(b);
    double rads = Math.atan2(n.length(), a.dot(b));
    if (n.dot(up) > 0) rads *= -1;
    return rads;
  }
  
  public static double vecAngle(RVec3 a, RVec3 b) {
    Vec3 n = a.cross(b);
    double rads = Math.atan2(n.length(), a.dot(b));
    return rads;
  }

  /**
   * Calculates rotations (in radians) for absolute pitch (rotation x) and yaw (rotation y) for an
   * object at position a to point toward b
   */
  public static Vec2 calcPitchYaw(Vec3 a, Vec3 b) {
    // v1 = -z axis = (0,0,-1)
    // v2 = b-a = (b.x - a.x, 0, b.z - a.z)
    // rotation y = 2pi - atan2(v2.z, v2.x) - atan2(v1.z, v1.x) =
    // = 2pi - atan2(b.z - a.z, b.x - a.x) - atan2(-1,0)
    // = 2pi - atan2(b.z - a.z, b.x - a.x) - pi/2
    // = 3pi/2 - atan2(b.z - a.z, b.x - a.x)

    double yawRads = 3 * PI_OVER_2 - Math.atan2(b.z - a.z, b.x - a.x);
    float yaw = (float) Math.toDegrees(yawRads);

    Vec3 v = b.minus(a);
    double pitchRads = Math.atan2(v.y, Math.sqrt(v.x * v.x + v.z * v.z));
    float pitch = (float) Math.toDegrees(pitchRads);

    return new Vec2(pitch, yaw);
  }

  /**
   * Clamps a double value to be within a specified range
   */
  public static double clamp(double val, double min, double max) {
    if (val < min) return min;
    if (val > max) return max;
    return val;
  }

  /**
   * Clamps a float value to be within a specified range
   */
  public static float clamp(float val, float min, float max) {
    if (val < min) return min;
    if (val > max) return max;
    return val;
  }

  /**
   * Clamps an integer value to be within a specified range
   */
  public static int clamp(int val, int min, int max) {
    if (val < min) return min;
    if (val > max) return max;
    return val;
  }

  /**
   * Returns the point in a list that is nearest to another point
   */
  public static Vec3 getNearest(Vec3 a, List<Vec3> list) {
    float minD = 0;
    Vec3 nearest = null;
    for (int i = 0; i < list.size(); i++) {
      float d = list.get(i).minus(a).lengthSquared();
      if (nearest == null || d < minD) {
        minD = d;
        nearest = list.get(i);
      }
    }
    return nearest;
  }

  public static Vec2 rndVec2f(float min, float max) {
    float x = (float) Math.random() * (max - min) + min;
    float y = (float) Math.random() * (max - min) + min;
    return new Vec2(x, y);
  }

  public static Vec3 rndVec3f(float min, float max) {
    float x = (float) Math.random() * (max - min) + min;
    float y = (float) Math.random() * (max - min) + min;
    float z = (float) Math.random() * (max - min) + min;
    return new Vec3(x, y, z);
  }

  /** Converts spherical coordinates to Cartesian using a y-up orientation. */
  public static Vec3 sphericalToCartesian(float radius, float altitude, float azimuth, boolean yUp) {
    float x, y, z;
    if (yUp) {
      x = (float) (radius * Math.cos(altitude) * Math.sin(azimuth));
      y = (float) (radius * Math.sin(altitude));
      z = (float) (radius * Math.cos(azimuth) * Math.cos(altitude));
    } else {
      x = (float) (radius * Math.cos(altitude) * Math.sin(azimuth));
      y = -(float) (radius * Math.cos(azimuth) * Math.cos(altitude));
      z = (float) (radius * Math.sin(altitude));
    }
    return new Vec3(x, y, z);
  }

  /** Converts Cartesian coordinates to spherical (radius, altitude, azimuth). */
  public static Vec3 cartesianToSpherical(float x, float y, float z, boolean yUp) {
    float radius, altitude, azimuth;
    radius = (float) Math.sqrt(x * x + y * y + z * z);
    if (yUp) {
      altitude = (float) Math.asin(y / radius);
      azimuth = (float) Math.atan2(x, z);
      if (azimuth < 0) azimuth += PI_2;
    } else {
      altitude = (float) Math.asin(z / radius);
      azimuth = (float) Math.atan2(x, -y);
      if (azimuth < 0) azimuth += PI_2;
    }
    return new Vec3(radius, altitude, azimuth);
  }

}
