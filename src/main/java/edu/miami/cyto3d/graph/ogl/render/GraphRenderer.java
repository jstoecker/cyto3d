package edu.miami.cyto3d.graph.ogl.render;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import edu.miami.cyto3d.graph.view.EdgeView;
import edu.miami.cyto3d.graph.view.GraphView;
import edu.miami.cyto3d.graph.view.GraphViewListener;
import edu.miami.cyto3d.graph.view.VertexView;
import edu.miami.jgloo.GLDisposable;
import edu.miami.jgloo.Viewport;
import edu.miami.jgloo.content.ContentManager;
import edu.miami.jgloo.view.Camera;

/**
 * OpenGL renderer for a GraphView instance.
 * 
 * @author justin
 */
public class GraphRenderer implements GraphViewListener, GLDisposable {

  protected ContentManager content;
  protected VertexRenderer vertexRenderer;
  protected EdgeRenderer   edgeRenderer;

  boolean                  initialized = false;
  boolean                  disposed    = false;

  public GraphRenderer(ContentManager content) {
    this.content = content;
    vertexRenderer = new VertexRenderer(this);
    edgeRenderer = new EdgeRenderer(this);
  }

  public void setGraphView(GraphView graphView) {
    vertexRenderer.setGraphView(graphView);
    edgeRenderer.setGraphView(graphView);
  }

  public VertexRenderer getVertexRenderer() {
    return vertexRenderer;
  }

  public EdgeRenderer getEdgeRenderer() {
    return edgeRenderer;
  }

  public void dispose(GL gl) {
    vertexRenderer.dispose(gl);
    edgeRenderer.dispose(gl);
    disposed = true;
  }

  public void render(GL2 gl, Camera camera) {
    vertexRenderer.render(gl);
    edgeRenderer.render(gl);
    gl.glUseProgram(0);
  }

  protected void renderScene(GL2 gl) {
    vertexRenderer.render(gl);
    edgeRenderer.render(gl);
    gl.glUseProgram(0);
  }

  public boolean isDisposed() {
    return disposed;
  }

  public boolean isInitialized() {
    return initialized;
  }

  public void initialize(GLAutoDrawable drawable, Viewport viewport) {
    GL2 gl = drawable.getGL().getGL2();

    edgeRenderer.init(gl);

    initialized = true;
  }

  @Override
  public void viewCreated(GraphView graphView, VertexView vertexView) {
    vertexRenderer.add(vertexView);
  }

  @Override
  public void viewCreated(GraphView graphView, EdgeView edgeView) {
    edgeRenderer.add(edgeView);
  }

  @Override
  public void viewRemoved(GraphView graphView, VertexView vertexView) {
    vertexRenderer.remove(vertexView);
  }

  @Override
  public void viewRemoved(GraphView graphView, EdgeView edgeView) {
    edgeRenderer.remove(edgeView);
  }

  @Override
  public void viewCleared(GraphView graphView) {
    vertexRenderer.viewCleared(graphView);
    edgeRenderer.viewCleared(graphView);
  }
}
