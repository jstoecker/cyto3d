package edu.miami.cyto3d.graph.ogl.render;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import com.jogamp.common.nio.Buffers;

import edu.miami.cyto3d.graph.view.VertexView;
import edu.miami.cyto3d.graph.view.shapes.Polyhedron;
import edu.miami.jgloo.IndexBuffer;
import edu.miami.jgloo.VertexBuffer;
import edu.miami.jgloo.VertexBuffer.BufferUsage;
import edu.miami.jgloo.content.CMShader;
import edu.miami.jgloo.vertex.VertexFmt;
import edu.miami.jgloo.vertex.VertexFmtPN;
import edu.miami.math.vector.RVec3;
import edu.miami.math.vector.RVec4;

/**
 * Renders a group of vertices that share the same geometric shape and shader.
 * 
 * @author justin
 */
public class VertexBatch {

   private final ArrayList<VertexView> views = new ArrayList<VertexView>();
   private final Polyhedron                   shape;
   private final CMShader                     shader;
   private VertexFmt                          vertexFormat;
   private VertexBuffer                       vbo;
   private IndexBuffer                        ibo;

   public VertexBatch(Polyhedron shape, CMShader shader) {
      this.shape = shape;
      this.shader = shader;
   }

   public void init(GL gl) {
      vertexFormat = new VertexFmtPN(3, GL.GL_FLOAT, GL.GL_FLOAT);

      // buffer vertex data (position, normal) for reference shape that each instance uses
      FloatBuffer vbuf = Buffers.newDirectFloatBuffer(shape.getVertices().length * 6);
      for (int i = 0; i < shape.getVertices().length; i++) {
         vbuf.put(shape.getVertices()[i]);
         vbuf.put(shape.getNormals()[i]);
      }
      vbuf.rewind();
      vbo = new VertexBuffer(gl, BufferUsage.STATIC);
      vbo.bind();
      vbo.setData(vbuf, vbuf.capacity() * Float.SIZE / Byte.SIZE);
      vbo.unbind();

      // buffer index data for reference shape
      ShortBuffer ibuf = Buffers.newDirectShortBuffer(shape.getTriangles().length * 3);
      for (int i = 0; i < shape.getTriangles().length; i++) {
         for (int j = 0; j < 3; j++)
            ibuf.put((short) shape.getTriangles()[i][j]);
      }
      
      ibuf.rewind();
      ibo = new IndexBuffer(gl, BufferUsage.STATIC);
      ibo.bind();
      ibo.setData(ibuf, ibuf.capacity() * Short.SIZE / Byte.SIZE);
      ibo.unbind();
   }

   public void add(VertexView view) {
      views.add(view);
   }

   public void remove(VertexView view) {
      views.remove(view);
   }

   public void clear() {
      views.clear();
   }

   public void render(GL gl) {
      if (views.size() == 0) return;

      if (vbo == null || ibo == null) init(gl);

      GL2 gl2 = gl.getGL2();

      gl.glEnable(GL.GL_CULL_FACE);
      shader.get().enable(gl2);
      vbo.bind();
      ibo.bind();
      vertexFormat.setState(gl2);

      for (VertexView view : views) {
         if (view.isVisible()) {
            gl2.glPushMatrix();
            RVec4 color = view.getVisibleColor();
            RVec3 position = view.getPosition();
            gl2.glColor4f(color.x(), color.y(), color.z(), color.w());
            gl2.glTranslated(position.x(), position.y(), position.z());
            gl2.glScaled(view.getSize(), view.getSize(), view.getSize());
            gl2.glDrawElements(GL.GL_TRIANGLES, shape.getTriangles().length * 3,
                  GL.GL_UNSIGNED_SHORT, 0);
            gl2.glPopMatrix();
         }
      }

      gl.glDisable(GL.GL_CULL_FACE);
      vertexFormat.unsetState(gl2);
      ibo.unbind();
      vbo.unbind();
      shader.get().disable(gl2);
   }

   public void dispose(GL gl) {
      vbo.dispose(gl);
      ibo.dispose(gl);
   }
}