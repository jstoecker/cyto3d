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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2GL3;

import com.jogamp.common.nio.Buffers;

/**
 * An off-screen buffer that can be used for render-to-texture purposes. To use an FBO, it must be
 * bound as the current framebuffer.
 * 
 * @author Justin Stoecker
 */
public class FrameBufferObject implements GLDisposable {

  private boolean                        disposed       = false;
  private int                            id;
  private HashMap<Integer, Texture2D>    texAttachments = new HashMap<Integer, Texture2D>();
  private HashMap<Integer, RenderBuffer> rboAttachments = new HashMap<Integer, RenderBuffer>();
  private List<GLDisposable>             disposables    = new ArrayList<GLDisposable>();
  private int                            width;
  private int                            height;

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public int getID() {
    return id;
  }

  public Texture2D getColorTexture(int i) {
    return texAttachments.get(i);
  }

  /** Returns depth texture if one has been attached; null otherwise */
  public Texture2D getDepthTexture() {
    return texAttachments.get(GL.GL_DEPTH_ATTACHMENT);
  }

  public FrameBufferObject(GL2GL3 gl) {
    IntBuffer temp = Buffers.newDirectIntBuffer(1);
    gl.glGenFramebuffers(1, temp);
    this.id = temp.get();
  }

  public void resize(GL2GL3 gl, int width, int height) {
    this.width = width;
    this.height = height;

    for (Texture2D tex : texAttachments.values())
      tex.resize(gl, width, height);
    for (RenderBuffer rbo : rboAttachments.values())
      rbo.resize(gl, width, height);
  }

  private boolean checkStatus(GL2GL3 gl) {
    int status = gl.glCheckFramebufferStatus(GL.GL_FRAMEBUFFER);
    gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
    if (status != GL.GL_FRAMEBUFFER_COMPLETE) {
      System.err.println("ERROR creating FBO - releasing resources");
      dispose(gl);
      return false;
    }
    return true;
  }

  private static Texture2D createBasicTexture(GL2GL3 gl, int w, int h, int internalFormat) {
    Texture2D colorTex = new Texture2D(gl, w, h, 0, internalFormat, 0, GL.GL_RGBA,
        GL.GL_UNSIGNED_BYTE);
    colorTex.bind(gl);
    new TexParams(GL.GL_TEXTURE_2D).magFilter(GL.GL_LINEAR).minFilter(GL.GL_LINEAR)
        .wrapS(GL.GL_CLAMP_TO_EDGE).wrapT(GL.GL_CLAMP_TO_EDGE).apply(gl);

    colorTex.buffer(gl, null);
    return colorTex;
  }

  /** Creates an FBO that has color and depth renderbuffer attachments */
  public static FrameBufferObject create(GL2GL3 gl, int w, int h, int internalFormat) {
    Texture2D colorTex = createBasicTexture(gl, w, h, internalFormat);
    RenderBuffer depthBuffer = RenderBuffer.createDepthBuffer(gl, w, h);

    FrameBufferObject fbo = new FrameBufferObject(gl);
    fbo.bind(gl);
    fbo.attachColorTarget(gl, colorTex, 0, 0, true);
    fbo.attachDepthTarget(gl, depthBuffer, true);

    return fbo.checkStatus(gl) ? fbo : null;
  }

  /** Creates an FBO with a color texture attachment and no depth attachment */
  public static FrameBufferObject createNoDepth(GL2GL3 gl, int w, int h, int internalFormat) {
    Texture2D colorTex = createBasicTexture(gl, w, h, internalFormat);

    FrameBufferObject fbo = new FrameBufferObject(gl);
    fbo.bind(gl);
    fbo.attachColorTarget(gl, colorTex, 0, 0, true);

    return fbo.checkStatus(gl) ? fbo : null;
  }

