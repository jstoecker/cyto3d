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
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2GL3;

import com.jogamp.common.nio.Buffers;

/**
 * A texture object that can be applied to geometry using texture coordinates
 * 
 * @author Justin Stoecker
 */
public class Texture2D implements GLDisposable {
  
  public enum Filtering {
    NEAREST, BILINEAR, TRILINEAR, ANISOTROPIC
  }

  public enum Wrap {
    REPEAT, MIRRORED_REPEAT, CLAMP_TO_EDGE, CLAMP_TO_BORDER
  }

  private boolean disposed = false;
  private int     id;
  private int     width;
  private int     height;
  private int     level;
  private int     internalFormat;
  private int     border;
  private int     format;
  private int     type;

  public int getID() {
    return id;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public int getLevel() {
    return level;
  }

  public int getInternalFormat() {
    return internalFormat;
  }

  public int getBorder() {
    return border;
  }

  public int getFormat() {
    return format;
  }

  public int getType() {
    return type;
  }

  public Texture2D(int id, int width, int height, int level, int internalFormat, int border,
      int format, int type) {
    this.id = id;
    this.width = width;
    this.height = height;
    this.level = level;
    this.internalFormat = internalFormat;
    this.border = border;
    this.format = format;
    this.type = type;
  }

  public Texture2D(GL2GL3 gl, int width, int height, int level, int internalFormat, int border,
      int format, int type) {
    IntBuffer temp = Buffers.newDirectIntBuffer(1);
    gl.glGenTextures(1, temp);
    this.id = temp.get();
    this.width = width;
    this.height = height;
    this.level = level;
    this.internalFormat = internalFormat;
    this.border = border;
    this.format = format;
    this.type = type;

    buffer(gl, null);
  }

  public Texture2D(GL2GL3 gl, int width, int height, int level, int internalFormat, int border,
      int format, int type, TexParams params) {
    IntBuffer temp = Buffers.newDirectIntBuffer(1);
    gl.glGenTextures(1, temp);
    this.id = temp.get();
    this.width = width;
    this.height = height;
    this.level = level;
    this.internalFormat = internalFormat;
    this.border = border;
    this.format = format;
    this.type = type;

    bind(gl);
    params.apply(gl);
    buffer(gl, null);
  }

  public void buffer(GL gl, Buffer pixelData) {
    gl.glBindTexture(GL2.GL_TEXTURE_2D, id);
    gl.glTexImage2D(GL.GL_TEXTURE_2D, level, internalFormat, width, height, border, format, type,
        pixelData);
    gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
  }

  public void resize(GL gl, int width, int height) {
    this.width = width;
    this.height = height;
    buffer(gl, null);
  }

  public void bind(GL gl) {
    gl.glBindTexture(GL.GL_TEXTURE_2D, id);
  }

  public static void unbind(GL gl) {
    gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
  }

  @Override
  public void dispose(GL gl) {
    gl.glDeleteTextures(1, new int[] { id }, 0);
    disposed = true;
  }
  
  public static Texture2D create(GL gl, Filtering filtering, Wrap wrap, int width, int height,
      int internalFormat, int format, int type, Buffer pixelData) {

    // Allocate a texture object and bind it.
    IntBuffer buf = IntBuffer.allocate(1);
    gl.glGenTextures(1, buf);
    Texture2D texture = new Texture2D(buf.get(), width, height, 0, internalFormat, 0, format, type);
    texture.bind(gl);

    // Choose the texture filtering mode (there are other combinations, but
    // these are the most common). The MIN_FILTER is used when the texture is
    // far away; the MAG_FILTER is used when the texture is up close.
    boolean mipmaps = false;
    switch (filtering) {
    case NEAREST:
      // Nearest-neighbor interpolation.
      gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
      gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
      break;
    case BILINEAR:
      // Linear interpolation along X and Y.
      gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
      gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
      break;
    case TRILINEAR:
      // Linear interpolation along X, Y, and mipmap levels.
      gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR);
      gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
      mipmaps = true;
      break;
    case ANISOTROPIC:
      // Use this when it is possible the texture is viewed at extreme angles.
      FloatBuffer maxAnisotropy = FloatBuffer.allocate(1);
      gl.glGetFloatv(GL.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT, maxAnisotropy);
      gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAX_ANISOTROPY_EXT, maxAnisotropy.get());
      gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR);
      gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
      mipmaps = true;
      break;
    }

    // Set texture wrapping mode. This determines colors when the texture
    // coordinates are outside [0,1].
    switch (wrap) {
    case REPEAT:
      gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
      gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
      break;
    case MIRRORED_REPEAT:
      gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_MIRRORED_REPEAT);
      gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_MIRRORED_REPEAT);
      break;
    case CLAMP_TO_EDGE:
      gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
      gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);
      break;
    case CLAMP_TO_BORDER:
      gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL2GL3.GL_CLAMP_TO_BORDER);
      gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL2GL3.GL_CLAMP_TO_BORDER);
      break;
    }

    // Upload texel data.
    gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, type, pixelData);
    if (mipmaps)
      gl.glGenerateMipmap(GL.GL_TEXTURE_2D);

    return texture;
  }

  @Override
  public boolean isDisposed() {
    return disposed;
  }
}
