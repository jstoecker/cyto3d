package edu.miami.cyto3d.graph.ogl.util;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;

import com.jogamp.opengl.util.FPSAnimator;

import edu.miami.jgloo.Viewport;

/**
 * Surface for OpenGL-related rendering.
 * 
 * @author justin
 */
public abstract class GLSurface implements GLEventListener {

   protected final GLCanvas    canvas;
   protected final FPSAnimator animator;
   protected Viewport          viewport;

   public GLSurface(GLCapabilities glc, int fps) {
      canvas = new GLCanvas(glc);
      canvas.addGLEventListener(this);
      animator = new FPSAnimator(canvas, fps);
      animator.start();
   }

   public GLCanvas getCanvas() {
      return canvas;
   }

   public Viewport getViewport() {
      return viewport;
   }

   @Override
   public abstract void display(GLAutoDrawable drawable);

   @Override
   public abstract void dispose(GLAutoDrawable drawable);

   @Override
   public abstract void init(GLAutoDrawable drawable);

   @Override
   public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
      viewport = new Viewport(x, y, w, h);
   }
}
