package edu.miami.jgloo.draw;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import edu.miami.math.vector.RVec2;
import edu.miami.math.vector.RVec2d;
import edu.miami.math.vector.RVec3;

/**
 * Circular arc
 */
public class GLCircleArc {

  public enum ThickenDirection {

    IN,
    CENTER,
    OUT,}
  float[][] verts;

  public GLCircleArc(RVec2d center, double radius, double width, double startAngle,
                     double endAngle, int numSegments, ThickenDirection thickenDir) {
    this(center.toVec2(), (float) radius, (float) width, startAngle, endAngle, numSegments, thickenDir);
  }

  public GLCircleArc(RVec2 center, float radius, float width, double startAngle, double endAngle,
                     int numSegments, ThickenDirection thickenDir) {

    // instead of calling 3D constructor, the 2D math is more efficient

    float outerR = radius;
    switch (thickenDir) {
      case CENTER:
        outerR = radius + width / 2;
        break;
      case OUT:
        outerR = radius + width;
        break;
    }
    float innerR = outerR - width;

    double angleStep = (endAngle - startAngle) / numSegments;
    double angle = startAngle;
    verts = new float[2 * (numSegments + 1)][];
    int v = 0;

    for (int i = 0; i < numSegments + 1; i++) {
      float cos = (float) Math.cos(angle);
      float sin = (float) Math.sin(angle);

      verts[v++] = new float[]{center.x() + cos * innerR, center.y() + sin * innerR, 0};
      verts[v++] = new float[]{center.x() + cos * outerR, center.y() + sin * outerR, 0};

      angle += angleStep;
    }
  }

  public GLCircleArc(RVec3 center, RVec3 up, float radius, float width, double startAngle,
                     double endAngle, int numSegments, ThickenDirection thickenDir) {
    // TODO rotMatrix arbitrary axis / use random pt to start on plane
  }

  public void draw(GL2 gl) {
    gl.glBegin(GL.GL_TRIANGLE_STRIP);
    for (int i = 0; i < verts.length; i++)
      gl.glVertex3fv(verts[i], 0);
    gl.glEnd();
  }
}
