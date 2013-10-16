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
import java.util.Collections;
import java.util.Comparator;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import edu.miami.jgloo.GLDisposable;
import edu.miami.math.geom.BoundingBox;
import edu.miami.math.vector.Mat4;

/**
 * A mesh is a collection of mesh parts that form a model. The mesh contains all the vertices. Upon
 * initialization, a specific rendering mode is assigned to this mesh (immediate mode, vertex
 * arrays, display lists, or VBOs).
 * 
 * @author Justin Stoecker
 */
public class Mesh implements GLDisposable {

  /**
   * Determines how mesh data is organized and which commands are issued to render the mesh. The
   * default mode is VBO, or vertex buffer objects, which is usually the fastest. Other modes are
   * provided for compatibility with older GPUs.
   */
  public enum RenderMode {
    /** Default rendering mode. Fastest */
    VBO,
    /** Use if VBO is not supported. */
    VERTEX_ARRAYS,
    /** Use if VBO is not supported */
    DISPLAY_LIST,
    /** Slowest rendering. Use only if other modes are not available */
    IMMEDIATE
  }

  protected MeshRenderer          renderer;
  protected ArrayList<MeshVertex> vertices;
  protected ArrayList<MeshPart>   parts;
  protected BoundingBox           bounds;
  protected boolean               useTexCoords = false;
  protected boolean               useNormals   = false;
  protected boolean               disposed     = false;
  protected RenderMode            mode;

  public ArrayList<MeshPart> getParts() {
    return parts;
  }

  public BoundingBox getBounds() {
    return bounds;
  }

  public ArrayList<MeshVertex> getVertices() {
    return vertices;
  }

  public void setBounds(BoundingBox bounds) {
    this.bounds = bounds;
  }

  public Mesh() {
    parts = new ArrayList<MeshPart>();
    vertices = new ArrayList<MeshVertex>();
  }

  public Mesh(ArrayList<MeshVertex> vertices) {
    this();
    this.vertices = vertices;
  }

  public void addPart(MeshPart part) {
    if (!parts.contains(part)) parts.add(part);
  }

  public void addVertex(MeshVertex vertex) {
    vertices.add(vertex);
  }

  /**
   * Initialize mesh for rendering using a specified rendering mode
   */
  public void init(GL2 gl, RenderMode mode) {
    this.mode = mode;
    useNormals = vertices.get(0).getNormal() != null;
    useTexCoords = vertices.get(0).getTexCoords() != null;
    switch (mode) {
    case VBO:
      renderer = new MeshRendererVBO();
      break;
    case DISPLAY_LIST:
      renderer = new MeshRendererDisplayList();
      break;
    case VERTEX_ARRAYS:
      renderer = new MeshRendererVertexArrays();
      break;
    case IMMEDIATE:
      renderer = new MeshRendererImmediate();
      break;
    }

    initMaterials(gl);
    renderer.init(gl, this);
  }

  /**
   * Initializes materials: load textures and sorts parts by transparency. This does not need to be
   * called if init() is used; it's only necessary to call this if using immediate mode rendering.
   * 
   * @param gl
   */
  private void initMaterials(GL2 gl) {
    for (MeshPart part : parts)
      part.getMaterial().init(gl);

    // if there are any transparent parts, push them to end of list so they
    // render last
    Collections.sort(parts, new Comparator<MeshPart>() {
      public int compare(MeshPart o1, MeshPart o2) {
        int o1t = o1.getMaterial().containsTransparency ? 1 : 0;
        int o2t = o2.getMaterial().containsTransparency ? -1 : 0;
        return o1t + o2t;
      }
    });
  }

  /**
   * All-in-one method for rendering the mesh in a single statement. Sets the mesh state, renders
   * all mesh parts, then unsets the state.
   */
  public void render(GL2 gl) {
    set(gl);
    renderer.render(gl);
    unset(gl);
  }

  /**
   * All-in-one method for rendering the mesh in a single statement. Sets the mesh state, renders
   * all mesh parts, then unsets the state. Uses the supplied model matrix to position, scale, and
   * rotate the mesh.
   * 
   * @param modelMatrix - transformation matrix to translate, scale, and rotate mesh to desired
   *          configuration
   */
  public void render(GL2 gl, Mat4 modelMatrix) {
    set(gl);
    renderInstance(gl, modelMatrix);
    unset(gl);
  }

  /**
   * Renders a single instance of the mesh, assuming the mesh state has been set. This method is
   * intended to be used when batching the rendering the same mesh repeatedly with different
   * transformations. Proper usage:<br>
   * <code>
   * mesh.set(gl);<br>
   * for (int i = 0; i < instances.length; i++)<br>
   * <t>mesh.renderInstance(gl, modelMatrix[i]);<br>
   * mesh.unset(gl);
   * </code>
   * 
   * @param gl
   */
  public void renderInstance(GL2 gl, Mat4 modelMatrix) {
    gl.glPushMatrix();
    gl.glMultMatrixf(modelMatrix.values(), 0);
    renderer.render(gl);
    gl.glPopMatrix();
  }

  /**
   * Sets up the mesh state prior to rendering. This should only be used in conjunction with the
   * renderInstance method. It is automatically called by the render method.
   */
  public void set(GL2 gl) {
    renderer.setState(gl);
  }

  /**
   * Unsets the mesh state after rendering. This should only be used in conjunction with the
   * renderInstance method. It is automatically called by the render method.
   */
  public void unset(GL2 gl) {
    renderer.unsetState(gl);
  }

  @Override
  public void dispose(GL gl) {
    if (renderer != null) renderer.dispose(gl);
    for (MeshPart part : parts)
      part.dispose(gl);
    disposed = true;
  }

  @Override
  public boolean isDisposed() {
    return disposed;
  }

  public void printInfo() {
    System.out.println("Mesh Info:");
    System.out.println("Num Verts: " + vertices.size());
    for (MeshPart part : parts)
      part.printInfo();
  }
}
