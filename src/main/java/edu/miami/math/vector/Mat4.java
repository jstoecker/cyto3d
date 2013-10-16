package edu.miami.math.vector;

import java.nio.FloatBuffer;

/**
 * A 4x4 matrix with single-precision values. Contains static methods for constructing common
 * transformation and projection matrices.
 * 
 * @author Justin Stoecker
 */
public class Mat4 {

  private float[] m = new float[16];

  /**
   * Creates a 4x4 matrix with all elements set to 0.
   */
  public Mat4() {
  }

  /**
   * Creates a 4x4 matrix with all elements set to the same value
   * 
   * @param v - the value for each element of the matrix
   */
  public Mat4(float v) {
    for (int i = 0; i < 16; i++)
      m[i] = v;
  }

  /**
   * Creates a 4x4 matrix
   * 
   * @param a - the 16 elements of the matrix in column-major form
   */
  public Mat4(float[] m) {
    this.m = m;
  }

  /** Transforms (v.x, v.y, v.z, 1) by this matrix. */
  public Vec3 transform(RVec3 v) {
    return transform(v.x(), v.y(), v.z());
  }

  public Vec4 transform(RVec4 v) {
    return transform(v.x(), v.y(), v.z(), v.w());
  }

  /** Transformation with w = 1 */
  public Vec3 transform(float x, float y, float z) {
    float nx = (float) (x * m[0] + y * m[4] + z * m[8] + m[12]);
    float ny = (float) (x * m[1] + y * m[5] + z * m[9] + m[13]);
    float nz = (float) (x * m[2] + y * m[6] + z * m[10] + m[14]);
    return new Vec3(nx, ny, nz);
  }

  public Vec4 transform(float x, float y, float z, float w) {
    float nx = (float) (x * m[0] + y * m[4] + z * m[8] + w * m[12]);
    float ny = (float) (x * m[1] + y * m[5] + z * m[9] + w * m[13]);
    float nz = (float) (x * m[2] + y * m[6] + z * m[10] + w * m[14]);
    float nw = (float) (x * m[3] + y * m[7] + z * m[11] + w * m[15]);
    return new Vec4(nx, ny, nz, nw);
  }

