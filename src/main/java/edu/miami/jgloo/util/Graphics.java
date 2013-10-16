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

package edu.miami.jgloo.util;

import javax.media.opengl.GL2;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.media.opengl.glu.GLU;

import edu.miami.jgloo.Viewport;
import edu.miami.math.geom.Ray;
import edu.miami.math.vector.Mat4;
import edu.miami.math.vector.RVec3;
import edu.miami.math.vector.RVec4;
import edu.miami.math.vector.Vec3;

/**
 * Graphics math routines
 * 
 * @author Justin Stoecker
 */
public class Graphics {

  private static GLU glu = new GLU();

  public static Vec3 project(RVec3 p, Mat4 modelView, Mat4 proj, Viewport viewport) {
    return project(proj.transform(modelView.transform(p.x(), p.y(), p.z(), 1)), viewport);
  }

  private static Vec3 project(RVec4 p, Viewport viewport) {
    // clip space -> normalized device coordinates
    float x = p.x() / p.w();
    float y = p.y() / p.w();
    float z = p.z() / p.w();

    float winX = viewport.x + (viewport.w * (x + 1f) / 2f); // NDC.x -> winX
    float winY = viewport.y + (viewport.w * (y + 1f) / 2f); // NDC.y -> winY
    float winZ = (z + 1f) / 2f; // NDC.z -> winZ
    return new Vec3(winX, winY, winZ);
  }

  public static Ray unproject(float x, float y, Mat4 modelView, Mat4 proj, Viewport viewport) {
    float[] modelVals = modelView.values();
    float[] projVals = proj.values();
    int[] viewVals = new int[] { viewport.x, viewport.y, viewport.w, viewport.h };
    Vec3 pNear = unproject(x, y, 0, modelVals, projVals, viewVals);
    Vec3 pFar = unproject(x, y, 1, modelVals, projVals, viewVals);
    return new Ray(pNear, pFar.minus(pNear).normalize());
  }

  private static Vec3 unproject(float x, float y, float z, float[] model, float[] proj,
      int[] viewport) {
    float[] objPos = new float[3];
    glu.gluUnProject(x, y, z, model, 0, proj, 0, viewport, 0, objPos, 0);
    return new Vec3((float) objPos[0], (float) objPos[1], (float) objPos[2]);
  }

  public static void renderScreenQuad(GL2 gl) {
    gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
    gl.glPushMatrix();
    gl.glLoadIdentity();

    gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
    gl.glPushMatrix();
    gl.glLoadIdentity();

    gl.glBegin(GL2.GL_QUADS);
    gl.glTexCoord2f(0, 0);
    gl.glVertex2f(-1, -1);
    gl.glTexCoord2f(1, 0);
    gl.glVertex2f(1, -1);
    gl.glTexCoord2f(1, 1);
    gl.glVertex2f(1, 1);
    gl.glTexCoord2f(0, 1);
    gl.glVertex2f(-1, 1);
    gl.glEnd();

    gl.glPopMatrix();
    gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
    gl.glPopMatrix();
  }
}