  /**
   * Creates a Framebuffer Object that can be used for offscreen rendering to a texture of a
   * specified with and height. This FBO is setup for multisampling.
   */
  public static FrameBufferObject create(GL2GL3 gl, int w, int h, int internalFormat, int samples) {
    RenderBuffer depthBuffer = RenderBuffer.createDepthBuffer(gl.getGL2GL3(), w, h, samples);
    RenderBuffer colorBuffer = RenderBuffer.createColorBuffer(gl.getGL2GL3(), w, h, internalFormat,
        samples);

    FrameBufferObject fbo = new FrameBufferObject(gl);
    fbo.bind(gl);
    fbo.attachDepthTarget(gl, depthBuffer, true);
    fbo.attachColorTarget(gl, colorBuffer, true, 0);

    return fbo.checkStatus(gl) ? fbo : null;
  }

  /**
   * Attaches a texture that will be the target for colors while this FBO is bound.
   * 
   * @param texture - the texture object
   * @param level - texture level if using mip-maps
   * @param attachPoint - the color attachment point index
   * @param autoDispose - if the texture should be released when the FBO is disposed
   */
  public void attachColorTarget(GL gl, Texture2D texture, int level, int attachPoint,
      boolean autoDispose) {
    width = texture.getWidth();
    height = texture.getHeight();

    gl.glFramebufferTexture2D(GL.GL_FRAMEBUFFER, GL.GL_COLOR_ATTACHMENT0 + attachPoint,
        GL.GL_TEXTURE_2D, texture.getID(), level);

    texAttachments.put(attachPoint, texture);
    if (autoDispose) disposables.add(texture);
  }

  /**
   * Attaches a renderbuffer that will be the target for colors while this FBO is bound.
   */
  public void attachColorTarget(GL gl, RenderBuffer rbo, boolean autoDispose, int attachPoint) {
    width = rbo.getWidth();
    height = rbo.getHeight();

    gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER, GL.GL_COLOR_ATTACHMENT0 + attachPoint,
        GL.GL_RENDERBUFFER, rbo.getID());

    rboAttachments.put(attachPoint, rbo);
    if (autoDispose) disposables.add(rbo);
  }

  /**
   * Attaches a texture that will be used as a depth buffer while this FBO is bound.
   */
  public void attachDepthTarget(GL gl, Texture2D texture, boolean autoDispose) {
    gl.glFramebufferTexture2D(GL.GL_FRAMEBUFFER, GL.GL_DEPTH_ATTACHMENT, GL.GL_TEXTURE_2D,
        texture.getID(), 0);

    texAttachments.put(GL.GL_DEPTH_ATTACHMENT, texture);
    if (autoDispose) disposables.add(texture);
  }

  /**
   * Attaches a renderbuffer that will be used as a depth buffer while this FBO is bound.
   */
  public void attachDepthTarget(GL gl, RenderBuffer rbo, boolean autoDispose) {
    gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER, GL.GL_DEPTH_ATTACHMENT, GL.GL_RENDERBUFFER,
        rbo.getID());

    rboAttachments.put(GL.GL_DEPTH_ATTACHMENT, rbo);
    if (autoDispose) disposables.add(rbo);
  }

  /**
   * Sets viewport to match color texture dimensions.
   * 
   * @param gl
   */
  public void setViewport(GL gl) {
    gl.glViewport(0, 0, width, height);
  }

  /** Binds the current FBO for off-screen rendering */
  public void bind(GL gl) {
    gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, id);
  }

  /**
   * Unbinds the current FBO and sets rendering to go to the selected draw buffer
   */
  public void unbind(GL gl) {
    gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
  }

  /** Clears the color and depth buffers for the FBO */
  public void clear(GL gl) {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
  }

  @Override
  public void dispose(GL gl) {
    for (GLDisposable d : disposables)
      d.dispose(gl);

    gl.glDeleteFramebuffers(1, new int[] { id }, 0);
    disposed = true;
  }

  @Override
  public boolean isDisposed() {
    return disposed;
  }

  @Override
  public void finalize() {
    if (!disposed) System.err.printf("WARNING: FBO %d was not disposed!\n", id);
  }
}
