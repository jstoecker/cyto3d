package edu.miami.cyto3d.graph.util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import edu.miami.cyto3d.util.AbstractModel;
import edu.miami.jgloo.view.Camera;
import edu.miami.math.geom.Ray;

/**
 * Mouse-based picker of scene objects.
 * 
 * @param <T>
 * @author justin
 */
public abstract class Picker<T> extends AbstractModel implements MouseListener, MouseMotionListener {

   public static final String P_HOVERED  = "hovered";
   public static final String P_GRABBED  = "grabbed";

   protected T                hovered;
   protected T                grabbed;
   protected Camera           camera;
   protected int              grabButton = MouseEvent.BUTTON3;

   public Picker(Camera camera) {
      this.camera = camera;
   }

   public T getHovered() {
      return hovered;
   }

   public T getGrabbed() {
      return grabbed;
   }

   public void setCamera(Camera camera) {
      this.camera = camera;
   }

   protected void setHovered(T hovered) {
      T oldValue = this.hovered;
      this.hovered = hovered;
      firePropertyChange(P_HOVERED, oldValue, hovered);
   }

   protected void setGrabbed(T grabbed) {
      T oldValue = this.grabbed;
      this.grabbed = grabbed;
      firePropertyChange(P_GRABBED, oldValue, grabbed);
   }

   @Override
   public void mousePressed(MouseEvent e) {
      if (hovered != null && e.getButton() == grabButton) setGrabbed(hovered);
   }

   @Override
   public void mouseReleased(MouseEvent e) {
      if (grabbed != null) setGrabbed(null);
      if (camera != null) setHovered(pickHovered(camera.unproject(e.getX(), e.getY(), true)));
   }

   @Override
   public void mouseDragged(MouseEvent e) {
      if (grabbed != null && e.getButton() == grabButton) grabbedMoved(camera.unproject(e.getX(),
            e.getY(), true));
   }

   @Override
   public void mouseMoved(MouseEvent e) {
      if (camera != null) {
         Ray ray = camera.unproject(e.getX(), e.getY(), true);
         T prevHover = hovered;
         hovered = pickHovered(ray);
         if (hovered != prevHover) {
            firePropertyChange(P_HOVERED, prevHover, hovered);
         }
      }
   }

   protected abstract void grabbedMoved(Ray ray);

   protected abstract T pickHovered(Ray ray);

   @Override
   public void mouseClicked(MouseEvent e) {
   }

   @Override
   public void mouseEntered(MouseEvent e) {
   }

   @Override
   public void mouseExited(MouseEvent e) {
   }
}
