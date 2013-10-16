package edu.miami.jgloo.view.controller.fp;

import edu.miami.jgloo.view.FPCamera;
import edu.miami.jgloo.view.controller.CameraSource;

public interface FPCameraSource extends CameraSource {

  @Override
  public FPCamera getCamera();
}
