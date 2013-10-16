package edu.miami.math.vector;

/**
 * Vector with four single-precision components x, y, z, and w
 * 
 * @author Justin Stoecker
 */
public class Vec4 implements RVec4 {

  public float x, y, z, w;

  public Vec4(float x, float y, float z, float w) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w = w;
  }

  public Vec4(float[] v) {
    this.x = v[0];
    this.y = v[1];
    this.z = v[2];
    this.w = v[3];
  }

  public Vec4(RVec3 v, float w) {
    this.x = v.x();
    this.y = v.y();
    this.z = v.z();
    this.w = w;
  }

  public Vec4(RVec2 v, float z, float w) {
    this.x = v.x();
    this.y = v.y();
    this.z = z;
    this.w = w;
  }

  public Vec4(float v) {
    this.x = v;
    this.y = v;
    this.z = v;
    this.w = v;
  }

  public Vec4() {
  }

  public float length() {
    return (float) Math.sqrt(dot(this));
  }

  public float lengthSquared() {
    return dot(this);
  }

  /**
   * Returns the result of this Vec4f normalized to unit length
   */
  public Vec4 normalize() {
    return over(length());
  }

  public Vec4 minus(RVec4 that) {
    return new Vec4(x - that.x(), y - that.y(), z - that.z(), w - that.w());
  }

  public Vec4 plus(RVec4 that) {
    return new Vec4(x + that.x(), y + that.y(), z + that.z(), w + that.w());
  }

  public Vec4 minus(float s) {
    return new Vec4(x - s, y - s, z - s, w - s);
  }

  public Vec4 plus(float s) {
    return new Vec4(x + s, y + s, z + s, w + s);
  }

  public Vec4 times(float s) {
    return new Vec4(x * s, y * s, z * s, w * s);
  }

  public Vec4 over(float s) {
    return new Vec4(x / s, y / s, z / s, w / s);
  }

  @Override
  public float get(int i) throws IndexOutOfBoundsException {
    switch (i) {
    case 0:
      return x;
    case 1:
      return y;
    case 2:
      return z;
    case 3:
      return w;
    }
    ;
    throw new IndexOutOfBoundsException(String.format("Index %d is outside of range [0,3]", i));
  }

  public void set(int i, float v) {
    switch (i) {
    case 0:
      x = v;
      break;
    case 1:
      y = v;
      break;
    case 2:
      z = v;
      break;
    case 3:
      w = v;
      break;
    }
  }

  @Override
  public String toString() {
    return String.format("{%.2f, %.2f, %.2f, %.2f}", x, y, z, w);
  }

  @Override
  public Vec4 clone() {
    return new Vec4(x, y, z, w);
  }

  /** Copies the values in v to this vector. */
  public void copyFrom(RVec4 v) {
    this.x = v.x();
    this.y = v.y();
    this.z = v.z();
    this.w = v.w();
  }

  /**
   * Returns a Vec4f with unit length in the X direction
   */
  public static Vec4 unitX() {
    return new Vec4(1, 0, 0, 0);
  }

  /**
   * Returns a Vec4f with unit length in the Y direction
   */
  public static Vec4 unitY() {
    return new Vec4(0, 1, 0, 0);
  }

  /**
   * Returns a Vec4f with unit length in the Z direction
   */
  public static Vec4 unitZ() {
    return new Vec4(0, 0, 1, 0);
  }

  /**
   * Returns a Vec4f with unit length in the W direction
   */
  public static Vec4 unitW() {
    return new Vec4(0, 0, 0, 1);
  }

  /**
   * Linear interpolation between two vectors
   */
  public static Vec4 lerp(RVec4 a, RVec4 b, float s) {
    return a.plus(b.minus(a).times(s));
  }

  public void add(RVec4 v) {
    x += v.x();
    y += v.y();
    z += v.z();
    w += v.w();
  }

  public void add(float v) {
    x += v;
    y += v;
    z += v;
    w += v;
  }

  public void sub(Vec4 v) {
    x -= v.x;
    y -= v.y;
    z -= v.z;
    w -= v.w;
  }

  public void sub(float v) {
    x -= v;
    y -= v;
    z -= v;
    w -= v;
  }

  public void mul(Vec4 v) {
    x *= v.x;
    y *= v.y;
    z *= v.z;
    w *= v.w;
  }

  public void mul(float v) {
    x *= v;
    y *= v;
    z *= v;
    w *= v;
  }

  public void div(Vec4 v) {
    x /= v.x;
    y /= v.y;
    z /= v.z;
    w /= v.w;
  }

  public void div(float v) {
    x /= v;
    y /= v;
    z /= v;
    w /= v;
  }

  public void maximize(RVec4 v) {
    if (v.x() > x) x = v.x();
    if (v.y() > y) y = v.y();
    if (v.z() > z) z = v.z();
    if (v.w() > w) w = v.w();
  }

  public void minimize(RVec4 v) {
    if (v.x() < x) x = v.x();
    if (v.y() < y) y = v.y();
    if (v.z() < z) z = v.z();
    if (v.w() < w) w = v.w();
  }

  @Override
  public float z() {
    return z;
  }

  @Override
  public float x() {
    return x;
  }

  @Override
  public float y() {
    return y;
  }

  @Override
  public float[] values() {
    return new float[] { x, y, z, w };
  }

  @Override
  public int size() {
    return 4;
  }

  @Override
  public float min() {
    return Math.min(Math.min(Math.min(x, y), z), w);
  }

  @Override
  public float max() {
    return Math.max(Math.max(Math.max(x, y), z), w);
  }

  @Override
  public float avg() {
    return (x + y + z + w) / 4f;
  }

  @Override
  public float w() {
    return w;
  }

  @Override
  public float dot(RVec4 that) {
    return x * that.x() + y * that.y() + z * that.z() + w * that.w();
  }

  @Override
  public Vec4 lerp(RVec4 that, float s) {
    return lerp(this, that, s);
  }

  public void set(RVec4 v) {
    set(v.x(), v.y(), v.z(), v.w());
  }

  public void set(float x, float y, float z, float w) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w = w;
  }

  public void sub(RVec4 v) {
    x -= v.x();
    y -= v.y();
    z -= v.z();
    w -= v.w();
  }
}
