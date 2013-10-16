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

package edu.miami.jgloo.draw;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import com.jogamp.opengl.util.awt.TextRenderer;

import edu.miami.math.geom.Geosphere;
import edu.miami.math.vector.Vec3;

public class GLGeosphere extends Geosphere {

  public GLGeosphere(float radius, int level) {
    super(radius, level);
  }

  public void render(GL2 gl) {
    gl.glBegin(GL.GL_TRIANGLES);
    for (int i = 0; i < triangles.length; i++) {
      for (int j = 0; j < 3; j++) {
        Vec3 v = new Vec3(verts[triangles[i][j]]);
        gl.glNormal3fv(v.normalize().values(), 0);
        gl.glVertex3fv(v.values(), 0);
      }
    }
    gl.glEnd();
  }

  public void renderPts(GL2 gl, TextRenderer tr) {
    gl.glColor3f(0, 0, 1);
    gl.glPointSize(6);
    gl.glBegin(GL.GL_POINTS);
    for (int i = 0; i < verts.length; i++) {
      gl.glVertex3fv(verts[i], 0);
    }
    gl.glEnd();
    gl.glPointSize(1);

    tr.begin3DRendering();
    for (int i = 0; i < verts.length; i++)
      tr.draw3D("" + i, verts[i][0], verts[i][1], verts[i][2], 0.015f);
    tr.end3DRendering();
  }
}
