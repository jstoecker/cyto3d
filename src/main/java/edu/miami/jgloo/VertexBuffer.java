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

import java.nio.Buffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2ES2;

import com.jogamp.common.nio.Buffers;

/**
 * A buffer used to store vertex data (position, normal, color, etc.) in video memory for
 * non-immediate-mode rendering. Using a VBO will greatly improve performance over immediate
 * drawing.
 * 
 * @author Justin Stoecker
 */
public class VertexBuffer implements GLDisposable {
  /** Hint for how the buffer's memory should be allocated */
  public static enum BufferUsage {
    /** GL_STATIC_DRAW: no changes to buffered data */
    STATIC(GL.GL_STATIC_DRAW),

    /** GL_DYNAMIC_DRAW: frequent changes to buffered data */
    DYNAMIC(GL.GL_DYNAMIC_DRAW),

    /** GL_STREAM_DRAW: buffered data changes every frame */
    STREAM(GL2ES2.GL_STREAM_DRAW);

    public final int glValue;

    BufferUsage(int glValue) {
      this.glValue = glValue;
    }
  }

  private boolean disposed = false;
  private GL      gl;
  private int     id;
  private int     usage;

  public VertexBuffer(GL gl, BufferUsage usage) {
    IntBuffer idBuf = Buffers.newDirectIntBuffer(1);
    gl.glGenBuffers(1, idBuf);
    this.id = idBuf.get();
    this.gl = gl;
    this.usage = usage.glValue;
  }

  /**
   * Creates a new data store for the VBO. This call will automatically bind and unbind the VBO.
   * 
   * @param data
   * @param numBytes
   */
  public void setData(Buffer data, long numBytes) {
    gl.glBufferData(GL.GL_ARRAY_BUFFER, numBytes, data, usage);
  }

  /**
   * Redefines some or all of the data store for the buffer object currently bound. The VBO must
   * first be bound before calling this.
   * 
   * @param data - data being copied
   * @param numBytes - size in bytes of data being copied
   * @param offset - byte offset for data being copied
   */
  public void subData(Buffer data, long numBytes, long offset) {
    gl.glBufferSubData(GL.GL_ARRAY_BUFFER, offset, numBytes, data);
  }

  public void bind() {
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, id);
  }

  public void unbind() {
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
  }

  @Override
  public void dispose(GL gl) {
    gl.glDeleteBuffers(1, new int[] { id }, 0);
  }

  @Override
  public boolean isDisposed() {
    return disposed;
  }
}
