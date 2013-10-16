package edu.miami.jgloo.content;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import edu.miami.jgloo.GLDisposable;
import edu.miami.jgloo.TexParams;
import edu.miami.jgloo.mesh.Mesh;
import edu.miami.jgloo.mesh.Mesh.RenderMode;
import edu.miami.jgloo.util.GLInfo;

/**
 * OpenGL resource content manager.
 * 
 * @author justin
 */
public class ContentManager {

  final String                 modelDir;
  final String                 textureDir;
  final String                 materialDir;
  final String                 shaderDir;
  HashMap<String, CMModel>     sharedModels         = new HashMap<String, CMModel>();
  HashMap<String, CMTexture2D> sharedTextures       = new HashMap<String, CMTexture2D>();
  HashMap<String, CMShader>    sharedShaders        = new HashMap<String, CMShader>();
  HashMap<String, CMMatLib>    sharedMatLibs        = new HashMap<String, CMMatLib>();
  Stack<CMContent>             uninitializedContent = new Stack<CMContent>();
  Stack<GLDisposable>          trash                = new Stack<GLDisposable>();
  RenderMode                   meshRenderMode       = RenderMode.VERTEX_ARRAYS;
  final boolean                loadFromFile;
  boolean                      initialized          = false;
  CMTexture2D                  blankTexture;
  TexParams                    defaultTexParams;
  
  public ContentManager(String contentDir, boolean loadFromFile) {
    modelDir = new File(contentDir, "models").getAbsolutePath();
    textureDir = new File(contentDir, "textures").getAbsolutePath();
    materialDir = new File(contentDir, "materials").getAbsolutePath();
    shaderDir = new File(contentDir, "shaders").getAbsolutePath();
    this.loadFromFile = loadFromFile;
  }

  public ContentManager(String modelDir, String textureDir, String materialDir, String shaderDir,
      boolean loadFromFile) {
    this.modelDir = modelDir;
    this.textureDir = textureDir;
    this.materialDir = materialDir;
    this.shaderDir = shaderDir;
    this.loadFromFile = loadFromFile;
  }
  
  public void setDefaultTexParams(TexParams defaultTexParams) {
    this.defaultTexParams = defaultTexParams;
  }

  public void initialize(GL2 gl) {
    if (initialized) return;

    GLInfo info = new GLInfo(gl);

    if (info.extSupported("GL_ARB_vertex_buffer_object")) meshRenderMode = Mesh.RenderMode.VBO;

    // default to highest quality texturing
    if (defaultTexParams == null) {
      defaultTexParams = new TexParams(GL.GL_TEXTURE_2D);
      defaultTexParams.magFilter(GL.GL_LINEAR);
      defaultTexParams.minFilter(GL.GL_LINEAR_MIPMAP_LINEAR);
      defaultTexParams.generateMipmap(GL.GL_TRUE);
      if (info.extSupported("GL_EXT_texture_filter_anisotropic")) {
        float[] buf = new float[1];
        gl.glGetFloatv(GL.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT, buf, 0);
        defaultTexParams.set(GL.GL_TEXTURE_MAX_ANISOTROPY_EXT, buf[0]);
      }
    }

    createBlankTexture(gl);
    initialized = true;
  }

  private void createBlankTexture(GL2 gl) {
    BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
    img.setRGB(0, 0, Color.white.getRGB());
    blankTexture = new CMTexture2D("cm_generated_white_tex", img, this);
    blankTexture.initialize(gl);
  }

  public synchronized void update(GL2 gl) {
    while (!uninitializedContent.isEmpty())
      uninitializedContent.pop().initialize(gl);
    while (!trash.isEmpty())
      trash.pop().dispose(gl);
  }

  public void disposeAll(GL2 gl) {
    if (!initialized)
      return;

    blankTexture.release();
    for (CMContent content : sharedModels.values())
      content.release();
    for (CMContent content : sharedTextures.values())
      content.release();
    for (CMContent content : sharedMatLibs.values())
      content.release();
    for (CMContent content : sharedShaders.values())
      content.release();

    while (!trash.isEmpty())
      trash.pop().dispose(gl);

    uninitializedContent.clear();
    sharedModels.clear();
    sharedTextures.clear();
    sharedMatLibs.clear();
    sharedShaders.clear();

    initialized = false;
  }

  public void addTrash(GLDisposable disposable) {
    trash.push(disposable);
  }

