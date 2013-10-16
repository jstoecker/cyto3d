/*******************************************************************************
 * Copyright 2011 Justin Stoecker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package edu.miami.jgloo.draw;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import com.jogamp.opengl.util.awt.TextRenderer;

import edu.miami.jgloo.Viewport;
import edu.miami.jgloo.view.Camera;
import edu.miami.jgloo.view.Frustum;
import edu.miami.math.geom.Polygon;
import edu.miami.math.geom.Ray;
import edu.miami.math.geom.Triangle;
import edu.miami.math.vector.RVec2;
import edu.miami.math.vector.RVec3;
import edu.miami.math.vector.Vec3;

/**
 * Contains methods for drawing primitive shapes. For performance reasons, many of these methods
 * should avoided. It is far more efficient to batch calls of multiple primitives rather than
 * processing them one at a time. The primary use of these methods is minimizing code for testing /
 * debug purposes.
 * 
 * @author Justin
 */
public class Draw {

  public static void ray(GL2 gl, Ray r) {
    Vec3 a = r.getPosition();
    Vec3 b = a.plus(r.getDirection().times(Float.MAX_VALUE));

    gl.glBegin(GL.GL_LINES);
    gl.glVertex3f(a.x, a.y, a.z);
    gl.glVertex3f(b.x, b.y, b.z);
    gl.glEnd();
    gl.glBegin(GL.GL_POINTS);
    gl.glVertex3f(a.x, a.y, a.z);
    gl.glEnd();
  }

  public static void polygon(GL2 gl, Polygon p) {
    gl.glBegin(GL2.GL_POLYGON);
    for (int i = 0; i < p.v.length; i++)
      gl.glVertex3fv(p.v[i].values(), 0);
    gl.glEnd();
  }

  public static void triangle(GL2 gl, Triangle t) {
    gl.glBegin(GL2.GL_TRIANGLES);
    for (int i = 0; i < 3; i++)
      gl.glVertex3fv(t.v[i].values(), 0);
    gl.glEnd();
  }

  /** Draws a single line between start and end. */
  public static void line(GL2 gl, Vec3 start, Vec3 end) {
    gl.glBegin(GL.GL_LINES);
    gl.glVertex3f(start.x, start.y, start.z);
    gl.glVertex3f(end.x, end.y, end.z);
    gl.glEnd();
  }
  
  /** Draws a curve using GL_LINE_STRIP */
  public static void arc(GL2 gl, RVec2 center, float radius, double startAngle,
      double endAngle, int numSegments) {

    double angleInc = (endAngle - startAngle) / numSegments;
    double angle = startAngle;

    gl.glBegin(GL2.GL_LINE_STRIP);
    for (int i = 0; i < numSegments + 1; i++) {
      double x = center.x() + Math.cos(angle) * radius;
      double y = center.y() + Math.sin(angle) * radius;

      gl.glVertex2d(x, y);

      angle += angleInc;
    }
    gl.glEnd();
  }

  /** Draws the world X,Y,Z axes */
  public static void axes(GL2 gl, double length) {
    gl.glBegin(GL.GL_LINES);
    gl.glColor3f(0.5f, 0, 0);
    gl.glVertex3d(0, 0, 0);
    gl.glVertex3d(length, 0, 0);
    gl.glColor3f(0, 0.5f, 0);
    gl.glVertex3d(0, 0, 0);
    gl.glVertex3d(0, length, 0);
    gl.glColor3f(0, 0, 0.5f);
    gl.glVertex3d(0, 0, 0);
    gl.glVertex3d(0, 0, length);
    gl.glEnd();
  }

  /**
   * Draws a square grid on the XZ plane
   * 
   * @param size - size of the side of the square grid
   * @param spacing - spacing units between grid lines
   * @param color - color of the grid
   */
  public static void grid(GL2 gl, double size, double spacing) {
    gl.glBegin(GL.GL_LINES);
    double i = -size;
    while (i <= size) {
      gl.glVertex3d(i, 0, -size);
      gl.glVertex3d(i, 0, size);
      gl.glVertex3d(-size, 0, i);
      gl.glVertex3d(size, 0, i);
      i += spacing;
    }
    ;
    gl.glEnd();
  }

  /**
   * Draws text on the screen
   * 
   * @param tr - text renderer object
   * @param text - text to be displayed
   * @param x - x coordinate on screen
   * @param y - y coordinate on screen
   */
  public static void text(TextRenderer tr, Viewport vp, String text, int x, int y) {
    String[] lines = text.split("\n");

    tr.beginRendering(vp.getW(), vp.getH());
    // for (int i = 0; i < lines.length; i++)
    // tr.draw(lines[i], x - 1, y - tr.getFont().getSize() * i - 1);
    for (int i = 0; i < lines.length; i++)
      tr.draw(lines[i], x, y - tr.getFont().getSize() * i);
    tr.endRendering();
  }

  public static void frustum(GL2 gl, Camera camera) {
    frustum(gl, camera.getFrustum(), camera.getEye(), camera.getForward(), camera.getUp(),
        camera.getRight());
  }

  public static void frustum(GL2 gl, Frustum frustum, RVec3 eye, RVec3 forward,
      RVec3 up, RVec3 right) {
    Vec3[] v = frustum.calcCorners(eye, forward, right, up);

    gl.glBegin(GL.GL_LINE_LOOP);
    for (int i = 0; i < 4; i++)
      gl.glVertex3f(v[i].x, v[i].y, v[i].z);
    gl.glEnd();

    gl.glBegin(GL.GL_LINE_LOOP);
    for (int i = 4; i < 8; i++)
      gl.glVertex3f(v[i].x, v[i].y, v[i].z);
    gl.glEnd();

    gl.glBegin(GL.GL_LINES);
    for (int i = 0; i < 4; i++) {
      gl.glVertex3f(v[i].x, v[i].y, v[i].z);
      gl.glVertex3f(v[i + 4].x, v[i + 4].y, v[i + 4].z);
    }
    gl.glEnd();
  }
}