  /**
   * Multiplies the current matrix by another matrix
   * 
   * @param that - the second matrix in the multiplication
   * @return the result of multiplying the two matrices
   */
  public Mat4 times(Mat4 that) {
    Mat4 mat = new Mat4();

    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        float sum = 0;
        for (int k = 0; k < 4; k++) {
          sum += m[i + 4 * k] * that.m[k + 4 * j];
        }
        mat.m[i + 4 * j] = sum;
      }
    }

    return mat;
  }

  /**
   * Creates an identity matrix
   */
  public static Mat4 createIdentity() {
    Mat4 mat = new Mat4();
    for (int i = 0; i < 4; i++)
      mat.m[i + 4 * i] = 1;
    return mat;
  }

  /**
   * Creates a translation matrix
   * 
   * @param t - the translation vector (Tx, Ty, Tz)
   * @return a translation matrix that translates by the vector t
   */
  public static Mat4 createTranslation(RVec3 t) {
    return createTranslation(t.x(), t.y(), t.z());
  }

  public static Mat4 createTranslation(float x, float y, float z) {
    return new Mat4(new float[] { 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, x, y, z, 1 });
  }

  /**
   * Creates a scale matrix
   * 
   * @param s - the scaling for x, y, and z components
   * @return the scale matrix that scales by s
   */
  public static Mat4 createScale(RVec3 s) {
    return new Mat4(new float[] { s.x(), 0, 0, 0, 0, s.y(), 0, 0, 0, 0, s.z(), 0, 0, 0, 0, 1 });
  }

  /**
   * Creates a rotation matrix using the x-axis
   * 
   * @param radians - the rotation angle in radians
   * @return the rotation matrix
   */
  public static Mat4 createRotationX(double radians) {
    float c = (float) Math.cos(radians);
    float s = (float) Math.sin(radians);
    return new Mat4(new float[] { 1, 0, 0, 0, 0, c, s, 0, 0, -s, c, 0, 0, 0, 0, 1 });
  }

  /**
   * Creates a rotation matrix using the y-axis
   * 
   * @param radians - the rotation angle in radians
   * @return the rotation matrix
   */
  public static Mat4 createRotationY(double radians) {
    float c = (float) Math.cos(radians);
    float s = (float) Math.sin(radians);
    return new Mat4(new float[] { c, 0, -s, 0, 0, 1, 0, 0, s, 0, c, 0, 0, 0, 0, 1 });
  }

  /**
   * Creates a rotation matrix using the z-axis
   * 
   * @param radians - the rotation angle in radians
   * @return the rotation matrix
   */
  public static Mat4 createRotationZ(double radians) {
    float c = (float) Math.cos(radians);
    float s = (float) Math.sin(radians);
    return new Mat4(new float[] { c, s, 0, 0, -s, c, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 });
  }

  /**
   * Creates a rotation matrix that allows rotation about an arbitrary axis
   * 
   * @param radians - the rotation angle in radians
   * @param u - the axis to rotate about
   * @return the rotation matrix
   * @see http://inside.mines.edu/~gmurray/ArbitraryAxisRotation/
   */
  public static Mat4 createRotation(double radians, RVec3 u) {
    float c = (float) Math.cos(radians);
    float s = (float) Math.sin(radians);
    float u2 = u.x() * u.x();
    float v2 = u.y() * u.y();
    float w2 = u.z() * u.z();
    float d = u2 + v2 + w2;
    float ic = 1 - c;
    float sqrtDs = (float) Math.sqrt(d) * s;

    float[] a = { (u2 + (v2 + w2) * c) / d, (u.x() * u.y() * ic + u.z() * sqrtDs) / d,
        (u.x() * u.z() * ic - u.y() * sqrtDs) / d, 0, (u.x() * u.y() * ic - u.z() * sqrtDs) / d,
        (v2 + (u2 + w2) * c) / d, (u.y() * u.z() * ic + u.x() * sqrtDs) / d, 0,
        (u.x() * u.z() * ic + u.y() * sqrtDs) / d, (u.y() * u.z() * ic - u.x() * sqrtDs) / d,
        (w2 + (u2 + v2) * c) / d, 0, 0, 0, 0, 1 };

    return new Mat4(a);
  }

  /**
   * Creates a rotation matrix using yaw (y-axis rotation), pitch (x-axis rotation), and roll
   * (z-axis rotation)
   */
  public static Mat4 createYawPitchRoll(double yaw, double pitch, double roll) {
    float a = (float) Math.cos(yaw);
    float b = (float) Math.sin(yaw);
    float c = (float) Math.cos(pitch);
    float d = (float) Math.sin(pitch);
    float f = (float) Math.cos(roll);
    float g = (float) Math.sin(roll);

    float[] vals = { f * a + d * g * b, c * g, d * g * a - f * b, 0, -g * a + d * f * b, c * f,
        d * f * a + g * b, 0, c * b, -d, c * a, 0, 0, 0, 0, 1 };
    return new Mat4(vals);
  }

  /**
   * Creates a perspective projection matrix that may be off-axis and asymmetric. Same as
   * glFrustum(left,right,bottom,top,near,far). Parameters are values with respect to (0,0,near) in
   * camera space.
   * 
   * @param left - distance from eye to left clipping plane at near depth
   * @param right - distance from eye to right clipping plane at near depth
   * @param top - distance from eye to top clipping plane at near depth
   * @param bottom - distance from eye to bottom clipping plane at near depth
   * @param near - distance from eye to near clipping plane
   * @param far - distance from eye to far clipping plane
   * @return
   */
  public static Mat4 createPerspective(float left, float right, float bottom, float top,
      float near, float far) {

    float rightMinusLeft = right - left;
    float topMinusBottom = top - bottom;
    float farMinusNear = far - near;
    float twoNear = 2 * near;

    float A = (right + left) / rightMinusLeft;
    float B = (top + bottom) / topMinusBottom;
    float C = -(far + near) / farMinusNear;
    float D = -twoNear * far / farMinusNear;

    float[] m = { twoNear / rightMinusLeft, 0, 0, 0, 0, twoNear / topMinusBottom, 0, 0, A, B, C,
        -1, 0, 0, D, 0, };

    return new Mat4(m);
  }

  /**
   * Creates a perspective projection matrix.
   * 
   * @param fovY - vertical field of view (degrees)
   * @param aspect - aspect ratio of frustum
   * @param near - distance from eye to near clipping plane
   * @param far - distance from eye to far clipping plane
   * @return
   */
  public static Mat4 createPerspective(float fovY, float aspect, float near, float far) {
    float f = (float) (1.0 / Math.tan(Math.toRadians(fovY) / 2));
    float nearMinusFar = near - far;

    float[] m = { f / aspect, 0, 0, 0, 0, f, 0, 0, 0, 0, (far + near) / nearMinusFar, -1, 0, 0,
        (2 * far * near) / nearMinusFar, 0 };

    return new Mat4(m);
  }

  /**
   * Creates an orthographic projection matrix.
   * 
   * @param left - distance from eye to left clipping plane at near depth
   * @param right - distance from eye to right clipping plane at near depth
   * @param top - distance from eye to top clipping plane at near depth
   * @param bottom - distance from eye to bottom clipping plane at near depth
   * @param near - distance from eye to near clipping plane
   * @param far - distance from eye to far clipping plane
   * @return
   */
  public static Mat4 createOrtho(float left, float right, float bottom, float top, float near,
      float far) {

    float rightMinusLeft = right - left;
    float topMinusBottom = top - bottom;
    float farMinusNear = far - near;

    float tx = -(right + left) / rightMinusLeft;
    float ty = -(top + bottom) / topMinusBottom;
    float tz = -(far + near) / farMinusNear;

    float[] m = { 2.0f / rightMinusLeft, 0, 0, 0, 0, 2.0f / topMinusBottom, 0, 0, 0, 0,
        -2.0f / farMinusNear, 0, tx, ty, tz, 1.0f };

    return new Mat4(m);
  }

  /**
   * Creates an orthographic projection matrix for use in 2D rendering. Same as createOrtho with
   * near = -1 and far = 1.
   * 
   * @param left - distance from eye to left clipping plane at near depth
   * @param right - distance from eye to right clipping plane at near depth
   * @param top - distance from eye to top clipping plane at near depth
   * @param bottom - distance from eye to bottom clipping plane at near depth
   * @return
   */
  public static Mat4 createOrtho2D(float left, float right, float bottom, float top) {
    return createOrtho(left, right, bottom, top, -1, 1);
  }

  /**
   * Creates a viewing matrix derived from an eye point, a reference point indicating the center of
   * the scene, and an UP vector.
   * 
   * @return
   */
  public static Mat4 createLookAt(float eyeX, float eyeY, float eyeZ, float centerX, float centerY,
      float centerZ, float upX, float upY, float upZ) {

    Vec3 f = new Vec3(centerX - eyeX, centerY - eyeY, centerZ - eyeZ);
    f = f.normalize();

    Vec3 up = new Vec3(upX, upY, upZ);
    up = up.normalize();

    Vec3 s = f.cross(up).normalize();
    Vec3 u = s.cross(f).normalize();

    float[] m = { s.x, u.x, -f.x, 0, s.y, u.y, -f.y, 0, s.z, u.z, -f.z, 0, 0, 0, 0, 1 };

    Mat4 m1 = new Mat4(m);
    Mat4 m2 = Mat4.createTranslation(-eyeX, -eyeY, -eyeZ);

    return m1.times(m2);
  }

  public static Mat4 createOrientation(RVec3 x, RVec3 y, RVec3 z) {
    float[] m = { x.x(), y.x(), z.x(), 0, x.y(), y.y(), z.y(), 0, x.z(), y.z(), z.z(), 0, 0, 0, 0,
        1 };
    return new Mat4(m);
  }

  /**
   * Returns the values of the matrix in a float buffer
   */
  public FloatBuffer wrap() {
    FloatBuffer buf = FloatBuffer.allocate(m.length);
    for (int i = 0; i < buf.capacity(); i++)
      buf.put((float) m[i]);
    buf.rewind();
    return buf;
  }

  public Vec4 getColumn(int i) {
    return new Vec4(m[i], m[i + 1], m[i + 2], m[i + 3]);
  }

  public float get(int row, int col) {
    return m[col * 4 + row];
  }

  public void set(int row, int col, float val) {
    m[col * 4 + row] = val;
  }

  public float[] values() {
    return m;
  }

  public Mat4 transpose() {
    float[] m2 = new float[16];
    for (int i = 0; i < 4; i++)
      for (int j = 0; j < 4; j++)
        m2[4 * i + j] = m[4 * j + i];
    return new Mat4(m2);
  }

  @Override
  public String toString() {
    StringBuilder b = new StringBuilder();
    for (int i = 0; i < 4; i++)
      b.append(String.format("%5.1f %5.1f %5.1f %5.1f\n", m[i], m[i + 4], m[i + 8], m[i + 12]));
    return b.toString();
  }
}
