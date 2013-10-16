package edu.miami.cyto3d.graph.util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import edu.miami.cyto3d.graph.layout.force.ForceLayoutThread;
import edu.miami.cyto3d.graph.model.Graph;
import edu.miami.cyto3d.graph.model.Vertex;
import edu.miami.cyto3d.graph.view.GraphView;
import edu.miami.cyto3d.graph.view.VertexView;
import edu.miami.jgloo.view.Camera;
import edu.miami.math.geom.Plane;
import edu.miami.math.geom.Ray;
import edu.miami.math.vector.RVec3;
import edu.miami.math.vector.Vec3;
import edu.miami.math.vector.Vec4;

/**
 * Enables 2D coordinate selection of a graph vertex.
 * 
 * @author justin
 */
public class VertexPicker implements MouseListener, MouseMotionListener {

  public interface Listener {
    void vertexClicked(Vertex v, MouseEvent e);

    void vertexHovered(Vertex v);

    void vertexGrabbed(Vertex v);

  }

  Vertex            hovered;
  Vertex            grabbed;
  Vec4              selectedColor = new Vec4(1, 1, 0, 1);
  Vec3              savedPosition;
  Plane             dragPlane;
  Graph             graph;
  GraphView         graphView;
  ForceLayoutThread forceLayoutThread;
  Camera            camera;
  List<Listener>    listeners     = new CopyOnWriteArrayList<Listener>();

  Vertex            src;

  public void addListener(Listener l) {
    listeners.add(l);
  }

  public void removeListener(Listener l) {
    listeners.remove(l);
  }

  public Vertex getHovered() {
    return hovered;
  }

  public Vertex getGrabbed() {
    return grabbed;
  }

  public void setForceLayoutThread(ForceLayoutThread forceLayoutThread) {
    this.forceLayoutThread = forceLayoutThread;
  }

  public void setGraph(Graph graph, GraphView view) {
    this.graph = graph;
    this.graphView = view;
  }

  public void setCamera(Camera camera) {
    this.camera = camera;
  }

  private void merge(Vertex src, Vertex tgt) {
    graph.createEdge(src, tgt, false);
    graphView.get(src).restoreColor();
    this.src = null;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    if (graph == null) return;
    if (hovered != null) {
      for (Listener l : listeners)
        l.vertexClicked(hovered, e);
    }
  }

  @Override
  public void mouseEntered(MouseEvent e) {
  }

  @Override
  public void mouseExited(MouseEvent e) {
  }

  @Override
  public void mousePressed(MouseEvent e) {
    if (graph == null) return;
    if (hovered != null && e.getButton() == MouseEvent.BUTTON3) {
      if (e.isShiftDown()) {
        if (src == null) {
          src = hovered;
          graphView.get(src).pushColor(new Vec4(0, 1, 0.5f, 1));
        } else
          merge(src, hovered);
      } else {
        grabbed = hovered;
        if (forceLayoutThread != null) forceLayoutThread.getLayout().getVertex(grabbed)
            .setClamped(true);
        savedPosition = graphView.get(grabbed).getPosition().clone();
        dragPlane = new Plane(savedPosition, camera.getForward().clone());

        for (Listener l : listeners)
          l.vertexGrabbed(grabbed);
      }
    }
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    if (graph == null) return;
    if (grabbed != null) {
      if (forceLayoutThread != null) forceLayoutThread.getLayout().getVertex(grabbed)
          .setClamped(false);
    }

    grabbed = null;
    pickHovered(e.getX(), e.getY());
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    if (graph == null) return;
    if (grabbed != null && e.getButton() == MouseEvent.BUTTON3) {
      Ray r = camera.unproject(e.getX(), e.getY(), true);
      Vec3 newPos = dragPlane.intersect(r);
      if (newPos != null) {
        graphView.get(grabbed).setPosition(newPos);
      }
    }
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    if (graph == null) return;
    if (graphView != null) pickHovered(e.getX(), e.getY());
  }

  private void pickHovered(int x, int y) {
    Ray r = camera.unproject(x, y, true);
    float minDSqr = Float.POSITIVE_INFINITY;
    Vertex closest = null;
    for (Vertex vertex : graph.getVertices()) {
      VertexView view = graphView.get(vertex);
      RVec3 p = view.getPosition();
      float radius = view.getSize() / 2;

      float d = view.getPosition().minus(camera.getEye()).lengthSquared();
      if (intersects(r, p, radius) && d < minDSqr) {
        closest = vertex;
        minDSqr = d;
      }
    }
    hover(closest);
  }

  private void hover(Vertex newHovered) {
    Vertex oldValue = hovered;

    if (graphView.get(hovered) == null)
      hovered = null;
    
    if (hovered != null) {
      graphView.get(hovered).restoreColor();
    }

    if (newHovered != null) {
      graphView.get(newHovered).pushColor(selectedColor);
    }

    hovered = newHovered;

    if (hovered != oldValue) {
      for (Listener l : listeners)
        l.vertexHovered(hovered);
    }
  }

  private boolean intersects(Ray ray, RVec3 center, float r) {
    Vec3 pMinusC = ray.getPosition().minus(center);
    float a = ray.getDirection().dot(ray.getDirection());
    float b = 2 * ray.getDirection().dot(pMinusC);
    float c = pMinusC.dot(pMinusC) - r * r;
    float disc = b * b - 4 * a * c;
    return disc >= 0;
  }

  public void clear() {
    hover(null);
    grabbed = null;
  }
}
