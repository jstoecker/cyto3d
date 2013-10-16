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

import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2GL3;

import com.jogamp.common.nio.Buffers;

/**
 * Buffer for direct offscreen rendering. Should be attached to an FBO.
 * 
 * @author justin
 */
public class RenderBuffer implements GLDisposable {

  private boolean disposed = false;
  private int     id;
  private int     samples;
  private int     format;
  private int     width;
  private int     height;

  public int getID() {
    return id;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public int getSamples() {
    return samples;
  }

  public int getFormat() {
    return format;
  }

  private RenderBuffer(GL2GL3 gl, int format, int width, int height, int samples) {
    IntBuffer temp = Buffers.newDirectIntBuffer(1);
    gl.glGenRenderbuffers(1, temp);
    this.id = temp.get();

    this.format = format;
    this.width = width;
    this.height = height;
    this.samples = samples;

    resize(gl, width, height);
  }

  public static RenderBuffer createDepthBuffer(GL2GL3 gl, int width, int height) {
    return new RenderBuffer(gl, GL2.GL_DEPTH_COMPONENT, width, height, -1);
  }

  public static RenderBuffer createDepthBuffer(GL2GL3 gl, int width, int height, int samples) {
    return new RenderBuffer(gl, GL2.GL_DEPTH_COMPONENT, width, height, samples);
  }

  public static RenderBuffer createColorBuffer(GL2GL3 gl, int width, int height, int format) {
    return new RenderBuffer(gl, format, width, height, -1);
  }

  public static RenderBuffer createColorBuffer(GL2GL3 gl, int width, int height, int format,
      int samples) {
    return new RenderBuffer(gl, format, width, height, samples);
  }

  public void resize(GL2GL3 gl, int width, int height) {
    gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, id);
    if (samples == -1) {
      gl.glRenderbufferStorage(GL.GL_RENDERBUFFER, format, width, height);
    } else {
      gl.glRenderbufferStorageMultisample(GL.GL_RENDERBUFFER, samples, format, width, height);
    }
    gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, 0);
  }

  public void dispose(GL gl) {
    if (!disposed) {
      gl.glDeleteRenderbuffers(1, new int[] { id }, 0);
      disposed = true;
    }
  }

  public boolean isDisposed() {
    return disposed;
  }
}
