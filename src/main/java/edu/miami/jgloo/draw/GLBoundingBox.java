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

import edu.miami.math.geom.BoundingBox;
import edu.miami.math.vector.Vec3;

public class GLBoundingBox extends BoundingBox {

  public GLBoundingBox(Vec3 min, Vec3 max) {
    super(min, max);
  }

  public void render(GL2 gl) {
    Vec3 d = getDiag();
    Vec3 u = new Vec3(0, d.y, 0);

    Vec3 v0 = min;
    Vec3 v1 = min.plus(new Vec3(d.x, 0, 0));
    Vec3 v2 = min.plus(new Vec3(d.x, 0, d.z));
    Vec3 v3 = min.plus(new Vec3(0, 0, d.z));
    Vec3 v4 = v0.plus(u);
    Vec3 v5 = v1.plus(u);
    Vec3 v6 = v2.plus(u);
    Vec3 v7 = v3.plus(u);

    gl.glBegin(GL.GL_LINE_LOOP);
    gl.glVertex3fv(v0.values(), 0);
    gl.glVertex3fv(v1.values(), 0);
    gl.glVertex3fv(v2.values(), 0);
    gl.glVertex3fv(v3.values(), 0);
    gl.glEnd();

    gl.glBegin(GL.GL_LINE_LOOP);
    gl.glVertex3fv(v4.values(), 0);
    gl.glVertex3fv(v5.values(), 0);
    gl.glVertex3fv(v6.values(), 0);
    gl.glVertex3fv(v7.values(), 0);
    gl.glEnd();

    gl.glBegin(GL.GL_LINES);
    gl.glVertex3fv(v0.values(), 0);
    gl.glVertex3fv(v4.values(), 0);
    gl.glVertex3fv(v1.values(), 0);
    gl.glVertex3fv(v5.values(), 0);
    gl.glVertex3fv(v2.values(), 0);
    gl.glVertex3fv(v6.values(), 0);
    gl.glVertex3fv(v3.values(), 0);
    gl.glVertex3fv(v7.values(), 0);
    gl.glEnd();
  }
}
