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

import edu.miami.math.geom.Icosahedron;

public class GLIcosahedron extends Icosahedron {

  public GLIcosahedron(float side) {
    super(side);
  }

  public void render(GL2 gl) {
    gl.glBegin(GL.GL_TRIANGLES);
    for (int i = 0; i < TRI_VERTS.length; i++) {
      gl.glVertex3fv(verts[TRI_VERTS[i][0]], 0);
      gl.glVertex3fv(verts[TRI_VERTS[i][1]], 0);
      gl.glVertex3fv(verts[TRI_VERTS[i][2]], 0);
    }
    gl.glEnd();
  }

  public void renderFace(GL2 gl, int face, TextRenderer tr) {

    // render solid face
    gl.glBegin(GL.GL_TRIANGLES);
    float[] v0 = verts[TRI_VERTS[face][0]];
    float[] v1 = verts[TRI_VERTS[face][1]];
    float[] v2 = verts[TRI_VERTS[face][2]];
    gl.glVertex3fv(v0, 0);
    gl.glVertex3fv(v1, 0);
    gl.glVertex3fv(v2, 0);
    gl.glEnd();

    int[] triEdgeIndices = TRI_EDGES[face];

    gl.glColor3f(0, 0, 1);
    gl.glLineWidth(6);
    gl.glBegin(GL.GL_LINES);
    for (int i = 0; i < 3; i++) {

      // if indices don't match, the edge's index order must be flipped
      float c = 1;
      if (TRI_VERTS[face][i] != EDGE_VERTS[triEdgeIndices[i]][0]) {
        c = 0;
      }

      gl.glColor3f(1 - c, 0, c);
      gl.glVertex3fv(verts[EDGE_VERTS[triEdgeIndices[i]][0]], 0);
      gl.glColor3f(c, 0, 1 - c);
      gl.glVertex3fv(verts[EDGE_VERTS[triEdgeIndices[i]][1]], 0);
    }
    gl.glEnd();
    gl.glLineWidth(1);

    tr.begin3DRendering();
    tr.draw3D("A", v0[0], v0[1], v0[2], 0.04f);
    tr.draw3D("B", v1[0], v1[1], v1[2], 0.04f);
    tr.draw3D("C", v2[0], v2[1], v2[2], 0.04f);
    tr.end3DRendering();
  }

  public void renderEdges(GL2 gl, TextRenderer tr) {
    gl.glColor3f(0.5f, 0.5f, 0.5f);
    gl.glBegin(GL.GL_LINES);
    for (int i = 0; i < EDGE_VERTS.length; i++) {
      gl.glVertex3fv(verts[EDGE_VERTS[i][0]], 0);
      gl.glVertex3fv(verts[EDGE_VERTS[i][1]], 0);
    }
    // gl.glVertex3fv(verts[triangles[face][0]], 0);
    // gl.glVertex3fv(verts[triangles[face][1]], 0);
    // gl.glVertex3fv(verts[triangles[face][2]], 0);
    gl.glEnd();
  }

  public void renderPts(TextRenderer tr) {
    tr.begin3DRendering();
    for (int i = 0; i < verts.length; i++)
      tr.draw3D("" + i, verts[i][0], verts[i][1], verts[i][2], 0.03f);
    tr.end3DRendering();
  }
}
