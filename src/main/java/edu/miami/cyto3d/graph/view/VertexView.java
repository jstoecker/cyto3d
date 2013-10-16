package edu.miami.cyto3d.graph.view;

import edu.miami.math.vector.RVec3;
import edu.miami.math.vector.RVec4;


/**
 * Generic 3D view of a vertex.
 * 
 * @author justin
 */
public interface VertexView {

  /** Possible shapes a vertex can be represented by. */
  public enum Shape {
    SPHERE,
    BOX,
    TETRAHEDRON,
    OCTAHEDRON
  }

  /** Returns the 3D position of the vertex. */
  RVec3 getPosition();

  /** Sets the 3D position of the vertex. */
  void setPosition(RVec3 position);

  /** Returns the size of the vertex shape. */
  float getSize();

  /** Sets the size of the vertex shape. */
  void setSize(float size);

  /** Returns the primary color of the vertex shape. */
  RVec4 getPrimaryColor();

  /** Sets the primary color of the shape. This is not necessarily the visible color. */
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

  /** Returns true if the vertex shape should be displayed. */
  boolean isVisible();

  /** Sets whether or not the vertex shape should be displayed. */
  void setVisible(boolean visible);

  /** Returns the shape used when displaying the vertex. */
  Shape getShape();

  /** Sets the shape used to display the vertex. */
  void setShape(Shape shape);

  /**
   * Linearly interpolate view appearance between this vertex and another. If the "other" parameter
   * is null, this should interpolate to an appearance that is invisible or "inactive". The "amount"
   * is a value in [0,1] where 0 would return this view and 1 would return the other view.
   */
  VertexView lerp(VertexView other, float amount);
}
