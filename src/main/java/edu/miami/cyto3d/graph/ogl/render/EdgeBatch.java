package edu.miami.cyto3d.graph.ogl.render;

import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import edu.miami.cyto3d.graph.view.EdgeView;
import edu.miami.jgloo.content.CMTexture2D;
import edu.miami.math.vector.RVec4;
import edu.miami.math.vector.Vec3;

/**
 * Draws network edges as axis-constrained billboards. Batches group edges by texture.
 * 
 * @author justin
 */
public class EdgeBatch {

  private final ArrayList<EdgeView> edges = new ArrayList<EdgeView>();
  private final CMTexture2D         managedTexture;

  public EdgeBatch(CMTexture2D texture) {
    this.managedTexture = texture;
  }

  public CMTexture2D getTexture() {
    return managedTexture;
  }

  public void add(EdgeView edge) {
    edges.add(edge);
  }

  public void remove(EdgeView edge) {
    edges.remove(edge);
  }

  public void clear() {
    edges.clear();
  }

  public void render(GL2 gl, int edgeBillboardDL) {
    managedTexture.get().bind(gl);

    gl.glBegin(GL2.GL_QUADS);
    for (int i = 0; i < edges.size(); i++) {
      EdgeView view = edges.get(i);
      
      if (view == null)
        continue;

      Vec3 v = view.calcVector();
      Vec3 center = view.getSourceView().getPosition().plus(v.over(2));
      float length = v.length();
      Vec3 rotAxis = v.over(length);
      RVec4 c = view.getVisibleColor();

      gl.glColor4f(c.x(), c.y(), c.z(), c.w());
      gl.glNormal3f(rotAxis.x, rotAxis.y, rotAxis.z);
      gl.glMultiTexCoord3f(GL.GL_TEXTURE1, center.x, center.y, center.z);
      gl.glMultiTexCoord2f(GL.GL_TEXTURE2, length, view.getWidth());

      // Rather than calling glTexCoord / glVertex 4x times (8 calls) with instance-specific
      // data, a static display list is used that has the proper texture coordinates and useless
      // vertex positions. The real positions and width/height are stored in texcoord slots 1/2
      // respectively, which reduces the number of calls required from 10 to 5.
      gl.glCallList(edgeBillboardDL);
    }
    gl.glEnd();
  }
}