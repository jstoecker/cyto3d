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

package edu.miami.math.geom;

/**
 * Icosahedron shape
 * 
 * @author Justin Stoecker
 */
public class Icosahedron {

  // indices for vertices of each triangle face (20 faces total)
  protected static final int[][] TRI_VERTS  = { { 0, 1, 2 }, { 0, 2, 3 }, { 0, 3, 4 }, { 0, 4, 5 },
      { 0, 5, 1 }, { 1, 9, 2 }, { 2, 9, 10 }, { 2, 10, 3 }, { 3, 10, 6 }, { 3, 6, 4 }, { 4, 6, 7 },
      { 4, 7, 5 }, { 5, 7, 8 }, { 5, 8, 1 }, { 1, 8, 9 }, { 11, 6, 10 }, { 11, 10, 9 },
      { 11, 9, 8 }, { 11, 8, 7 }, { 11, 7, 6 } };

  // indices for edges of each triangle face (20 faces total)
  protected static final int[][] TRI_EDGES  = { { 0, 1, 2 }, { 2, 3, 4 }, { 4, 5, 6 }, { 6, 7, 8 },
      { 8, 9, 0 }, { 10, 11, 1 }, { 11, 12, 13 }, { 13, 14, 3 }, { 14, 15, 16 }, { 16, 17, 5 },
      { 17, 18, 19 }, { 19, 20, 7 }, { 20, 21, 22 }, { 22, 23, 9 }, { 23, 24, 10 }, { 25, 15, 26 },
      { 26, 12, 27 }, { 27, 24, 28 }, { 28, 21, 29 }, { 29, 18, 25 }, };

  // indices for vertices of each edge (30 edges total)
  protected static final int[][] EDGE_VERTS = { { 0, 1 }, { 1, 2 }, { 2, 0 }, { 2, 3 }, { 3, 0 },
      { 3, 4 }, { 4, 0 }, { 4, 5 }, { 5, 0 }, { 5, 1 }, { 1, 9 }, { 9, 2 }, { 9, 10 }, { 10, 2 },
      { 10, 3 }, { 10, 6 }, { 6, 3 }, { 6, 4 }, { 6, 7 }, { 7, 4 }, { 7, 5 }, { 7, 8 }, { 8, 5 },
      { 8, 1 }, { 8, 9 }, { 11, 6 }, { 11, 10 }, { 9, 11 }, { 8, 11 }, { 7, 11 } };

  protected float[][]            verts;

  /** Returns indices for edges in each triangle */
  public int[][] getTriangleEdges() {
    return TRI_EDGES;
  }

  /** Returns indices for vertices in each triangle */
  public int[][] getTriangleVertices() {
    return TRI_VERTS;
  }

  /** Returns vertices of the icosahedron */
  public float[][] getVertices() {
    return verts;
  }

  /** Returns indices for vertices in each edge */
  public int[][] getEdgeVertices() {
    return EDGE_VERTS;
  }

  public Icosahedron(float side) {
    float hs = side / 2;
    float piOver5 = (float) (Math.PI / 5.0);
    float t2 = piOver5 / 2;
    float t4 = piOver5;
    float R = (float) ((side / 2) / Math.sin(t4));
    float H = (float) (Math.cos(t4) * R);
    float Cx = (float) (R * Math.cos(t2));
    float Cy = (float) (R * Math.sin(t2));
    float H1 = (float) (Math.sqrt(side * side - R * R));
    float H2 = (float) (Math.sqrt((H + R) * (H + R) - H * H));
    float Z2 = (H2 - H1) / 2;
    float Z1 = Z2 + H1;

    verts = new float[][] { { 0, Z1, 0 }, { 0, Z2, R }, { Cx, Z2, Cy }, { hs, Z2, -H },
        { -hs, Z2, -H }, { -Cx, Z2, Cy }, { 0, -Z2, -R }, { -Cx, -Z2, -Cy }, { -hs, -Z2, H },
        { hs, -Z2, H }, { Cx, -Z2, -Cy }, { 0, -Z1, 0 }, };
  }
}
