package edu.miami.cyto3d.graph.view;

import edu.miami.math.vector.RVec4;
import edu.miami.math.vector.Vec3;


/**
 * Generic view for a 3D edge.
 * 
 * @author justin
 */
public interface EdgeView {

  public enum Style {
    SOLID,
    DOTTED,
  }

  /** Returns a view for the source vertex. */
  VertexView getSourceView();

  /** Returns a view for the target vertex. */
  VertexView getTargetView();

  /** Returns the visual style of the edge. */
  Style getStyle();

  /** Sets the visual style of the edge. */
  void setStyle(Style style);

  /** Returns the width of the edge. */
  float getWidth();

  /** Sets the width of the edge. */
  void setWidth(float width);

  /** Returns the primary color of the edge. */
  RVec4 getPrimaryColor();

  /** Sets the primary color of the edge. This is not necessarily the visible color. */
  void setPrimaryColor(RVec4 color);

  /**
   * Returns the current color that a user should see. This will be the same as getColor() unless a
   * temporary color has been pushed using pushColor().
   */
  RVec4 getVisibleColor();

  /**
   * Overrides the visible color without changing the primary color. The primary color can be
   * restored by calling restoreColor().
   */
  void pushColor(RVec4 color);

  /** Resets the visible color to the primary color, reversing the effect of a pushColor() call. */
  void restoreColor();

  /** Calculates the current position of the center point on the edge. */
  Vec3 calcCenter();

  /** Calculates the current length of the edge. */
  float calcLength();

  /** Returns a vector from the source to target vertex views. */
  Vec3 calcVector();

  /**
   * Performs linear interpolation between this view and another. If other is null, this view should
   * return an appearance that is interpolated between this and invisible or "inactive".
   */
  EdgeView lerp(GraphView graphView, EdgeView other, float amount);
}
