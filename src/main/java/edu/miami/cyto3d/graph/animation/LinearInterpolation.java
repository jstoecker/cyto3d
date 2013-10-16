package edu.miami.cyto3d.graph.animation;

import edu.miami.cyto3d.graph.animation.EffectAnimator.Animation;

/**
 * Animation that fades
 * 
 * @author justin
 */
public abstract class LinearInterpolation extends Animation {

  final float     a;
  final float     b;
  protected float curValue;

  public LinearInterpolation(float a, float b, int duration) {
    super(duration);
    this.a = a;
    this.b = b;
  }

  @Override
  void updateAnimation(float progress) {
    curValue = a + (b - a) * progress;
  }
}
