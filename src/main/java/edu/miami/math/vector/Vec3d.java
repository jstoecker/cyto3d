package edu.miami.math.vector;

/**
 * Vector with three double-precision components x, y, and z
 * 
 * @author Justin Stoecker
 */
public class Vec3d implements RVec3d {

  public double x, y, z;
  
  public Vec3d(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Vec3d(double[] v) {
    this.x = v[0];
    this.y = v[1];
    this.z = v[2];
  }

  public Vec3d(RVec2d v, double z) {
    this.x = v.x();
    this.y = v.y();
    this.z = z;
  }

  public Vec3d(double v) {
    this.x = v;
    this.y = v;
    this.z = v;
  }

  public Vec3d() {
  }

  @Override
  public double dot(RVec3d that) {
    return x * that.x() + y * that.y() + z * that.z();
  }

  @Override
  public Vec3d cross(RVec3d that) {
    double x = this.y * that.z() - this.z * that.y();
    double y = this.z * that.x() - this.x * that.z();
    double z = this.x * that.y() - this.y * that.x();

    return new Vec3d(x, y, z);
  }

  @Override
  public double length() {
    return (double) Math.sqrt(dot(this));
  }

  @Override
  public double lengthSquared() {
    return dot(this);
  }

  public Vec3d normalize() {
    return over(length());
  }

  @Override
  public Vec3d minus(RVec3d that) {
    return new Vec3d(x - that.x(), y - that.y(), z - that.z());
  }

  @Override
  public Vec3d plus(RVec3d that) {
    return new Vec3d(x + that.x(), y + that.y(), z + that.z());
  }

  public Vec3d minus(double s) {
    return new Vec3d(x - s, y - s, z - s);
  }

  public Vec3d plus(double s) {
    return new Vec3d(x + s, y + s, z + s);
  }

  @Override
  public Vec3d times(double s) {
    return new Vec3d(x * s, y * s, z * s);
  }

  @Override
  public Vec3d over(double s) {
    return new Vec3d(x / s, y / s, z / s);
  }

  @Override
  public Vec3d reflect(RVec3d n) {
    return this.minus(n.times(this.dot(n)).times(2.0f));
  }

  @Override
  public double get(int i) throws IndexOutOfBoundsException {
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

  public void set(int i, double v) {
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
  public Vec3d clone() {
    return new Vec3d(x, y, z);
  }

  public static Vec3d unitX() {
    return new Vec3d(1, 0, 0);
  }

  public static Vec3d unitY() {
    return new Vec3d(0, 1, 0);
  }

  public static Vec3d unitZ() {
    return new Vec3d(0, 0, 1);
  }

  public static Vec3d zero() {
    return new Vec3d();
  }

  public static Vec3d lerp(RVec3d a, RVec3d b, double s) {
    return a.plus(b.minus(a).times(s));
  }

  public void add(double v) {
    x += v;
    y += v;
    z += v;
  }

  public void sub(RVec3d v) {
    x -= v.x();
    y -= v.y();
    z -= v.z();
  }

  public void sub(double v) {
    x -= v;
    y -= v;
    z -= v;
  }

  public void mul(double v) {
    x *= v;
    y *= v;
    z *= v;
  }

  public void div(double v) {
    x /= v;
    y /= v;
    z /= v;
  }

  public void maximize(RVec3d v) {
    if (v.x() > x) x = v.x();
    if (v.y() > y) y = v.y();
    if (v.z() > z) z = v.z();
  }

  public void minimize(RVec3d v) {
    if (v.x() < x) x = v.x();
    if (v.y() < y) y = v.y();
    if (v.z() < z) z = v.z();
  }

  @Override
  public double x() {
    return x;
  }

  @Override
  public double y() {
    return y;
  }

  @Override
  public double z() {
    return z;
  }

  @Override
  public double[] values() {
    return new double[] { x, y, z };
  }

  @Override
  public int size() {
    return 3;
  }

  @Override
  public double min() {
    return Math.min(Math.min(x, y), z);
  }

  @Override
  public double max() {
    return Math.max(Math.max(x, y), z);
  }

  @Override
  public double avg() {
    return (x + y + z) / 3f;
  }

  public boolean equals(RVec3d that) {
    return (x == that.x() && y == that.y() && z == that.z());
  }

  @Override
  public Vec3d lerp(RVec3d that, double s) {
    return lerp(this, that, s);
  }

  public void set(RVec3d v) {
    this.x = v.x();
    this.y = v.y();
    this.z = v.z();
  }

  public void set(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public void add(RVec3d v) {
    x += v.x();
    y += v.y();
    z += v.z();
  }

  @Override
  public Vec3 toVec3() {
    return new Vec3((float)x, (float)y, (float)z);
  }
}
