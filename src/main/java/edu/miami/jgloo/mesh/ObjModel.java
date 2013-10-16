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

package edu.miami.jgloo.mesh;

import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import edu.miami.jgloo.GLDisposable;
import edu.miami.math.geom.BoundingBox;

/**
 * A model loaded from a Wavefront OBJ file.<br>
 * Supports the following vertex data:<br>
 * -Geometric vertices (v)<br>
 * -Texture vertices (vt)<br>
 * -Vertex normals (vn)<br>
 * <br>
 * Supported the following elements:<br>
 * -Points (p)<br>
 * -Lines (p)<br>
 * -Faces (f)<br>
 * <br>
 * Supported grouping types:<br>
 * -Group name (g)<br>
 * -Object name(o) <br>
 * -Smoothing group (s)<br>
 * 
 * @author Justin Stoecker
 */
public class ObjModel implements GLDisposable {

  /**
   * Contains indexing information for vertex positions, normals, and texture coordinates. The
   * indices stored in these objects are 0-indexed, but the input string should be 1-indexed
   * (according to OBJ format).
   */
  public static class Face {

    int         vertIndices[]     = null;
    int         normalIndices[]   = null;
    int         texCoordIndices[] = null;
    ObjMaterial material;

    public Face(String line, ObjMaterial material) {
      String[] parts = line.split("\\s+");
      vertIndices = new int[parts.length - 1];
      this.material = material;

      String[] subparts = parts[1].split("/");
      if (subparts.length == 1) {
        readVertexFormat(parts);
      } else if (subparts.length == 2) {
        readVertexTexCoordsFormat(parts);
      } else if (subparts[1].equals("")) {
        readVertexNormalFormat(parts);
      } else {
        readVertexTexCoordsNormalFormat(parts);
      }
    }

    private void readVertexFormat(String[] parts) {
      // format: f v v v ...
      // where v = vertex index

      for (int i = 0; i < vertIndices.length; i++) {
        vertIndices[i] = Integer.parseInt(parts[i + 1]) - 1;
      }
    }

    private void readVertexNormalFormat(String[] parts) {
      // format: f v//vn v//vn v//vn ...
      // where v = vertex index, vn = normal index

      normalIndices = new int[vertIndices.length];
      for (int i = 0; i < vertIndices.length; i++) {
        String[] subparts = parts[i + 1].split("/");
        vertIndices[i] = Integer.parseInt(subparts[0]) - 1;
        normalIndices[i] = Integer.parseInt(subparts[2]) - 1;
      }
    }

    private void readVertexTexCoordsFormat(String[] parts) {
      // format: f v/vt v/vt v/vt ...
      // where v = vertex index, vt = normal index

      texCoordIndices = new int[vertIndices.length];
      for (int i = 0; i < vertIndices.length; i++) {
        String[] subparts = parts[i + 1].split("/");
        vertIndices[i] = Integer.parseInt(subparts[0]) - 1;
        texCoordIndices[i] = Integer.parseInt(subparts[1]) - 1;
      }
    }

    private void readVertexTexCoordsNormalFormat(String[] parts) {
      // format: f v/vt/vn v/vt/vn v/vt/vn ...
      // where v = vertex index, vt = normal index, vn = normal index

      texCoordIndices = new int[vertIndices.length];
      normalIndices = new int[vertIndices.length];
      for (int i = 0; i < vertIndices.length; i++) {
        String[] subparts = parts[i + 1].split("/");
        vertIndices[i] = Integer.parseInt(subparts[0]) - 1;
        texCoordIndices[i] = Integer.parseInt(subparts[1]) - 1;
        normalIndices[i] = Integer.parseInt(subparts[2]) - 1;
      }
    }
  }

  private ArrayList<ObjGroup> groups    = new ArrayList<ObjGroup>();
  private BoundingBox         bounds;
  private ArrayList<float[]>  verts     = new ArrayList<float[]>();
  private ArrayList<float[]>  normals   = new ArrayList<float[]>();
  private ArrayList<float[]>  texCoords = new ArrayList<float[]>();
  private ObjMaterialLibrary  mtllib    = null;
  private boolean             disposed  = false;

  public BoundingBox getBounds() {
    return bounds;
  }

  private ObjModel() {
  }

  protected static float[] readFloatValues(String line) {
    String[] parts = line.split("\\s+");
    float[] pos = new float[parts.length - 1];
    for (int i = 0; i < pos.length; i++)
      pos[i] = Float.parseFloat(parts[i + 1]);
    return pos;
  }

  public void render(GL2 gl) {
    ObjMaterial currentMaterial = null;
    for (ObjGroup group : groups) {
      for (Face face : group.faces) {

        if (currentMaterial == null || face.material != currentMaterial) {
          currentMaterial = face.material;
          currentMaterial.apply(gl);
        }
        gl.glBegin(GL2.GL_POLYGON);
        for (int i = 0; i < face.vertIndices.length; i++) {
          if (face.normalIndices != null) gl.glNormal3fv(normals.get(face.normalIndices[i]), 0);
          if (face.texCoordIndices != null)
            gl.glTexCoord2fv(texCoords.get(face.texCoordIndices[i]), 0);
          gl.glVertex3fv(verts.get(face.vertIndices[i]), 0);
        }
        gl.glEnd();
      }
    }
  }

  @Override
  public void dispose(GL gl) {
    for (ObjMaterial m : mtllib.materials)
      if (m.texture != null) m.texture.dispose(gl);
    disposed = true;
  }

  @Override
  public boolean isDisposed() {
    return disposed;
  }
}
