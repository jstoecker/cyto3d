package edu.miami.cyto3d;

import java.awt.event.MouseEvent;
import java.util.Collections;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;

import com.jogamp.opengl.util.FPSAnimator;

import edu.miami.cyto3d.graph.animation.EffectAnimator;
import edu.miami.cyto3d.graph.animation.VertexVanish;
import edu.miami.cyto3d.graph.layout.force.ForceLayoutThread;
import edu.miami.cyto3d.graph.model.Graph;
import edu.miami.cyto3d.graph.model.Vertex;
import edu.miami.cyto3d.graph.ogl.render.GraphRenderer;
import edu.miami.cyto3d.graph.ogl.render.hud.GraphLabelLayer;
import edu.miami.cyto3d.graph.ogl.render.hud.MainViewHUD;
import edu.miami.cyto3d.graph.style.GraphStyle;
import edu.miami.cyto3d.graph.util.EdgePicker;
import edu.miami.cyto3d.graph.util.VertexPicker;
import edu.miami.cyto3d.graph.view.GraphView;
import edu.miami.jgloo.FrameBufferObject;
import edu.miami.jgloo.Texture2D;
import edu.miami.jgloo.Viewport;
import edu.miami.jgloo.content.ContentManager;
import edu.miami.jgloo.draw.Draw;
import edu.miami.jgloo.light.LightModel;
import edu.miami.jgloo.util.Bloom;
import edu.miami.jgloo.util.Graphics;
import edu.miami.jgloo.view.OrbitCamera;
import edu.miami.jgloo.view.controller.orbit.OrbitCamController;
import edu.miami.math.util.GMath;
import edu.miami.math.vector.Vec3;

/**
 * OpenGL visualization of a graph.
 * 
 * @author justin
 */
public class GraphViewer implements GLEventListener {

  Graph              graph;
  GraphView          graphView;
  MainViewHUD        hud              = new MainViewHUD();
  ContentManager     content;
  GLCanvas           canvas;
  FPSAnimator        animator;
  Viewport           viewport         = new Viewport(0, 0, 800, 600);
  GraphStyle         style;
  ForceLayoutThread  forceLayout;
  OrbitCamera        camera           = new OrbitCamera(20, GMath.PI_OVER_3, 0, Vec3.zero(), true);
  OrbitCamController cameraController = new OrbitCamController(15, true);
  EffectAnimator     effectAnimator   = new EffectAnimator();
  VertexPicker       vertPicker       = new VertexPicker();
  EdgePicker         edgePicker       = new EdgePicker(null, null);
  GraphRenderer      graphRenderer;
  Bloom              bloom            = new Bloom();
  FrameBufferObject  sceneFBO;
  FrameBufferObject  multisampledFBO;
  LightModel         lightModel       = new LightModel();
  GraphLabelLayer    labelLayer;

  public GraphViewer() {
    GLProfile glp = GLProfile.get(GLProfile.GL2);
    GLCapabilities glc = new GLCapabilities(glp);
    glc.setNumSamples(8);
    glc.setSampleBuffers(true);

    content = new ContentManager(null, "textures/", null, "shaders/", false);
    graphRenderer = new GraphRenderer(content);
    canvas = new GLCanvas(glc);
    animator = new FPSAnimator(canvas, 60);
    canvas.addGLEventListener(this);

    initCameraController();
    initVertexPicker();
//    initEdgePicker();

    animator.start();
  }

  private void initCameraController() {
    cameraController.setCamera(camera);
    canvas.addKeyListener(cameraController);
    canvas.addMouseListener(cameraController);
    canvas.addMouseMotionListener(cameraController);
    canvas.addMouseWheelListener(cameraController);
  }

  private void initVertexPicker() {
    vertPicker.setCamera(camera);
    canvas.addMouseListener(vertPicker);
    canvas.addMouseMotionListener(vertPicker);

    vertPicker.addListener(new VertexPicker.Listener() {
      public void vertexHovered(Vertex v) {
        if (labelLayer != null) {
          labelLayer.setActiveVerts(Collections.singleton(v), effectAnimator);
        }
      }

      public void vertexGrabbed(Vertex v) {
      }

      public void vertexClicked(Vertex v, MouseEvent e) {
        if (e.getClickCount() == 1) {
          System.out.println(v.getAttributes());
        } else if (e.getClickCount() == 2) effectAnimator.addAnimation(new VertexVanish(v, graph,
            graphView, forceLayout, 200));
      }
    });
  }

  private void initEdgePicker() {
    edgePicker.setCamera(camera);
    canvas.addMouseListener(edgePicker);
    canvas.addMouseMotionListener(edgePicker);
  }
  
  public EffectAnimator getEffectAnimator() {
    return effectAnimator;
  }

