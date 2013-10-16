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

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import com.jogamp.common.nio.Buffers;

import edu.miami.jgloo.VertexBuffer;
import edu.miami.jgloo.VertexBuffer.BufferUsage;
import edu.miami.jgloo.vertex.VertexFmt;

public abstract class DynamicObjBuf<T> {

  /** Number of objects that can occupy the VBO before a resize is needed */
  protected int                 capacity       = 10;

  protected final int           vertsPerObject;

  /** Size in bytes per object */
  protected final int           objectSize;

  /** OpenGL primitive type (GL_TRIANGLES, GL_QUADS, ...) */
  protected final int           primitiveMode;

  /** Video memory allocated for the vertex data */
  protected VertexBuffer        vbo;

  /** List of objects contained in this buffer */
  protected ArrayList<T>        objects        = new ArrayList<T>();

  /** Objects that been modified and must be re-uploaded to VBO */
  protected ArrayList<T>        updatedObjects = new ArrayList<T>();

  /** Objects have been added but are not in the VBO */
  protected boolean             addedObjects   = false;

  /** Objects have been removed but are still in the VBO */
  protected boolean             deletedObjects = false;

  /** Flag indicating if all data should be re-uploaded next sync */
  protected boolean             rebuffer       = false;

  /** Maps an object to its index in the objects list */
  protected HashMap<T, Integer> objectMap      = new HashMap<T, Integer>();

  /** Filled with temporary vertex data that is to be transferred to VBO */
  protected ByteBuffer          hostVertexData;

  /** Layout and dimensions of vertex data in VBO */
  protected VertexFmt           vertexFormat;

  public DynamicObjBuf(VertexFmt vertexFormat, int vertsPerObject, int primitiveMode) {
    this.vertexFormat = vertexFormat;
    this.objectSize = vertexFormat.getStride() * vertsPerObject;
    this.vertsPerObject = vertsPerObject;
    this.primitiveMode = primitiveMode;
  }

  public void add(T object) {
    // don't allow duplicate entries
    if (objectMap.get(object) != null) return;

    objectMap.put(object, objects.size());
    objects.add(object);
    addedObjects = true;
  }

  public void remove(T object) {
    Integer removeIndex = objectMap.get(object);
    if (removeIndex != null) {
      // move the last element to replace the element at removeIndex
      T objectToDelete = objects.get(removeIndex);
      T lastObject = objects.get(objects.size() - 1);
      objectMap.remove(objectToDelete);
      objectMap.put(lastObject, removeIndex);
      objects.set(removeIndex, lastObject);
      objects.remove(objects.size() - 1);
      updatedObjects.add(lastObject);

      deletedObjects = true;
    }
  }

  public void modified(T object) {
    if (objectMap.get(object) != null) updatedObjects.add(object);
  }

  /**
   * Sets batch contents and replaces any existing data. Initial capacity is set to 20% more than
   * the number of objects.
   */
  public void setObjects(List<? extends T> objects) {
    setObjects(objects, (int) (1.2f * objects.size()));
  }

  /**
   * Sets batch contents and replaces any existing data. Specify the capacity to balance expected
   * growth with memory usage.
   */
  public void setObjects(List<? extends T> objects, int initialCapacity) {
    capacity = Math.max(1, Math.max(objects.size(), initialCapacity));
    this.objects.clear();
    objectMap.clear();

    for (int i = 0; i < objects.size(); i++) {
      T curObject = objects.get(i);
      this.objects.add(curObject);
      objectMap.put(curObject, i);
    }

    bufferAll();
  }

  public void rebuffer() {
    this.rebuffer = true;
  }

  protected void bufferAll() {
    hostVertexData = Buffers.newDirectByteBuffer(capacity * objectSize);
    for (int i = 0; i < objects.size(); i++)
      bufferObject(hostVertexData, objects.get(i));
    hostVertexData.rewind();
  }

  public abstract void bufferObject(ByteBuffer buf, T object);

  protected void transferNewData(GL2 gl) {
    vbo.setData(null, hostVertexData.capacity());
    vbo.subData(hostVertexData, hostVertexData.capacity(), 0);
    hostVertexData = null;
  }

  public void syncData(GL2 gl) {

    if (addedObjects || deletedObjects || rebuffer) {
      while (objects.size() > capacity) {
        capacity *= 2;
      }
      while (objects.size() <= capacity / 4) {
        capacity /= 2;
      }
      addedObjects = false;
      addedObjects = false;
      rebuffer = false;
      bufferAll();
      transferNewData(gl);
    } else if (updatedObjects.size() > 0) {
      // no need to update objects if everything has been re-buffered
      ByteBuffer buf = Buffers.newDirectByteBuffer(updatedObjects.size() * objectSize);
      for (int i = 0; i < updatedObjects.size(); i++)
        bufferObject(buf, updatedObjects.get(i));
      buf.rewind();

      for (int i = 0; i < updatedObjects.size(); i++) {
        buf.position(i * objectSize);
        long offset = objectMap.get(updatedObjects.get(i)) * objectSize;
        vbo.subData(buf, objectSize, offset);
      }
    }

    updatedObjects.clear();
  }

  public void dispose(GL gl) {
    vbo.dispose(gl);
  }

  public void setRenderState(GL2 gl) {
    if (vbo == null) {
      vbo = new VertexBuffer(gl, BufferUsage.DYNAMIC);
      vbo.bind();
      vbo.setData(null, capacity * objectSize);
    } else {
      vbo.bind();
    }
    vertexFormat.setState(gl);
  }

  public void unsetRenderState(GL2 gl) {
    vertexFormat.unsetState(gl);
    vbo.unbind();
  }

  public void render(GL2 gl) {
    setRenderState(gl);
    syncData(gl);
    gl.glDrawArrays(primitiveMode, 0, vertsPerObject * objects.size());
    unsetRenderState(gl);
  }
}
