package edu.miami.jgloo.content;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import edu.miami.jgloo.TexParams;
import edu.miami.jgloo.Texture2D;
import edu.miami.jgloo.util.ImageUtil;

public class CMTexture2D extends CMContent {

  Texture2D     texture;
  BufferedImage image;
  TexParams     params;

  protected CMTexture2D(String name, BufferedImage image, ContentManager manager) {
    super(name, manager);
    this.image = image;
    params = new TexParams(GL.GL_TEXTURE_2D);
  }

  protected CMTexture2D(String name, TexParams params, ContentManager manager) throws IOException {
    super(name, manager);
    this.params = params;
    image = ImageIO.read(getContentInputStream(manager.textureDir));
  }

  protected void initialize(GL2 gl) {
    if (initialized) return;
    texture = ImageUtil.loadTex(gl, image, params);
    image = null;
    initialized = true;
  }

  public Texture2D get() {
    return texture;
  }

  public void dispose(GL gl) {
    if (disposed) return;
    if (texture != null) texture.dispose(gl);
    disposed = true;
  }
}
