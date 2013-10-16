package edu.miami.cyto3d.graph.animation;

import edu.miami.cyto3d.graph.view.EdgeView;
import edu.miami.math.vector.Vec4;

public class EdgeFade extends LinearInterpolation {

  final EdgeView view;

  public EdgeFade(EdgeView view, int duration, boolean fadeIn) {
    super(fadeIn ? 0 : 1, fadeIn ? 1 : 0, duration);
    this.view = view;
  }

  @Override
  void updateAnimation(float progress) {
    super.updateAnimation(progress);
    Vec4 color = view.getPrimaryColor().clone();
    color.w = curValue;
    view.pushColor(color);
  }
}