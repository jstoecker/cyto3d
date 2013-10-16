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

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import com.jogamp.common.nio.Buffers;

import edu.miami.jgloo.IndexBuffer;
import edu.miami.jgloo.VertexBuffer.BufferUsage;
import edu.miami.jgloo.vertex.VertexFmt;

/**
 * Dynamic list of objects that are rendered with an index and vertex buffer object pair.
 */
public abstract class DynamicIndexedObjBuf<T> extends DynamicObjBuf<T> {

  /** Video memory allocated for the index data */
  IndexBuffer   ibo;

  /** Temporary index data that is to be transferred to index buffer */
  ByteBuffer    hostIndexData;

  /** Type of index data (GL_UNSIGNED_SHORT, GL_UNSIGNED_INTEGER, ...) */
  int           indexType;

  /** Size of indices in bytes */
  int           indexSize;

  /** Number of indices used per base object render */
  final int     indicesPerObject;

  /** Indices of the object all object instances are based on */
  final int[][] baseIndices;

  public DynamicIndexedObjBuf(VertexFmt vertexFormat, int vertsPerObject, int primitiveMode,
      int[][] baseIndices) {
    super(vertexFormat, vertsPerObject, primitiveMode);
    this.baseIndices = baseIndices;
    this.indicesPerObject = baseIndices.length * baseIndices[0].length;
  }

  private void updateIndexType() {
    int maxIndexVal = vertsPerObject * capacity - 1;

    // use unsigned shorts if possible (max ushort = 2^16-1 or 65535)
    if (maxIndexVal < 65536) {
      indexType = GL.GL_UNSIGNED_SHORT;
      indexSize = 2;
    } else {
      indexType = GL.GL_UNSIGNED_INT;
      indexSize = 4;
    }
  }

  @Override
  protected void bufferAll() {
    super.bufferAll();
    bufferIndices();
  }

  private void bufferIndices() {
    updateIndexType();
    int numIndices = capacity * indicesPerObject;
    hostIndexData = Buffers.newDirectByteBuffer(numIndices * indexSize);

    if (indexType == GL.GL_UNSIGNED_SHORT) {
      for (int i = 0; i < capacity; i++)
        for (int j = 0; j < baseIndices.length; j++)
          for (int k = 0; k < baseIndices[j].length; k++)
            hostIndexData.putShort((short) (baseIndices[j][k] + i * vertsPerObject));
    } else {
      for (int i = 0; i < capacity; i++)
        for (int j = 0; j < baseIndices.length; j++)
          for (int k = 0; k < baseIndices[j].length; k++)
            hostIndexData.putInt(baseIndices[j][k] + i * vertsPerObject);
    }

    hostIndexData.rewind();
  }

  @Override
  protected void transferNewData(GL2 gl) {
    super.transferNewData(gl);
    if (hostIndexData == null) bufferIndices();
    transferIndices(gl);
  }

  private void transferIndices(GL2 gl) {
    ibo.setData(null, hostIndexData.capacity());
    ibo.subData(hostIndexData, hostIndexData.capacity(), 0);
    hostIndexData = null;
  }

  @Override
  public void dispose(GL gl) {
    super.dispose(gl);
    ibo.dispose(gl);
  }

  @Override
  public void setRenderState(GL2 gl) {
    super.setRenderState(gl);
    if (ibo == null) {
      ibo = new IndexBuffer(gl, BufferUsage.STATIC);
      ibo.bind();
      bufferIndices();
      transferIndices(gl);
    } else {
      ibo.bind();
    }
  }

  @Override
  public void unsetRenderState(GL2 gl) {
    super.unsetRenderState(gl);
    ibo.unbind();
  }

  @Override
  public void render(GL2 gl) {
    setRenderState(gl);
    syncData(gl);
    int totalIndices = objects.size() * indicesPerObject;
    gl.glDrawElements(primitiveMode, totalIndices, indexType, 0);
    unsetRenderState(gl);
  }
}
