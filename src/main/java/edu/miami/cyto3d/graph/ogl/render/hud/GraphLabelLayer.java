package edu.miami.cyto3d.graph.ogl.render.hud;

import java.awt.Font;
import java.util.PriorityQueue;
import java.util.Set;

import javax.media.opengl.GL2;

import com.jogamp.opengl.util.awt.TextRenderer;

import edu.miami.cyto3d.graph.animation.EffectAnimator;
import edu.miami.cyto3d.graph.model.Graph;
import edu.miami.cyto3d.graph.model.Vertex;
import edu.miami.cyto3d.graph.view.GraphView;
import edu.miami.cyto3d.graph.view.VertexView;
import edu.miami.jgloo.view.Camera;
import edu.miami.jgloo.view.Frustum;
import edu.miami.math.vector.Mat4;
import edu.miami.math.vector.RVec3;
import edu.miami.math.vector.Vec3;
import edu.miami.math.vector.Vec4;

/**
 * HUD layer that renders vertex properties as text positioned at the 2D projections of vertices
 * from a graph view.
 */
public class GraphLabelLayer implements HUDLayer {

  private class Label implements Comparable<Label> {
    final String text;
    final int    x, y;
    final float  depth;
    final float  alpha;

    public Label(String text, int x, int y, float depth, float alpha) {
      this.text = text;
      this.x = x;
      this.y = y;
      this.depth = depth;
      this.alpha = alpha;
    }

    @Override
    public int compareTo(Label that) {
      // sort by depth such that labels with larger z (further) are drawn first
      return (int) ((this.depth - that.depth) * 100);
    }

    public void render() {
      textRenderer.setColor(0, 0, 0, alpha);
      textRenderer.draw(text, x, y);
      textRenderer.setColor(1, 1, 1, alpha);
      textRenderer.draw(text, x + 1, y + 1);
    }
  }

  private Graph                graph;
  private GraphView            graphView;
  private String               propertyName;
  private TextRenderer         textRenderer;
  private PriorityQueue<Label> labels         = new PriorityQueue<Label>();

  private Set<Vertex>          activeVerts;
  private boolean              fadeByDistance = true;

  public GraphLabelLayer(Graph graph, GraphView view) {
    this.graph = graph;
    this.graphView = view;
    textRenderer = new TextRenderer(new Font("Helvetica", Font.PLAIN, 18), true, true);

    activeVerts = graph.getVertices();
  }

  public void setUsedVertexProperty(String propertyName) {
    this.propertyName = propertyName;
  }

  public void setActiveVerts(Set<Vertex> activeVerts, EffectAnimator animator) {
    this.activeVerts = activeVerts;
  }

  public void setFadeByDistance(boolean fadeByDistance) {
    this.fadeByDistance = fadeByDistance;
  }

  @Override
  public void render(GL2 gl, Camera camera) {
    calculateLabels(camera);

    textRenderer.beginRendering(camera.getViewport().w, camera.getViewport().h);
    for (Label label : labels) {
      label.render();
    }
    textRenderer.endRendering();
  }

  private void calculateLabels(Camera camera) {
    labels.clear();

    // TODO: just use the camera project
    Frustum projection = camera.getFrustum();
    Mat4 projectionMatrix = camera.getProjMatrix();
    Mat4 viewProjectionMatrix = projectionMatrix.times(camera.getViewMatrix());

    // for eye space depth calculation of labels
    float twoFN = 2 * projection.getZFar() * projection.getZNear();
    float fMinusN = projection.getZFar() - projection.getZNear();
    float fPlusN = projection.getZFar() + projection.getZNear();

    float maxDepth = 75;
    float fadeDepth = 60;
    float fadeRange = maxDepth - fadeDepth;

    if (activeVerts != null) {
      for (Vertex v : activeVerts) {
        if (!graph.contains(v)) continue;

        VertexView view = graphView.get(v);
        if (view == null || !view.isVisible()) continue;

        String property;
        if (propertyName == null) {
          property = v.toString();
        } else {
          property = v.getAttributes().get(propertyName, null);
          if (property == null) continue;
        }

        // world space coordinates
        RVec3 p3D = graphView.get(v).getPosition();

        // clip space coordinates
        Vec4 coords = viewProjectionMatrix.transform(new Vec4(p3D, 1));

        // normalized device coordinates
        coords.div(coords.w);

        // window coordinates (depth in eye space w.r.t. camera)
        Vec3 p2D = camera.getViewport().transform(coords, projection.getZNear(),
            projection.getZFar());
        int x = (int) p2D.x;
        int y = (int) p2D.y;
        float depth = twoFN / (fMinusN * p2D.z - fPlusN);

        if (x < 0 || x > camera.getViewport().w || y < 0 || y > camera.getViewport().h) continue;

        if (-depth > 0) {
          if (fadeByDistance) {
            if (-depth < maxDepth) {
              float alpha = 1;
              if (-depth > fadeDepth) {
                alpha = 1 + (depth + fadeDepth) / fadeRange;
              }
              alpha *= alpha * alpha * alpha;
              labels.add(new Label(property.toString(), x, y, depth, alpha));
            }
          } else {
            labels.add(new Label(property.toString(), x, y, depth, 1));
          }
        }
      }
    }
  }
}
