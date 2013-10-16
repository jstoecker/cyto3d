package edu.miami.cyto3d.util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Abstract base class for a model object that supports property changes.
 * 
 * @author justin
 */
public abstract class AbstractModel {

   protected PropertyChangeSupport propertyChangeSupport;

   public AbstractModel() {
      propertyChangeSupport = new PropertyChangeSupport(this);
   }

   public void addPropertyChangeListener(PropertyChangeListener listener) {
      propertyChangeSupport.addPropertyChangeListener(listener);
   }
   
   public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
      propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
   }

   public void removePropertyChangeListener(PropertyChangeListener listener) {
      propertyChangeSupport.removePropertyChangeListener(listener);
   }
   
   public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
      propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
   }

   protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
      propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
   }
}
