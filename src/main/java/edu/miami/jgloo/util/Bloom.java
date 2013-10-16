/*
 *  Copyright 2011 RoboViz
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package edu.miami.jgloo.util;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2GL3;

import edu.miami.jgloo.FrameBufferObject;
import edu.miami.jgloo.GLDisposable;
import edu.miami.jgloo.ShaderProgram;
import edu.miami.jgloo.Texture2D;
import edu.miami.jgloo.Uniform;
import edu.miami.jgloo.Viewport;
import edu.miami.jgloo.content.ContentManager;
import edu.miami.math.util.Gaussian;

/**
 * Bloom post-processing effect
 * 
 * @author Justin Stoecker
 */
public class Bloom implements GLDisposable {
  private boolean               disposed     = false;

  private ShaderProgram         luminosityShader;
  private ShaderProgram         blurShader;
  private ShaderProgram         compositeShader;

  private Uniform.Float         luminosityThreshold;
  private Uniform.Float         compositeIntensity;
  private Uniform.Int           compositeTex1;
  private Uniform.Int           compositeTex2;

  private FrameBufferObject[]   fullSizeFBOs = new FrameBufferObject[2];
  private FrameBufferObject[]   halfSizeFBOs = new FrameBufferObject[2];

  private Gaussian.BlurParams[] blurParams;
  private float                 blurriness   = 1.5f;
  private int                   samples      = 15;
  private float                 intensity    = 2.4f;
  private float                 threshold    = 0.85f;
  private int                   ulocBlurOffsets;
  private int                   ulocBlurWeights;
  private int                   w, h;

  public void setBlurParams(int w, int h) {
    this.w = w;
    this.h = h;
    this.blurParams = Gaussian.calcBlurParams(blurriness, samples, w, h);
  }
  
  public float getBlurriness() {
    return blurriness;
  }
  
  public float getIntensity() {
    return intensity;
  }
  
  public float getThreshold() {
    return threshold;
  }
  
  public void setBlurriness(float blurriness) {
    this.blurriness = blurriness;
    setBlurParams(w, h);
  }
  
  public void setIntensity(float intensity) {
    this.intensity = intensity;
    compositeIntensity.setValue(intensity);
  }
  
  public void setThreshold(float threshold) {
    this.threshold = threshold;
    luminosityThreshold.setValue(threshold);
  }

  public Bloom() {
  }

  public boolean init(GL2 gl, Viewport viewport, ContentManager content) {

    luminosityShader = content.retrieveShader("luminosity", false, gl).get();
    if (luminosityShader == null) return abortInit(gl, "could not create luminosity shader.");
    blurShader = content.retrieveShader("blur", false, gl).get();
    if (blurShader == null) return abortInit(gl, "could not create blur shader.");
    compositeShader = content.retrieveShader("composite", false, gl).get();
    if (compositeShader == null) return abortInit(gl, "could not create composite shader.");

    luminosityShader.enable(gl);
    luminosityThreshold = new Uniform.Float(gl, luminosityShader, "threshold", threshold);
    luminosityShader.disable(gl);

    blurShader.enable(gl);
    ulocBlurOffsets = blurShader.getUniform(gl, "offsets");
    ulocBlurWeights = blurShader.getUniform(gl, "weights");
    blurShader.disable(gl);

    compositeShader.enable(gl);
    compositeIntensity = new Uniform.Float(gl, compositeShader, "intensity", intensity);
    compositeTex1 = new Uniform.Int(gl, compositeShader, "inputTexture1", 0);
    compositeTex1 = new Uniform.Int(gl, compositeShader, "inputTexture2", 1);
    compositeShader.disable(gl);

    for (int i = 0; i < fullSizeFBOs.length; i++)
      fullSizeFBOs[i] = FrameBufferObject.create(gl, viewport.w, viewport.h, GL.GL_RGBA);
    for (int i = 0; i < halfSizeFBOs.length; i++)
      halfSizeFBOs[i] = FrameBufferObject.create(gl, viewport.w / 2, viewport.h / 2, GL.GL_RGBA);

    setBlurParams(viewport.w / 2, viewport.h / 2);

    return true;
  }

