package edu.miami.cyto3d.graph.ogl.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import edu.miami.cyto3d.graph.view.EdgeView;
import edu.miami.cyto3d.graph.view.GraphView;
import edu.miami.jgloo.content.CMShader;
import edu.miami.jgloo.content.CMTexture2D;

public class EdgeRenderer {

  GraphRenderer                      renderer;
  CMShader                           edgeShader;
  List<EdgeBatch>                    batches  = new ArrayList<EdgeBatch>();
  HashMap<EdgeView.Style, EdgeBatch> batchMap = new HashMap<EdgeView.Style, EdgeBatch>();
  int                                edgeBillboardDL;

  public EdgeRenderer(GraphRenderer renderer) {
    this.renderer = renderer;
  }

  public void setGraphView(GraphView graphView) {
    viewCleared(graphView);
    for (EdgeView view : graphView.getEdgeViews())
      add(view);
  }

  public void init(GL2 gl) {
    edgeShader = renderer.content.retrieveShader("edge", false);

    edgeBillboardDL = gl.glGenLists(1);
    gl.glNewList(edgeBillboardDL, GL2.GL_COMPILE);
    {
      gl.glTexCoord2f(0, 0);
      gl.glVertex3f(0, 0, 0);
      gl.glTexCoord2f(1, 0);
      gl.glVertex3f(0, 0, 0);
      gl.glTexCoord2f(1, 1);
      gl.glVertex3f(0, 0, 0);
      gl.glTexCoord2f(0, 1);
      gl.glVertex3f(0, 0, 0);
    }
    gl.glEndList();
  }

  public void render(GL2 gl) {
    if (batches.size() == 0) return;

    gl.glEnable(GL.GL_BLEND);
    gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
    gl.glEnable(GL2.GL_ALPHA_TEST);
    gl.glAlphaFunc(GL.GL_GREATER, 0);
    edgeShader.get().enable(gl);
    int ulocTexAspect = edgeShader.get().getUniform(gl, "textureAspect");

    for (EdgeBatch eb : batches) {
      float texAspect = (float) eb.getTexture().get().getWidth()
          / eb.getTexture().get().getHeight();
      gl.glUniform1f(ulocTexAspect, texAspect);
      eb.render(gl, edgeBillboardDL);
    }

    gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
    gl.glDisable(GL2.GL_ALPHA_TEST);
    gl.glDisable(GL.GL_BLEND);
  }

  public void dispose(GL gl) {
    gl.getGL2().glDeleteLists(edgeBillboardDL, 1);
  }

  private EdgeBatch createBatch(EdgeView.Style style) {

    String textureName;
    switch (style) {
    case SOLID:
      textureName = "edges/solid.png";
      break;
    case DOTTED:
      textureName = "edges/dot.png";
      break;
    default:
      textureName = "edges/solid.png";
    }

    CMTexture2D texture = renderer.content.retrieveTexture(textureName, false);
    EdgeBatch batch = new EdgeBatch(texture);
    batches.add(batch);
    batchMap.put(style, batch);
    return batch;
  }

  protected void add(EdgeView view) {
    EdgeView.Style style = view.getStyle();
    EdgeBatch batch = batchMap.get(style);
    if (batch == null) batch = createBatch(style);
    batch.add(view);
  }

  protected void remove(EdgeView view) {
    EdgeView.Style style = view.getStyle();
    EdgeBatch batch = batchMap.get(style);
    if (batch != null) batch.remove(view);
  }

  public void viewCleared(GraphView graphView) {
    for (EdgeBatch batch : batches)
      batch.clear();
  }
}
