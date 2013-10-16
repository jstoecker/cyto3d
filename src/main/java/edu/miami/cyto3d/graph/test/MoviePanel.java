package edu.miami.cyto3d.graph.test;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import edu.miami.cyto3d.C3DApp;
import edu.miami.cyto3d.graph.animation.EdgeFade;
import edu.miami.cyto3d.graph.animation.VertexVanish;
import edu.miami.cyto3d.graph.layout.force.ForceLayout;
import edu.miami.cyto3d.graph.model.BasicGraph;
import edu.miami.cyto3d.graph.model.Edge;
import edu.miami.cyto3d.graph.model.Graph;
import edu.miami.cyto3d.graph.model.Vertex;
import edu.miami.cyto3d.graph.morph.GraphFrame;
import edu.miami.cyto3d.graph.morph.GraphInterframe;
import edu.miami.cyto3d.graph.ogl.render.hud.GraphLabelLayer;
import edu.miami.cyto3d.graph.style.SimpleStyle;
import edu.miami.cyto3d.graph.view.BasicGraphView;
import edu.miami.cyto3d.graph.view.EdgeView;
import edu.miami.cyto3d.graph.view.GraphView;
import edu.miami.cyto3d.graph.view.VertexView;
import edu.miami.jgloo.view.OrbitCamera;
import edu.miami.math.util.GMath;
import edu.miami.math.vector.RVec4;
import edu.miami.math.vector.Vec4;

public class MoviePanel extends JPanel {

  final C3DApp plugin;
  Graph              originalGraph;
  Graph              t1graph;
  GraphView          t1view;
  Graph              t2graph;
  GraphView          t2view;
  GraphInterframe    graphInterframe;
  float              timerProgress = 0;
  Timer              timer;
  OrbitCamera        cam;
  CameraRotateThread camRotThread  = new CameraRotateThread();
  
  Timer play;

  class CameraRotateThread extends Thread {
    boolean running = true;
    float   spd     = 0.1f;

    @Override
    public void run() {
      while (running) {
        cam.setAzimuth(cam.getAzimuth() + (float) Math.toRadians(spd));
        try {
          Thread.sleep(16);
        } catch (InterruptedException e) {
        }
      }
    }
  }