  public GraphView getGraphView() {
    return graphView;
  }

  public ForceLayoutThread getForceLayout() {
    return forceLayout;
  }

  public GLCanvas getCanvas() {
    return canvas;
  }
  
  public GraphLabelLayer getLabelLayer() {
    return labelLayer;
  }

  public void setGraph(Graph graph, GraphView view) {
    if (this.graphView != null) {
      this.graphView.removeListener(graphRenderer);
      this.graph.removeListener(forceLayout.getLayout());
      hud.removeLayer(labelLayer);
    }

    this.graph = graph;
    this.graphView = view;

    graphRenderer.setGraphView(view);
    graphView.addListener(graphRenderer);

    boolean useForceLayout = true;
    if (forceLayout != null) {
      forceLayout.stop();
      useForceLayout = forceLayout.isEnabled();
    }
    forceLayout = new ForceLayoutThread(graph, view);
    forceLayout.setEnabled(useForceLayout);
    forceLayout.setInterval(10);
    graph.addListener(forceLayout.getLayout());

    vertPicker.setForceLayoutThread(forceLayout);
    vertPicker.setGraph(graph, view);
    edgePicker.setGraphView(view);

    labelLayer = new GraphLabelLayer(graph, view);
    labelLayer.setUsedVertexProperty("name");
    labelLayer.setActiveVerts(null, effectAnimator);
    labelLayer.setFadeByDistance(false);
    hud.addLayer(labelLayer);
  }

  @Override
  public void display(GLAutoDrawable drawable) {
    GL2 gl = drawable.getGL().getGL2();
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

    content.update(gl);
    cameraController.update();
    camera.apply(gl);
    lightModel.apply(gl);
    effectAnimator.update();

    renderMSPP(gl);
//    renderScene(gl);

    hud.render(gl, camera);
  }

  private void renderScene(GL2 gl) {
    gl.glColor3f(0.2f, 0.2f, 0.2f);
    gl.glLineWidth(2.5f);
    Draw.grid(gl, 40, 4);
    graphRenderer.render(gl, camera);
  }

  private void renderMSPP(GL2 gl) {
    multisampledFBO.bind(gl);
    multisampledFBO.clear(gl);
    multisampledFBO.setViewport(gl);

    renderScene(gl);
    gl.glUseProgram(0);

    int w = sceneFBO.getColorTexture(0).getWidth();
    int h = sceneFBO.getColorTexture(0).getHeight();
    gl.glBindFramebuffer(GL2.GL_READ_FRAMEBUFFER, multisampledFBO.getID());
    gl.glBindFramebuffer(GL2.GL_DRAW_FRAMEBUFFER, sceneFBO.getID());
    gl.glBlitFramebuffer(0, 0, w, h, 0, 0, w, h, GL.GL_COLOR_BUFFER_BIT, GL.GL_NEAREST);
    multisampledFBO.unbind(gl);

    // post processing
    Texture2D output = bloom.process(gl, sceneFBO.getColorTexture(0));
    gl.glColor4f(1, 1, 1, 1);

    // render result to window
    gl.glEnable(GL.GL_BLEND);
    viewport.apply(gl);
    output.bind(gl);
    Graphics.renderScreenQuad(gl);
    Texture2D.unbind(gl);
    gl.glDisable(GL.GL_BLEND);
  }
  
  public OrbitCamController getCameraController() {
    return cameraController;
  }

  @Override
  public void dispose(GLAutoDrawable drawable) {
    GL gl = drawable.getGL();
    if (sceneFBO != null) sceneFBO.dispose(gl);
    if (multisampledFBO != null) multisampledFBO.dispose(gl);
    if (bloom != null) bloom.dispose(gl);
  }

  @Override
  public void init(GLAutoDrawable drawable) {
    GL2 gl = drawable.getGL().getGL2();
    content.initialize(gl);
    graphRenderer.initialize(drawable, viewport);

    multisampledFBO = FrameBufferObject.create(gl, viewport.w, viewport.h, GL.GL_RGBA, 8);
    sceneFBO = FrameBufferObject.createNoDepth(gl, viewport.w, viewport.h, GL.GL_RGBA);

    bloom.init(gl, viewport, content);
    bloom.setThreshold(0.85f);
    bloom.setIntensity(6);
    bloom.setBlurriness(3.5f);

    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glEnable(GL.GL_TEXTURE_2D);
    gl.glEnable(GL.GL_BLEND);
  }

  @Override
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    viewport = new Viewport(x, y, width, height);
    camera.setViewport(viewport);

    GL2 gl = drawable.getGL().getGL2();
    sceneFBO.resize(gl, width, height);
    multisampledFBO.resize(gl, width, height);
    bloom.resize(gl, viewport);
  }
}
