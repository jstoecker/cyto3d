package edu.miami.cyto3d.graph.ogl.render;

import java.util.ArrayList;
import java.util.HashMap;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import edu.miami.cyto3d.graph.view.GraphView;
import edu.miami.cyto3d.graph.view.VertexView;
import edu.miami.cyto3d.graph.view.shapes.Box;
import edu.miami.cyto3d.graph.view.shapes.Octahedron;
import edu.miami.cyto3d.graph.view.shapes.Polyhedron;
import edu.miami.cyto3d.graph.view.shapes.Sphere;
import edu.miami.cyto3d.graph.view.shapes.Tetrahedron;
import edu.miami.jgloo.content.CMShader;

public class VertexRenderer {

  GraphRenderer                          renderer;
  ArrayList<VertexBatch>                 batches  = new ArrayList<VertexBatch>();
  HashMap<VertexView.Shape, VertexBatch> batchMap = new HashMap<VertexView.Shape, VertexBatch>();

  public VertexRenderer(GraphRenderer renderer) {
    this.renderer = renderer;
  }

  public void setGraphView(GraphView graphView) {
    viewCleared(graphView);
    for (VertexView view : graphView.getVertexViews())
      add(view);
  }

  public void render(GL2 gl) {
    if (batches.size() > 0) {
      for (VertexBatch batch : batches)
        batch.render(gl);
    }
  }

  public void dispose(GL gl) {
    for (VertexBatch batch : batches) {
      batch.clear();
      batch.dispose(gl);
    }
  }

  public void add(VertexView view) {
    VertexView.Shape shape = view.getShape();
    VertexBatch batch = batchMap.get(shape);
    if (batch == null) batch = createBatch(shape);
    batch.add(view);
  }

  public void remove(VertexView view) {
    VertexView.Shape shape = view.getShape();
    VertexBatch batch = batchMap.get(shape);
    if (batch != null) batch.remove(view);
  }

  public void viewCleared(GraphView graphView) {
    for (VertexBatch batch : batches)
      batch.clear();
  }

  private VertexBatch createBatch(VertexView.Shape shape) {
    CMShader shader = null;
    Polyhedron polyhedron = null;

    switch (shape) {
    case SPHERE:
      shader = renderer.content.retrieveShader("convpoly_smooth", false);
      polyhedron = new Sphere(0.5f, 2);
      break;
    case BOX:
      shader = renderer.content.retrieveShader("convpoly_flat", false);
      polyhedron = new Box(1, 1, 1);
      break;
    case OCTAHEDRON:
      shader = renderer.content.retrieveShader("convpoly_flat", false);
      polyhedron = new Octahedron(1.0f);
      break;
    case TETRAHEDRON:
      shader = renderer.content.retrieveShader("convpoly_flat", false);
      polyhedron = new Tetrahedron(1.0f);
      break;
    }

    VertexBatch batch = new VertexBatch(polyhedron, shader);
    batchMap.put(shape, batch);
    batches.add(batch);

    return batch;
  }
}