  public MoviePanel(final C3DApp plugin) {
    this.plugin = plugin;

    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
    gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
    gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
    gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
    setLayout(gridBagLayout);

    JButton btnHideLinks = new JButton("Initialize");
    btnHideLinks.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        init();
      }
    });
    GridBagConstraints gbc_btnHideLinks = new GridBagConstraints();
    gbc_btnHideLinks.insets = new Insets(0, 0, 5, 5);
    gbc_btnHideLinks.gridx = 0;
    gbc_btnHideLinks.gridy = 0;
    add(btnHideLinks, gbc_btnHideLinks);

    JButton btnShowLinks = new JButton("Step 1");
    btnShowLinks.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ev) {
        step1();
      }
    });

    JLabel lblSavesTheCurrent = new JLabel("Saves the current graph as the reference graph");
    GridBagConstraints gbc_lblSavesTheCurrent = new GridBagConstraints();
    gbc_lblSavesTheCurrent.fill = GridBagConstraints.HORIZONTAL;
    gbc_lblSavesTheCurrent.insets = new Insets(0, 0, 5, 0);
    gbc_lblSavesTheCurrent.gridx = 1;
    gbc_lblSavesTheCurrent.gridy = 0;
    add(lblSavesTheCurrent, gbc_lblSavesTheCurrent);
    GridBagConstraints gbc_btnShowLinks = new GridBagConstraints();
    gbc_btnShowLinks.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnShowLinks.insets = new Insets(0, 0, 5, 5);
    gbc_btnShowLinks.gridx = 0;
    gbc_btnShowLinks.gridy = 1;
    add(btnShowLinks, gbc_btnShowLinks);

    JLabel lblRemovesAllEdges = new JLabel("Removes all edges to provide first frame");
    GridBagConstraints gbc_lblRemovesAllEdges = new GridBagConstraints();
    gbc_lblRemovesAllEdges.anchor = GridBagConstraints.WEST;
    gbc_lblRemovesAllEdges.insets = new Insets(0, 0, 5, 0);
    gbc_lblRemovesAllEdges.gridx = 1;
    gbc_lblRemovesAllEdges.gridy = 1;
    add(lblRemovesAllEdges, gbc_lblRemovesAllEdges);

    JButton btnShowLinks_1 = new JButton("Step 2");
    btnShowLinks_1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        step2();
      }
    });
    GridBagConstraints gbc_btnShowLinks_1 = new GridBagConstraints();
    gbc_btnShowLinks_1.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnShowLinks_1.insets = new Insets(0, 0, 5, 5);
    gbc_btnShowLinks_1.gridx = 0;
    gbc_btnShowLinks_1.gridy = 2;
    add(btnShowLinks_1, gbc_btnShowLinks_1);

    JLabel lblShowsNamesOf = new JLabel("Shows names of two proteins");
    GridBagConstraints gbc_lblShowsNamesOf = new GridBagConstraints();
    gbc_lblShowsNamesOf.anchor = GridBagConstraints.WEST;
    gbc_lblShowsNamesOf.insets = new Insets(0, 0, 5, 0);
    gbc_lblShowsNamesOf.gridx = 1;
    gbc_lblShowsNamesOf.gridy = 2;
    add(lblShowsNamesOf, gbc_lblShowsNamesOf);

    JButton btnStartMorphFwd = new JButton("Step 3");
    btnStartMorphFwd.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        step3();
      }
    });
    GridBagConstraints gbc_btnStartMorphFwd = new GridBagConstraints();
    gbc_btnStartMorphFwd.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnStartMorphFwd.insets = new Insets(0, 0, 5, 5);
    gbc_btnStartMorphFwd.gridx = 0;
    gbc_btnStartMorphFwd.gridy = 3;
    add(btnStartMorphFwd, gbc_btnStartMorphFwd);

    JLabel lblHidesNamesAdds = new JLabel("Hides names; adds edges from reference graph");
    GridBagConstraints gbc_lblHidesNamesAdds = new GridBagConstraints();
    gbc_lblHidesNamesAdds.anchor = GridBagConstraints.WEST;
    gbc_lblHidesNamesAdds.insets = new Insets(0, 0, 5, 0);
    gbc_lblHidesNamesAdds.gridx = 1;
    gbc_lblHidesNamesAdds.gridy = 3;
    add(lblHidesNamesAdds, gbc_lblHidesNamesAdds);

    JButton btnStartMorphBkwd = new JButton("Step 4");
    btnStartMorphBkwd.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        step4();
      }
    });
    GridBagConstraints gbc_btnStartMorphBkwd = new GridBagConstraints();
    gbc_btnStartMorphBkwd.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnStartMorphBkwd.insets = new Insets(0, 0, 5, 5);
    gbc_btnStartMorphBkwd.gridx = 0;
    gbc_btnStartMorphBkwd.gridy = 4;
    add(btnStartMorphBkwd, gbc_btnStartMorphBkwd);

    JLabel lblMorphsIntoSecond = new JLabel("Morphing");
    GridBagConstraints gbc_lblMorphsIntoSecond = new GridBagConstraints();
    gbc_lblMorphsIntoSecond.insets = new Insets(0, 0, 5, 0);
    gbc_lblMorphsIntoSecond.anchor = GridBagConstraints.WEST;
    gbc_lblMorphsIntoSecond.gridx = 1;
    gbc_lblMorphsIntoSecond.gridy = 4;
    add(lblMorphsIntoSecond, gbc_lblMorphsIntoSecond);

    JButton btnStep = new JButton("Step 5");
    btnStep.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        step5();
      }
    });
    GridBagConstraints gbc_btnStep = new GridBagConstraints();
    gbc_btnStep.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnStep.insets = new Insets(0, 0, 5, 5);
    gbc_btnStep.gridx = 0;
    gbc_btnStep.gridy = 5;
    add(btnStep, gbc_btnStep);

    JLabel lblMorphsBackTo = new JLabel("Protein Removal");
    GridBagConstraints gbc_lblMorphsBackTo = new GridBagConstraints();
    gbc_lblMorphsBackTo.insets = new Insets(0, 0, 5, 0);
    gbc_lblMorphsBackTo.anchor = GridBagConstraints.WEST;
    gbc_lblMorphsBackTo.gridx = 1;
    gbc_lblMorphsBackTo.gridy = 5;
    add(lblMorphsBackTo, gbc_lblMorphsBackTo);
    
    JButton btnPlay = new JButton("Play");
    btnPlay.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        play();
      }
    });
    GridBagConstraints gbc_btnPlay = new GridBagConstraints();
    gbc_btnPlay.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnPlay.insets = new Insets(0, 0, 0, 5);
    gbc_btnPlay.gridx = 0;
    gbc_btnPlay.gridy = 6;
    add(btnPlay, gbc_btnPlay);
  }

  /** save the graph for reference, generate random edges for frame 2 */
  private void init() {
    originalGraph = new BasicGraph(plugin.getGraph());

    cam = plugin.getViewer().getCameraController().getCamera();
  }

  /** show the network w/o any edges */
  private void step1() {
    // create t1 graph
    t1graph = new BasicGraph(originalGraph);
    t1view = new BasicGraphView(t1graph);
    t1graph.addListener(t1view);
    t1graph.getAttributes().set("min_edge_length", 1);
    t1graph.getAttributes().set("max_edge_length", 5);
    
    t2graph = new BasicGraph(originalGraph);
    t2view = new BasicGraphView(t2graph);
    t2graph.addListener(t2view);
    t2graph.getAttributes().set("min_edge_length", 1);
    t2graph.getAttributes().set("max_edge_length", 5);

    // remove all edges initially
    for (Edge e : originalGraph.getEdges())
      t1graph.remove(e);

    // set proteins to bright green at random positions and display it
    for (VertexView v : t1view.getVertexViews()) {
      v.setPosition(GMath.rndVec3f(-10, 10));
      v.setPrimaryColor(new Vec4(0.2f, 1.0f, 0.4f, 1.0f));
    }
    plugin.setNewGraph(t1graph, t1view, false);

    // start slow camera rotation
    if (camRotThread != null) {
      camRotThread.running = false;
    }
    camRotThread = new CameraRotateThread();
    camRotThread.start();
    camRotThread.spd = 0.05f;
  }

  /** show protein names for a selected set of names */
  private void step2() {
    Set<String> vertNames = new HashSet<String>();
    vertNames.add("cdc42");
    vertNames.add("n-wasp");

    Set<Vertex> activeVerts = new HashSet<Vertex>();
    for (Vertex v : t1graph.getVertices()) {
      String name = v.getAttributes().get("name", null);
      if (name != null && vertNames.contains(name.toLowerCase())) activeVerts.add(v);
    }

    GraphLabelLayer gll = plugin.getViewer().getLabelLayer();
    gll.setFadeByDistance(false);
    gll.setUsedVertexProperty("name");
    gll.setActiveVerts(activeVerts, plugin.getViewer().getEffectAnimator());
  }

  /** hide labels, fade in edges from reference graph */
  private void step3() {
    camRotThread.spd = 0;

    GraphLabelLayer gll = plugin.getViewer().getLabelLayer();
    gll.setActiveVerts(null, null);

    plugin.getViewer().getForceLayout().setEnabled(false);
    for (Edge e : originalGraph.getEdges()) {
      // create random lengths from 1 to 3 and fade them in
      e.getAttributes().set("length", (float) (1 + Math.random() * 2));
      t1graph.add(e);
      plugin.getViewer().getEffectAnimator().addAnimation(new EdgeFade(t1view.get(e), 500, true));
    }

    new SimpleStyle().apply(t1graph, t1view);

    timer = new Timer(350, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        plugin.getViewer().getForceLayout().setEnabled(true);
        plugin.getViewer().getForceLayout().setInterval(20);
        plugin.getViewer().getForceLayout().getLayout().setMaxSpeed(0.5f);
        // plugin.getViewer().getForceLayout().getLayout().setDamping(0.99f);
        plugin.getViewer().getForceLayout().getLayout().setSpringiness(0.2f);
      }
    });
    timer.setRepeats(false);
    timer.start();
  }
  
  float r;

  /** morph from t1 to t2 */
  private void step4() {
    r = cam.getRadius();

    timer = new Timer(10, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        timerProgress += 1 / 300f;
        if (timerProgress >= 1) {
          timer.setRepeats(false);
          timer.stop();
          plugin.setNewGraph(t2graph, t2view, false);
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e1) {
            e1.printStackTrace();
          }
          step5();
        } else {
          graphInterframe.lerp(timerProgress);
          plugin.setNewGraph(graphInterframe.getGraph(), graphInterframe.getGraphView(), false);
        }
      }
    });

    new Thread(new Runnable() {
      public void run() {
        System.out.println("compute morph");

        for (int i = 0; i < 100; i++) {
          plugin.getViewer().getForceLayout().getLayout().setDamping(0.9f - 0.9f * i / 100f);
          cam.setRadius(r - (r - 50) * i / 100f);
          try {
            Thread.sleep(25);
          } catch (InterruptedException e1) {
            e1.printStackTrace();
          }
        }
        
        for (EdgeView ev : t1view.getEdgeViews())
          ev.setPrimaryColor(ev.getVisibleColor());
        for (EdgeView ev : t2view.getEdgeViews())
          ev.setPrimaryColor(new Vec4(0,0,1,1));
        
        GraphFrame start = new GraphFrame(t1graph, t1view);

        // generate t2 graph view
        for (Vertex v : t1graph.getVertices()) {
          t2view.get(v).setPosition(t1view.get(v).getPosition());
        }

        Set<String> ignored = new HashSet<String>();
        ignored.add("cdc42");
        ignored.add("n-wasp");
        ignored.add("ubiquitin");
        // make most edges longer than in t1 (older neuron) except for cdc42, ubiquitin, wasp
        for (Edge e : t2graph.getEdges()) {
          String s1 = e.getSource().getAttributes().get("name", "x").toLowerCase();
          String s2 = e.getTarget().getAttributes().get("name", "x").toLowerCase();
          if (ignored.contains(s1) || ignored.contains(s2)) continue;

          float l = e.getAttributes().get("length", 5f);
          l = GMath.clamp(l * 1.5f, 1, 5);
          e.getAttributes().set("length", l);
        }

        ForceLayout fl = new ForceLayout(t2graph, t2view);
        for (int i = 0; i < 12000; i++) {
          fl.iterateLayout();
        }
        
        new SimpleStyle().apply(t2graph, t2view);
        System.out.println("starting the morph");

        GraphFrame end = new GraphFrame(t2graph, t2view);
        graphInterframe = new GraphInterframe(start, end);

        plugin.getViewer().getForceLayout().getLayout().setDamping(0.9f);
        plugin.getViewer().getForceLayout().setEnabled(false);
        timerProgress = 0;
        timer.setDelay(10);
        timer.setRepeats(true);
        timer.start();
        // camRotThread.spd = 0.05f;
      }
    }).start();
  }
  
  Vertex toRemove;
  RVec4 originalColor = null;

  private void step5() {
    camRotThread.spd = 0;

    String tgtVert = "ubiquitin";
    for (Vertex v : t1graph.getVertices()) {
      String name = v.getAttributes().get("name", null);
      if (name != null && tgtVert.equalsIgnoreCase(name)) {
        toRemove = v;
         originalColor = t2view.get(toRemove).getPrimaryColor().clone();
      }
      ;
    }
    
    timerProgress = 0;
    timer = new Timer(10, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        timerProgress += 0.01f;
        t2view.get(toRemove).setPrimaryColor(Vec4.lerp(originalColor, new Vec4(1,0,0,1), timerProgress));
        if (timerProgress >= 1) {
          System.out.println("explode");
          timer.setRepeats(false);
          timer.stop();
          plugin.getViewer().getForceLayout().setEnabled(true);
          plugin.getViewer().getForceLayout().getLayout().setUseVelocity(true);
          plugin.getViewer().getForceLayout().getLayout().setSpringiness(0.5f);
          plugin
              .getViewer()
              .getEffectAnimator()
              .addAnimation(
                  new VertexVanish(toRemove, t2graph, t2view, plugin.getViewer().getForceLayout(), 200));
        }
      }
    });
    timer.start();


    // plugin.getViewer().getForceLayout().getLayout().setDamping(0.95f);
  }
  
  int step = 1;
  int elapsed = 0;
  int[] durations = {1000, 3000, 5000, 6000, 8000};
  
  private void play() {
    elapsed = 0;
    step = 1;
    play = new Timer(20, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (elapsed >= durations[step-1]) {
          switch (step) {
          case 1:
            step1(); 
            break;
          case 2:
            step2();
            break;
          case 3:
            step3();
            break;
          case 4:
            step4();
            break;
          }
          step++;
          elapsed = 0;
          if (step > 4)
            play.stop();
        }
        elapsed += 20;
      }
    });
    play.setRepeats(true);
    play.start();
  }
}