  private boolean abortInit(GL2 gl, String error) {
    System.err.println("Bloom: " + error);
    dispose(gl);
    return false;
  }

  /**
   * Applies bloom effect to an input texture
   * 
   * @param input - texture to process
   */
  public Texture2D process(GL2 gl, Texture2D input) {
    Texture2D brightPassOut = brightPass(gl, input);
    Texture2D blurPassOut = blurPass(gl, brightPassOut);
    return compositePass(gl, input, blurPassOut);
  }

  private Texture2D brightPass(GL2 gl, Texture2D input) {
    halfSizeFBOs[0].setViewport(gl);
    halfSizeFBOs[0].bind(gl);
    halfSizeFBOs[0].clear(gl);
    luminosityShader.enable(gl);
    luminosityThreshold.update(gl);
    input.bind(gl);
    Graphics.renderScreenQuad(gl);
    input.unbind(gl);
    luminosityShader.disable(gl);
    halfSizeFBOs[0].unbind(gl);

    return halfSizeFBOs[0].getColorTexture(0);
  }

  private Texture2D blurPass(GL2 gl, Texture2D input) {
    // blur H
    halfSizeFBOs[1].bind(gl);
    halfSizeFBOs[1].clear(gl);
    blurShader.enable(gl);
    gl.glUniform2fv(ulocBlurOffsets, blurParams[0].offsets.length / 2, blurParams[0].offsets, 0);
    gl.glUniform1fv(ulocBlurWeights, blurParams[0].weights.length, blurParams[0].weights, 0);
    input.bind(gl);
    Graphics.renderScreenQuad(gl);
    input.unbind(gl);
    blurShader.disable(gl);
    halfSizeFBOs[1].unbind(gl);

    // blur V
    halfSizeFBOs[0].bind(gl);
    halfSizeFBOs[0].clear(gl);
    blurShader.enable(gl);
    gl.glUniform2fv(ulocBlurOffsets, this.blurParams[1].offsets.length / 2,
        this.blurParams[1].offsets, 0);
    gl.glUniform1fv(ulocBlurWeights, this.blurParams[1].weights.length, this.blurParams[1].weights,
        0);
    halfSizeFBOs[1].getColorTexture(0).bind(gl);
    Graphics.renderScreenQuad(gl);
    halfSizeFBOs[1].getColorTexture(0).unbind(gl);

    blurShader.disable(gl);
    halfSizeFBOs[0].unbind(gl);

    return halfSizeFBOs[0].getColorTexture(0);
  }

  private Texture2D compositePass(GL2 gl, Texture2D input1, Texture2D input2) {
    fullSizeFBOs[1].setViewport(gl);
    fullSizeFBOs[1].bind(gl);
    fullSizeFBOs[1].clear(gl);
    compositeShader.enable(gl);
    compositeIntensity.update(gl);
    gl.glActiveTexture(GL.GL_TEXTURE0);
    input1.bind(gl);
    gl.glActiveTexture(GL.GL_TEXTURE1);
    input2.bind(gl);

    Graphics.renderScreenQuad(gl);

    compositeShader.disable(gl);
    input2.unbind(gl);
    gl.glActiveTexture(GL.GL_TEXTURE0);
    input1.unbind(gl);
    fullSizeFBOs[1].unbind(gl);

    return fullSizeFBOs[1].getColorTexture(0);
  }

  @Override
  public void dispose(GL gl) {
    if (disposed) return;

    for (FrameBufferObject fbo : halfSizeFBOs)
      if (fbo != null) fbo.dispose(gl);
    for (FrameBufferObject fbo : fullSizeFBOs)
      if (fbo != null) fbo.dispose(gl);

    disposed = true;
  }

  @Override
  public boolean isDisposed() {
    return disposed;
  }

  public void resize(GL2GL3 gl, Viewport screen) {
    for (FrameBufferObject fbo : halfSizeFBOs)
      fbo.resize(gl, screen.w, screen.h);
    for (FrameBufferObject fbo : fullSizeFBOs)
      fbo.resize(gl, screen.w, screen.h);
    setBlurParams(screen.w, screen.h);
  }
}
