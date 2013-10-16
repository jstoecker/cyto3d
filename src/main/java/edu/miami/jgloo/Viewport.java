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

package edu.miami.jgloo;

import java.awt.Point;

import javax.media.opengl.GL;

import edu.miami.math.vector.Vec3;
import edu.miami.math.vector.Vec4;

public class Viewport {
  public final int   x, y, w, h;
  public final float aspect;

  public Viewport(int x, int y, int w, int h) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    aspect = w / (float) h;
  }

  public Viewport(int[] values) {
    this.x = values[0];
    this.y = values[1];
    this.w = values[2];
    this.h = values[3];
    aspect = w / (float) h;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getW() {
    return w;
  }

  public int getH() {
    return h;
  }

  public float getAspect() {
    return aspect;
  }

  public boolean contains(Point p) {
    return p.x >= x && p.x <= x + w && p.y >= y && p.y <= y + h;
  }

  public int[] getValues() {
    return new int[] { x, y, w, h };
  }

  public void apply(GL gl) {
    gl.glViewport(x, y, w, h);
  }

  /**
   * Transforms normalized device coordinates to window coordinates
   * 
   * @param ndc
   * @return
   */
  public Vec3 transform(Vec4 ndc, float near, float far) {
    float hW = w / 2;
    float hH = h / 2;
    float wx = hW * ndc.x + (this.x + hW);
    float wy = hH * ndc.y + (this.y + hH);
    // float wz = (far - near) / 2 * ndc.z + (far + near) / 2;
    return new Vec3(wx, wy, ndc.z);
  }

  @Override
  public String toString() {
    return String.format("{%d, %d, %d, %d}\n", x, y, w, h);
  }

  public boolean equals(Viewport that) {
    if (that == null) return false;
    return this.x == that.x && this.y == that.y && this.w == that.w && this.h == that.h;
  }
}
