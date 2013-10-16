package edu.miami.cyto3d.graph.layout.force;

import edu.miami.cyto3d.graph.model.Graph;
import edu.miami.cyto3d.graph.view.GraphView;

/**
 * Iterates a force layout repeatedly over time. The user of this class has three options for
 * setting the edge lengths in the force layout: <br>
 * 1) Register a class as a delegate that provides the edge lengths as a hash map. <br>
 * 2) Set the edge lengths manually calling setEdgeLengths(). <br>
 * 3) Set a single length that is used for all edges by calling setEdgeLength().<br>
 * <br>
 * If multiple values are set, this class will use the first non-null edge lengths in the order
 * above.
 * 
 * @author justin
 */
public class ForceLayoutThread {

   ForceLayout layout;
   long        interval           = 1;
   boolean     running            = false;
   boolean     enabled            = true;
   InnerThread currentInnerThread = null;

   private class InnerThread extends Thread {
      volatile boolean running  = true;
      volatile boolean finished = false;

      @Override
      public void run() {
         while (running) {
            try {
               Thread.sleep(interval);
               layout.iterateLayout();
            } catch (InterruptedException e) {
            }
         }
         finished = true;
      }
   }

   public ForceLayoutThread(Graph graph, GraphView graphView) {
      layout = new ForceLayout(graph, graphView);
      if (enabled) start();
   }

   public void setRunning(boolean running) {
      this.running = running;
   }

   public synchronized void setEnabled(boolean enabled) {
      boolean wasEnabled = this.enabled;
      this.enabled = enabled;

      if (wasEnabled && !enabled) {
         stop();
      } else if (!wasEnabled && enabled) {
         start();
      }
   }

   public void setInterval(long interval) {
      this.interval = interval;
   }

   public long getInterval() {
      return interval;
   }

   public boolean isEnabled() {
      return enabled;
   }

   public ForceLayout getLayout() {
      return layout;
   }

   public synchronized void start() {
      if (!running && enabled) {
         running = true;
         currentInnerThread = new InnerThread();
         currentInnerThread.start();
      }
   }

   public synchronized void stop() {
      running = false;
      if (currentInnerThread != null) {
         currentInnerThread.running = false;
         currentInnerThread.interrupt();
         while (!currentInnerThread.finished)
            ;
      }
   }
}
