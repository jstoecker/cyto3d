package edu.miami.jgloo.view.controller;

import edu.miami.jgloo.view.Camera;

/**
 * Refers to an object that stores or provides a camera. This is especially useful for camera
 * motions. Many motions belong to a camera controller and last until the controller is destroyed;
 * however, the controller may have its camera object changed. Rather than passing the camera object
 * directly to the motions, the motions always use the most recent camera by asking for it using
 * getCamera().
 * 
 * @author justin
 */
public interface CameraSource {
  Camera getCamera();
}
