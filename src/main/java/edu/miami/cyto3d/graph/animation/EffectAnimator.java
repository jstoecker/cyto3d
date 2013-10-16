package edu.miami.cyto3d.graph.animation;

import java.util.LinkedList;

/**
 * Updates animations.
 * 
 * @author justin
 */
public class EffectAnimator {

  public static abstract class Animation {

    final int              duration;
    private final long     creationTime;
    private EffectAnimator animator;

    public Animation(int durationMS) {
      creationTime = System.currentTimeMillis();
      duration = durationMS;
    }

    private final void update(long currentTime) {
      float progress = (float) (currentTime - creationTime) / duration;
      updateAnimation(progress);
      if (progress >= 1.0f) end();
    }

    abstract void updateAnimation(float progress);

    private final void end() {
      animator.finishedAnimations.add(this);
      endAnimation();
    }

    protected void endAnimation() {
    }
  }

  private LinkedList<Animation> animations         = new LinkedList<Animation>();
  private LinkedList<Animation> finishedAnimations = new LinkedList<Animation>();

  public synchronized void addAnimation(Animation animation) {
    animation.animator = this;
    animations.add(animation);
  }

  public synchronized void removeAnimation(Animation animation) {
    animations.remove(animation);
  }

  public void update() {
    long currentTime = System.currentTimeMillis();
    synchronized (this) {
      for (Animation a : animations)
        a.update(currentTime);
    }
    animations.removeAll(finishedAnimations);
    finishedAnimations.clear();
  }
}
