package edu.miami.jgloo.draw;

import javax.media.opengl.GL2;

import edu.miami.math.util.GMath;
import edu.miami.math.vector.Mat4;
import edu.miami.math.vector.Vec3;

/**
 * Creates a triangle strip mesh in the shape of a circle.
 * 
 * @author justin
 */
public class GLCircle {

  private float[][] verts;
  private float upX, upY, upZ;

  /**
   * Creates a new circle.
   * 
   * @param radius - radius of the circle.
   * @param segments - number of segments in the circle (3 = triangle, 4 = diamond, etc.).
   * @param center - center point of the circle.
   * @param up - unit normal vector of the circle's plane.
   */
  public GLCircle(float radius, float thickness, int segments, Vec3 center, Vec3 up) {
    this(radius, thickness, segments, center, up, randomRight(up));
  }
  
  public GLCircle(float radius, float thickness, int segments, Vec3 center, Vec3 up, Vec3 right) {
    upX = up.x;
    upY = up.y;
    upZ = up.z;
    
    // matrix that rotates around the up axis
    Mat4 rotation = Mat4.createRotation(Math.PI * 2.0 / segments, up);

    // for each segment iteration: add the inner and outer vertices, then rotate
    float deltaR = thickness / 2;
    verts = new float[segments * 2][3];
    int j = 0;
    for (int i = 0; i < segments; i++) {
      verts[j++] = right.times(radius - deltaR).plus(center).values();
      verts[j++] = right.times(radius + deltaR).plus(center).values();
      right = rotation.transform(right);
    }
  }
  
  private static Vec3 randomRight(Vec3 up) {
    // get a random point on the circle's plane
    Vec3 rndVec;
    do {
      rndVec = GMath.rndVec3f(-1, 1).normalize();
    } while (Math.abs(up.dot(rndVec)) == 1);
    return rndVec.cross(up).normalize();
  }

  public void render(GL2 gl) {
    gl.glBegin(GL2.GL_QUAD_STRIP);
    gl.glNormal3f(upX, upY, upZ);
    for (int i = 0; i < verts.length; i++)
      gl.glVertex3fv(verts[i], 0);
    gl.glVertex3fv(verts[0], 0);
    gl.glVertex3fv(verts[1], 0);
    gl.glEnd();
  }
}
