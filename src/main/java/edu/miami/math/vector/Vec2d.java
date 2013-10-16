package edu.miami.math.vector;

/**
 * Vector with two double-precision components x and y.
 * 
 * @author Justin Stoecker
 */
public class Vec2d implements RVec2d {

  public double x, y;

  public Vec2d(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public Vec2d(double[] c) {
    x = c[0];
    y = c[1];
  }

  public Vec2d(double v) {
    x = y = v;
  }

  public Vec2d() {
  }

  @Override
  public double dot(RVec2d that) {
    return x * that.x() + y * that.y();
  }

  @Override
  public double cross(RVec2d that) {
    return x * that.y() - y * that.x();
  }

  @Override
  public double length() {
    return (double) Math.sqrt(lengthSquared());
  }

  @Override
  public double lengthSquared() {
    return dot(this);
  }

  public Vec2d normalize() {
    return over(length());
  }

  @Override
  public Vec2d minus(RVec2d that) {
    return new Vec2d(x - that.x(), y - that.y());
  }

  @Override
  public Vec2d plus(RVec2d that) {
    return new Vec2d(x + that.x(), y + that.y());
  }

  @Override
  public Vec2d times(double s) {
    return new Vec2d(x * s, y * s);
  }

  @Override
  public Vec2d over(double s) {
    return new Vec2d(x / s, y / s);
  }

  @Override
  public Vec2d rot90() {
    return new Vec2d(-y, x);
  }
  
  @Override
  public Vec2d times(RVec2d that) {
    return new Vec2d(this.x * that.x() - this.y * that.y(), this.x * that.y() + this.y * that.x());
  }

  @Override
  public Vec2d reflect(RVec2d n) {
    return this.minus(n.times(this.dot(n)).times(2));
  }

  public void set(int i, double v) {
    if (i == 0)
      x = v;
    else if (i == 1)
      y = v;
    else
      throw new IndexOutOfBoundsException();
  }

  @Override
  public String toString() {
    return String.format("{%.3f, %.3f}", x, y);
  }

  public void add(double v) {
    x += v;
    y += v;
  }

  public void sub(RVec2d v) {
    x -= v.x();
    y -= v.y();
  }

  public void sub(double v) {
    x -= v;
    y -= v;
  }

  public void mul(RVec2d v) {
    x *= v.x();
    y *= v.y();
  }

  public void mul(double v) {
    x *= v;
    y *= v;
  }

  public void div(double v) {
    x /= v;
    y /= v;
  }

  public void maximize(RVec2d v) {
    if (v.x() > x) x = v.x();
    if (v.y() > y) y = v.y();
  }

  public void minimize(RVec2d v) {
    if (v.x() < x) x = v.x();
    if (v.y() < y) y = v.y();
  }

  @Override
  public double[] values() {
    return new double[] { x, y };
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
  public int size() {
    return 2;
  }

  @Override
  public double get(int i) {
    return (i == 0) ? x : y;
  }

  @Override
  public double min() {
    return Math.min(x, y);
  }

  @Override
  public double max() {
    return Math.max(x, y);
  }

  @Override
  public double avg() {
    return (x + y) / 2f;
  }

  public void set(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public void set(RVec2d v) {
    this.x = v.x();
    this.y = v.y();
  }

  @Override
  public Vec2d lerp(RVec2d that, double s) {
    return lerp(this, that, s);
  }

  @Override
  public Vec2d clone() {
    return new Vec2d(x, y);
  }

  public static Vec2d unitX() {
    return new Vec2d(1, 0);
  }

  public static Vec2d unitY() {
    return new Vec2d(0, 1);
  }

  public static Vec2d zero() {
    return new Vec2d();
  }

  public static Vec2d lerp(RVec2d a, RVec2d b, double s) {
    return a.plus(b.minus(a).times(s));
  }

  public void add(RVec2d v) {
    x += v.x();
    y += v.y();
  }
  
  @Override
  public Vec2 toVec2() {
    return new Vec2((float)x, (float)y);
  }
}
