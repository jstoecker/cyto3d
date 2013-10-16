package edu.miami.cyto3d.graph.ogl.render.hud;

import javax.media.opengl.GL2;

import edu.miami.jgloo.view.Camera;

public interface HUDLayer {

   /**
    * Renders the HUD layer.
    * @param gl - OpenGL context.
    * @param viewport - Currently active viewport.
    * @param camera - Camera used to render the 3D graph.
    */
   public void render(GL2 gl, Camera camera);
}
