package edu.miami.cyto3d.graph.ogl.render.hud;

import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import edu.miami.jgloo.Viewport;
import edu.miami.jgloo.view.Camera;

/**
 * 2D heads up display drawn over the graph.
 * 
 * @author justin
 */
public class MainViewHUD {

   private ArrayList<HUDLayer> layers = new ArrayList<HUDLayer>();

   public void addLayer(HUDLayer layer) {
      layers.add(layer);
   }

   public void removeLayer(HUDLayer layer) {
      layers.remove(layer);
   }
   
   public void render(GL2 gl, Camera camera) {
      Viewport viewport = camera.getViewport();
      
      // set orthographic projection for 2D overlays
      GL2 gl2 = gl.getGL2();
      gl2.glMatrixMode(GL2.GL_PROJECTION);
      gl2.glLoadIdentity();
      gl2.glOrtho(0, viewport.w, 0, viewport.h, -1, 1);
      gl2.glMatrixMode(GL2.GL_MODELVIEW);
      gl2.glLoadIdentity();
      viewport.apply(gl2);

      gl.glDisable(GL.GL_DEPTH_TEST);
      for (HUDLayer layer : layers)
         layer.render(gl.getGL2(), camera);
      gl.glEnable(GL.GL_DEPTH_TEST);
   }
}