  private CMModel getModel(String name, boolean uniqueInstance, boolean delayedInit) {
    CMModel model;
    if (uniqueInstance || (model = sharedModels.get(name)) == null) {
      model = new CMModel(name, this);
      if (delayedInit)
        new ModelLoader(model, this).start();
      else
        model.readMeshData();
      if (!uniqueInstance) sharedModels.put(name, model);
    }
    return model;
  }

  private CMTexture2D getTexture(String name, TexParams params, boolean uniqueInstance,
      boolean delayedInit) {
    if (name == null) return blankTexture;

    CMTexture2D texture;
    if (uniqueInstance || (texture = sharedTextures.get(name)) == null) {
      try {
        texture = new CMTexture2D(name, params, this);
      } catch (IOException e) {
        return null;
      }
      if (delayedInit) uninitializedContent.add(texture);
      if (!uniqueInstance) sharedTextures.put(name, texture);
    }
    return texture;
  }

  private CMShader getShader(String name, boolean uniqueInstance, boolean delayedInit) {
    CMShader shader;
    if (uniqueInstance || (shader = sharedShaders.get(name)) == null) {
      try {
        shader = new CMShader(name, this);
      } catch (FileNotFoundException e) {
        return null;
      }
      if (delayedInit) uninitializedContent.add(shader);
      if (!uniqueInstance) sharedShaders.put(name, shader);
    }
    return shader;
  }

  private CMMatLib getMatLib(String name, boolean uniqueInstance, boolean delayedInit) {
    CMMatLib matlib;
    if (uniqueInstance || (matlib = sharedMatLibs.get(name)) == null) {
      try {
        matlib = new CMMatLib(name, this);
      } catch (IOException e) {
        return null;
      }
      if (delayedInit) uninitializedContent.add(matlib);
      if (!uniqueInstance) sharedMatLibs.put(name, matlib);
    }
    return matlib;
  }

  /** Retrieves a model; it will be ready after the next call to the manager's update(). */
  public synchronized CMModel retrieveModel(String name, boolean uniqueInstance) {
    return getModel(name, uniqueInstance, true);
  }

  /** Retrieves a model and ensures it is immediately initialized and ready for use. */
  public synchronized CMModel retrieveModel(String name, boolean uniqueInstance, GL2 gl) {
    CMModel model = getModel(name, uniqueInstance, false);
    model.initialize(gl);
    return model;
  }

  /** Retrieves a texture; it will be ready after the next call to the manager's update(). */
  public synchronized CMTexture2D retrieveTexture(String name, TexParams params,
      boolean uniqueInstance) {
    return getTexture(name, params, uniqueInstance, true);
  }

  /** Retrieves a texture and ensures it is immediately initialized and ready for use. */
  public synchronized CMTexture2D retrieveTexture(String name, TexParams params,
      boolean uniqueInstance, GL2 gl) {
    CMTexture2D texture = getTexture(name, params, uniqueInstance, false);
    texture.initialize(gl);
    return texture;
  }

  /** Retrieves a texture; it will be ready after the next call to the manager's update(). */
  public synchronized CMTexture2D retrieveTexture(String name, boolean uniqueInstance) {
    return getTexture(name, defaultTexParams, uniqueInstance, true);
  }

  /** Retrieves a texture and ensures it is immediately initialized and ready for use. */
  public synchronized CMTexture2D retrieveTexture(String name, boolean uniqueInstance, GL2 gl) {
    CMTexture2D texture = getTexture(name, defaultTexParams, uniqueInstance, false);
    texture.initialize(gl);
    return texture;
  }

  /** Retrieves a shader; it will be ready after the next call to the manager's update(). */
  public synchronized CMShader retrieveShader(String name, boolean uniqueInstance) {
    return getShader(name, uniqueInstance, true);
  }

  /** Retrieves a shader and ensures it is immediately initialized and ready for use. */
  public synchronized CMShader retrieveShader(String name, boolean uniqueInstance, GL2 gl) {
    CMShader shader = getShader(name, uniqueInstance, false);
    shader.initialize(gl);
    return shader;
  }

  /** Retrieves a mat library; it will be ready after the next call to the manager's update(). */
  public synchronized CMMatLib retrieveMatLib(String name, boolean uniqueInstance) {
    return getMatLib(name, uniqueInstance, true);
  }

  /** Retrieves a mat library and ensures it is immediately initialized and ready for use. */
  public synchronized CMMatLib retrieveMatLib(String name, boolean uniqueInstance, GL2 gl) {
    CMMatLib lib = getMatLib(name, uniqueInstance, false);
    lib.initialize(gl);
    return lib;
  }
}
