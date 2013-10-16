package edu.miami.cyto3d.graph.view;

import edu.miami.math.vector.RVec3;
import edu.miami.math.vector.RVec4;
import edu.miami.math.vector.Vec3;
import edu.miami.math.vector.Vec4;


/**
 * Basic implementation of VertexView interface.
 * 
 * @author justin
 */
public class BasicVertexView implements VertexView {

  Shape           shape        = Shape.SPHERE;
  Vec3            position     = new Vec3(0);
  Vec4            primaryColor = new Vec4(1);
  Vec4            visibleColor = primaryColor.clone();
  float           size         = 1.0f;
  boolean         visible      = true;
  boolean         pushedColor  = false;

  public BasicVertexView() {
  }

  @Override
  public RVec3 getPosition() {
    return position;
  }

  @Override
  public void setPosition(RVec3 position) {
    this.position.set(position);
  }

  @Override
  public float getSize() {
    return size;
  }

  @Override
  public void setSize(float size) {
    this.size = size;
  }

  @Override
  public RVec4 getPrimaryColor() {
    return primaryColor;
  }

  @Override
  public void setPrimaryColor(RVec4 color) {
    this.primaryColor.set(color);
    if (!pushedColor) visibleColor.set(color);
  }

  @Override
  public RVec4 getVisibleColor() {
    return visibleColor;
  }

  @Override
  public void pushColor(RVec4 color) {
    this.visibleColor.set(color);
    pushedColor = true;
  }

  @Override
  public void restoreColor() {
    this.visibleColor.set(primaryColor);
    pushedColor = false;
  }

  @Override
  public boolean isVisible() {
    return visible;
  }

  @Override
  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  @Override
  public Shape getShape() {
    return shape;
  }

  @Override
  public void setShape(Shape shape) {
    this.shape = shape;
  }

  @Override
  public VertexView lerp(VertexView other, float amount) {
    BasicVertexView view = new BasicVertexView();

    if (other == null) {
      Vec4 color = primaryColor.clone();
      color.w *= (1 - amount);
      view.setPosition(this.getPosition());
      view.setPrimaryColor(color);
      view.setSize(this.size * (1 - amount));
    } else {
      view.setPosition(Vec3.lerp(this.position, other.getPosition(), amount));
      view.setPrimaryColor(Vec4.lerp(this.primaryColor, other.getPrimaryColor(), amount));
      view.setSize(this.size + amount * (other.getSize() - this.size));
    }

    return view;
  }
}
