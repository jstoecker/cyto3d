package edu.miami.math.vector;

/**
 * Vector with three single-precision components x, y, and z
 * 
 * @author Justin Stoecker
 */
public class Vec3 implements RVec3 {

  public float x, y, z;
  
  public Vec3(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Vec3(float[] v) {
    this.x = v[0];
    this.y = v[1];
    this.z = v[2];
  }

  public Vec3(RVec2 v, float z) {
    this.x = v.x();
    this.y = v.y();
    this.z = z;
  }

  public Vec3(float v) {
    this.x = v;
    this.y = v;
    this.z = v;
  }

  public Vec3() {
  }

  @Override
  public float dot(RVec3 that) {
    return x * that.x() + y * that.y() + z * that.z();
  }

  @Override
  public Vec3 cross(RVec3 that) {
    float x = this.y * that.z() - this.z * that.y();
    float y = this.z * that.x() - this.x * that.z();
    float z = this.x * that.y() - this.y * that.x();

    return new Vec3(x, y, z);
  }

  @Override
  public float length() {
    return (float) Math.sqrt(dot(this));
  }

  @Override
  public float lengthSquared() {
    return dot(this);
  }

  public Vec3 normalize() {
    return over(length());
  }

  @Override
  public Vec3 minus(RVec3 that) {
    return new Vec3(x - that.x(), y - that.y(), z - that.z());
  }

  @Override
  public Vec3 plus(RVec3 that) {
    return new Vec3(x + that.x(), y + that.y(), z + that.z());
  }

  public Vec3 minus(float s) {
    return new Vec3(x - s, y - s, z - s);
  }

  public Vec3 plus(float s) {
    return new Vec3(x + s, y + s, z + s);
  }

  @Override
  public Vec3 times(float s) {
    return new Vec3(x * s, y * s, z * s);
  }

  @Override
  public Vec3 over(float s) {
    return new Vec3(x / s, y / s, z / s);
  }

  @Override
  public Vec3 reflect(RVec3 n) {
    return this.minus(n.times(this.dot(n)).times(2.0f));
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
    }
    throw new IndexOutOfBoundsException(String.format("Index %d is outside of range [0,2]", i));
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
    }
  }

  @Override
  public String toString() {
    return String.format("{%.2f, %.2f, %.2f}", x, y, z);
  }

  @Override
  public Vec3 clone() {
    return new Vec3(x, y, z);
  }

  public static Vec3 unitX() {
    return new Vec3(1, 0, 0);
  }

  public static Vec3 unitY() {
    return new Vec3(0, 1, 0);
  }

  public static Vec3 unitZ() {
    return new Vec3(0, 0, 1);
  }

  public static Vec3 zero() {
    return new Vec3();
  }

  public static Vec3 lerp(RVec3 a, RVec3 b, float s) {
    return a.plus(b.minus(a).times(s));
  }

  public void add(float v) {
    x += v;
    y += v;
    z += v;
  }

  public void sub(RVec3 v) {
    x -= v.x();
    y -= v.y();
    z -= v.z();
  }

  public void sub(float v) {
    x -= v;
    y -= v;
    z -= v;
  }

  public void mul(float v) {
    x *= v;
    y *= v;
    z *= v;
  }

  public void div(float v) {
    x /= v;
    y /= v;
    z /= v;
  }

  public void maximize(RVec3 v) {
    if (v.x() > x) x = v.x();
    if (v.y() > y) y = v.y();
    if (v.z() > z) z = v.z();
  }

  public void minimize(RVec3 v) {
    if (v.x() < x) x = v.x();
    if (v.y() < y) y = v.y();
    if (v.z() < z) z = v.z();
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
  public float z() {
    return z;
  }

  @Override
  public float[] values() {
    return new float[] { x, y, z };
  }

  @Override
  public int size() {
    return 3;
  }

  @Override
  public float min() {
    return Math.min(Math.min(x, y), z);
  }

  @Override
  public float max() {
    return Math.max(Math.max(x, y), z);
  }

  @Override
  public float avg() {
    return (x + y + z) / 3f;
  }

  public boolean equals(RVec3 that) {
    return (x == that.x() && y == that.y() && z == that.z());
  }

  @Override
  public Vec3 lerp(RVec3 that, float s) {
    return lerp(this, that, s);
  }

  public void set(RVec3 v) {
    this.x = v.x();
    this.y = v.y();
    this.z = v.z();
  }

  public void set(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public void add(RVec3 v) {
    x += v.x();
    y += v.y();
    z += v.z();
  }
}
