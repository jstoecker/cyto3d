package edu.miami.jgloo.view.controller.orbit;

import edu.miami.jgloo.view.OrbitCamera;
import edu.miami.jgloo.view.controller.CameraSource;

public interface OrbitCameraSource extends CameraSource {

  @Override
  OrbitCamera getCamera();
}
