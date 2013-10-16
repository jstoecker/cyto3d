package edu.miami.jgloo.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;

import com.jogamp.common.nio.Buffers;

import edu.miami.jgloo.TexParams;
import edu.miami.jgloo.Texture2D;

public class ImageUtil {

  /** Reads pixel data from a buffered image into a byte buffer */
  public static ByteBuffer readPixels(BufferedImage img, boolean alpha) throws InterruptedException {

    int w = img.getWidth();
    int h = img.getHeight();

    // Create a buffer big enough to store pixel data separated into bytes.
    // There are 4 channels if alpha is present (RGBA); 3 otherwise (RGB)
    int channels = alpha ? 4 : 3;
    ByteBuffer data = Buffers.newDirectByteBuffer(w * h * channels);

    // Put pixel data into the buffer starting from bottom row going left
    // to right. Each pixel must be unpacked from int form into each channel
    for (int y = 0; y < h; y++) {
      for (int x = 0; x < w; x++) {
        int pixel = img.getRGB(x, h - y - 1);
        data.put((byte) ((pixel >> 16) & 0xFF));
        data.put((byte) ((pixel >> 8) & 0xFF));
        data.put((byte) ((pixel) & 0xFF));
        if (alpha) data.put((byte) ((pixel >> 24) & 0xFF));
      }
    }

    data.rewind();
    return data;
  }

  public static Texture2D loadTex(GL gl, BufferedImage img) {
    return loadTex(gl, img, null);
  }

  public static Texture2D loadTex(GL gl, BufferedImage img, TexParams params) {
    if (params == null) params = new TexParams(GL.GL_TEXTURE_2D).minFilter(GL.GL_LINEAR);

    int w = img.getWidth();
    int h = img.getHeight();
    boolean alpha = img.getColorModel().hasAlpha();
    int format = alpha ? GL.GL_RGBA : GL.GL_RGB;
    int type = GL.GL_UNSIGNED_BYTE;

    ByteBuffer pixelData;
    try {
      pixelData = ImageUtil.readPixels(img, alpha);
    } catch (InterruptedException e) {
      System.out.println("Error buffering pixel data: " + e.getMessage());
      return null;
    }

    Texture2D texture = new Texture2D(gl.getGL2GL3(), w, h, 0, format, 0, format, type);

    texture.bind(gl);
    params.apply(gl);
    texture.buffer(gl, pixelData);

    return texture;
  }

  public static Texture2D loadTex(GL gl, String name, ClassLoader loader) {
    BufferedImage img;
    try {
      img = ImageIO.read(loader.getResourceAsStream(name));
    } catch (IOException e) {
      System.out.println("Error loading image: " + e.getMessage());
      return null;
    }
    return loadTex(gl, img);
  }
}
