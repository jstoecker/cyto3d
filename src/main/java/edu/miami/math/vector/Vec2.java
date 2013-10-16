package edu.miami.math.vector;

/**
 * Vector with two single-precision components x and y.
 * 
 * @author Justin Stoecker
 */
public class Vec2 implements RVec2 {

  public float x, y;

  public Vec2(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public Vec2(float[] c) {
    x = c[0];
    y = c[1];
  }

  public Vec2(float v) {
    x = y = v;
  }

  public Vec2() {
  }

  @Override
  public float dot(RVec2 that) {
    return x * that.x() + y * that.y();
  }

  @Override
  public float cross(RVec2 that) {
    return x * that.y() - y * that.x();
  }

  @Override
  public float length() {
    return (float) Math.sqrt(lengthSquared());
  }

  @Override
  public float lengthSquared() {
    return dot(this);
  }

  public Vec2 normalize() {
    return over(length());
  }

  @Override
  public Vec2 minus(RVec2 that) {
    return new Vec2(x - that.x(), y - that.y());
  }

  @Override
  public Vec2 plus(RVec2 that) {
    return new Vec2(x + that.x(), y + that.y());
  }

  @Override
  public Vec2 times(float s) {
    return new Vec2(x * s, y * s);
  }

  @Override
  public Vec2 over(float s) {
    return new Vec2(x / s, y / s);
  }

  @Override
  public Vec2 rot90() {
    return new Vec2(-y, x);
  }
  
  @Override
  public Vec2 times(RVec2 that) {
    return new Vec2(this.x * that.x() - this.y * that.y(), this.x * that.y() + this.y * that.x());
  }

  @Override
  public Vec2 reflect(RVec2 n) {
    return this.minus(n.times(this.dot(n)).times(2));
  }

  public void set(int i, float v) {
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

  public void add(float v) {
    x += v;
    y += v;
  }

  public void sub(RVec2 v) {
    x -= v.x();
    y -= v.y();
  }

  public void sub(float v) {
    x -= v;
    y -= v;
  }

  public void mul(RVec2 v) {
    x *= v.x();
    y *= v.y();
  }

  public void mul(float v) {
    x *= v;
    y *= v;
  }

  public void div(float v) {
    x /= v;
    y /= v;
  }

  public void maximize(RVec2 v) {
    if (v.x() > x) x = v.x();
    if (v.y() > y) y = v.y();
  }

  public void minimize(RVec2 v) {
    if (v.x() < x) x = v.x();
    if (v.y() < y) y = v.y();
  }

  @Override
  public float[] values() {
    return new float[] { x, y };
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
  public int size() {
    return 2;
  }

  @Override
  public float get(int i) {
    return (i == 0) ? x : y;
  }

  @Override
  public float min() {
    return Math.min(x, y);
  }

  @Override
  public float max() {
    return Math.max(x, y);
  }

  @Override
  public float avg() {
    return (x + y) / 2f;
  }

  public void set(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public void set(RVec2 v) {
    this.x = v.x();
    this.y = v.y();
  }

  @Override
  public Vec2 lerp(RVec2 that, float s) {
    return lerp(this, that, s);
  }

  @Override
  public Vec2 clone() {
    return new Vec2(x, y);
  }

  public static Vec2 unitX() {
    return new Vec2(1, 0);
  }

  public static Vec2 unitY() {
    return new Vec2(0, 1);
  }

  public static Vec2 zero() {
    return new Vec2();
  }

  public static Vec2 lerp(RVec2 a, RVec2 b, float s) {
    return a.plus(b.minus(a).times(s));
  }

  public void add(RVec2 v) {
    x += v.x();
    y += v.y();
  }
}
