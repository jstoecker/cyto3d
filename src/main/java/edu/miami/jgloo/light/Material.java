/*******************************************************************************
 * Copyright 2011 Justin Stoecker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package edu.miami.jgloo.light;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.fixedfunc.GLLightingFunc;

import edu.miami.math.util.GMath;
import edu.miami.math.vector.Vec4;

/**
 * Data structure that stores material properties used in OpenGL lighting
 */
public class Material {
  protected static final int MAX_SHININESS = 128;

  protected float[]          emission      = new float[] { 0, 0, 0, 1 };
  protected float[]          diffuse       = new float[] { 0.8f, 0.8f, 0.8f, 1 };
  protected float[]          ambient       = new float[] { 0.2f, 0.2f, 0.2f, 1 };
  protected float[]          specular      = new float[] { 0, 0, 0, 1 };
  protected int              shininess     = 96;

  public float[] getEmission() {
    return emission.clone();
  }

  public float[] getDiffuse() {
    return diffuse.clone();
  }

  public float[] getAmbient() {
    return ambient.clone();
  }

  public float[] getSpecular() {
    return specular.clone();
  }

  public int getShininess() {
    return shininess;
  }

  public void setEmission(float[] color) {
    System.arraycopy(color, 0, emission, 0, Math.min(color.length, 4));
  }

  public void setEmission(float r, float g, float b, float a) {
    emission = new float[] { r, g, b, a };
  }

  public void setEmission(Vec4 emission) {
    this.emission = emission.values();
  }

  public void setAmbient(float[] color) {
    System.arraycopy(color, 0, ambient, 0, Math.min(color.length, 4));
  }

  public void setAmbient(float r, float g, float b, float a) {
    ambient = new float[] { r, g, b, a };
  }

  public void setAmbient(Vec4 ambient) {
    this.ambient = ambient.values();
  }

  public void setDiffuse(float[] color) {
    System.arraycopy(color, 0, diffuse, 0, Math.min(color.length, 4));
  }

  public void setDiffuse(float r, float g, float b, float a) {
    diffuse = new float[] { r, g, b, a };
  }

  public void setDiffuse(Vec4 diffuse) {
    this.diffuse = diffuse.values();
  }

  public void setDiffAmbient(float r, float g, float b, float a) {
    setDiffuse(r, g, b, a);
    setAmbient(r, g, b, a);
  }

  public void setSpecular(float[] color) {
    System.arraycopy(color, 0, specular, 0, Math.min(color.length, 4));
  }

  public void setSpecular(float r, float g, float b, float a) {
    specular = new float[] { r, g, b, a };
  }

  public void setSpecular(Vec4 specular) {
    this.specular = specular.values();
  }

  public void setShininess(int shininess) {
    this.shininess = GMath.clamp(shininess, 0, MAX_SHININESS);
  }

  /** Applies material to a specific face */
  public void apply(GL2 gl, int face) {
    gl.glMaterialfv(face, GLLightingFunc.GL_EMISSION, emission, 0);
    gl.glMaterialfv(face, GLLightingFunc.GL_SPECULAR, specular, 0);
    gl.glMaterialfv(face, GLLightingFunc.GL_AMBIENT, ambient, 0);
    gl.glMaterialfv(face, GLLightingFunc.GL_DIFFUSE, diffuse, 0);
    gl.glMateriali(face, GLLightingFunc.GL_SHININESS, shininess);
  }

  /** Applies material to front and back faces */
  public void apply(GL2 gl) {
    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GLLightingFunc.GL_EMISSION, emission, 0);
    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GLLightingFunc.GL_SPECULAR, specular, 0);
    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GLLightingFunc.GL_AMBIENT, ambient, 0);
    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GLLightingFunc.GL_DIFFUSE, diffuse, 0);
    gl.glMateriali(GL.GL_FRONT_AND_BACK, GLLightingFunc.GL_SHININESS, shininess);
  }
}
