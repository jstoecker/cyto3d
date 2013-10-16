package edu.miami.jgloo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

/**
 * A collection of OpenGL texture parameters.
 * 
 * @author justin
 */
public class TexParams {

  private final int                 target;
  private HashMap<Integer, Integer> iParams  = new HashMap<Integer, Integer>();
  private HashMap<Integer, int[]>   ivParams = new HashMap<Integer, int[]>();
  private HashMap<Integer, Float>   fParams  = new HashMap<Integer, Float>();
  private HashMap<Integer, float[]> fvParams = new HashMap<Integer, float[]>();

  /**
   * Creates a new set of texture parameters.
   * 
   * @param target - Specifies the target texture, which must be either GL_TEXTURE_1D, GL_TEXTURE_2D
   *          or GL_TEXTURE_3D.
   */
  public TexParams(int target) {
    this.target = target;
  }

  /**
   * The texture minifying function is used whenever the pixel being textured maps to an area
   * greater than one texture element. (The object is far away).
   * 
   * @param minFilter - <br>
   *          GL_NEAREST : Returns the value of the texture element that is nearest (in Manhattan
   *          distance) to the center of the pixel being textured.<br>
   *          GL_LINEAR : Returns the weighted average of the four texture elements that are closest
   *          to the center of the pixel being textured.<br>
   *          GL_NEAREST_MIPMAP_NEAREST : Chooses the mipmap that most closely matches the size of
   *          the pixel being textured and uses the GL_NEAREST criterion (the texture element
   *          nearest to the center of the pixel) to produce a texture value.<br>
   *          GL_LINEAR_MIPMAP_NEAREST : Chooses the mipmap that most closely matches the size of
   *          the pixel being textured and uses the GL_LINEAR criterion (a weighted average of the
   *          four texture elements that are closest to the center of the pixel) to produce a
   *          texture value. <br>
   *          GL_NEAREST_MIPMAP_LINEAR : Chooses the two mipmaps that most closely match the size of
   *          the pixel being textured and uses the GL_NEAREST criterion (the texture element
   *          nearest to the center of the pixel) to produce a texture value from each mipmap. The
   *          final texture value is a weighted average of those two values. <br>
   *          GL_LINEAR_MIPMAP_LINEAR : Chooses the two mipmaps that most closely match the size of
   *          the pixel being textured and uses the GL_LINEAR criterion (a weighted average of the
   *          four texture elements that are closest to the center of the pixel) to produce a
   *          texture value from each mipmap. The final texture value is a weighted average of those
   *          two values. <br>
   */
  public TexParams minFilter(int value) {
    return set(GL.GL_TEXTURE_MIN_FILTER, value);
  }

  /**
   * The texture magnification function is used when the pixel being textured maps to an area less
   * than or equal to one texture element. (The object is close).
   * 
   * @param magFilter - <br>
   *          GL_NEAREST : Returns the value of the texture element that is nearest (in Manhattan
   *          distance) to the center of the pixel being textured. <br>
   *          GL_LINEAR : Returns the weighted average of the four texture elements that are closest
   *          to the center of the pixel being textured. These can include border texture elements,
   *          depending on the values of GL_TEXTURE_WRAP_S and GL_TEXTURE_WRAP_T, and on the exact
   *          mapping. <br>
   */
  public TexParams magFilter(int value) {
    return set(GL.GL_TEXTURE_MAG_FILTER, value);
  }

  /**
   * Sets the minimum level-of-detail parameter. This floating-point value limits the selection of
   * highest resolution mipmap (lowest mipmap level). The initial value is -1000.
   */
  public TexParams minLOD(float value) {
    return set(GL2.GL_TEXTURE_MIN_LOD, value);
  }

  /**
   * Sets the maximum level-of-detail parameter. This floating-point value limits the selection of
   * the lowest resolution mipmap (highest mipmap level). The initial value is 1000.
   */
  public TexParams maxLOD(float value) {
    return set(GL2.GL_TEXTURE_MAX_LOD, value);
  }

  /**
   * Specifies the index of the lowest defined mipmap level. This is an integer value. The initial
   * value is 0.
   */
  public TexParams baseLevel(int value) {
    return set(GL2.GL_TEXTURE_BASE_LEVEL, value);
  }

  /**
   * Sets the index of the highest defined mipmap level. This is an integer value. The initial value
   * is 1000.
   */
  public TexParams maxLevel(int value) {
    return set(GL2.GL_TEXTURE_MAX_LEVEL, value);
  }

  /**
   * Sets the wrap parameter for texture coordinate s to either GL_CLAMP, GL_CLAMP_TO_BORDER,
   * GL_CLAMP_TO_EDGE, GL_MIRRORED_REPEAT, or GL_REPEAT.
   */
  public TexParams wrapS(int value) {
    return set(GL.GL_TEXTURE_WRAP_S, value);
  }

  /**
   * Sets the wrap parameter for texture coordinate t to either GL_CLAMP, GL_CLAMP_TO_BORDER,
   * GL_CLAMP_TO_EDGE, GL_MIRRORED_REPEAT, or GL_REPEAT.
   */
  public TexParams wrapT(int value) {
    return set(GL.GL_TEXTURE_WRAP_T, value);
  }

  /**
   * Sets the wrap parameter for texture coordinate r to either GL_CLAMP, GL_CLAMP_TO_BORDER,
   * GL_CLAMP_TO_EDGE, GL_MIRRORED_REPEAT, or GL_REPEAT.
   */
  public TexParams wrapR(int value) {
    return set(GL2.GL_TEXTURE_WRAP_R, value);
  }

  /**
   * Sets a border color. params contains four values that comprise the RGBA color of the texture
   * border. Integer color components are interpreted linearly such that the most positive integer
   * maps to 1.0, and the most negative integer maps to -1.0. The values are clamped to the range
   * [0,1] when they are specified. Initially, the border color is (0, 0, 0, 0).
   */
  public TexParams borderColor(float[] params) {
    return set(GL2.GL_TEXTURE_BORDER_COLOR, params);
  }

  /**
   * Specifies the texture residence priority of the currently bound texture. Permissible values are
   * in the range [0,1].
   */
  public TexParams priority(float value) {
    return set(GL2.GL_TEXTURE_PRIORITY, value);
  }

  /**
   * Specifies the texture comparison mode for currently bound depth textures.
   */
  public TexParams compareMode(int value) {
    return set(GL2.GL_TEXTURE_COMPARE_MODE, value);
  }

  /**
   * Specifies the comparison operator used when GL_TEXTURE_COMPARE_MODE is set to
   * GL_COMPARE_R_TO_TEXTURE. Permissible values are:GL_LEQUAL, GL_GEQUAL, GL_LESS,
   * GL_GREATER,GL_EQUAL,GL_NOTEQUAL,GL_ALWAYS,GL_NEVER.
   */
  public TexParams compareFunc(int value) {
    return set(GL2.GL_TEXTURE_COMPARE_FUNC, value);
  }

  /**
   * Specifies a single symbolic constant indicating how depth values should be treated during
   * filtering and texture application. Accepted values are GL_LUMINANCE, GL_INTENSITY, and
   * GL_ALPHA. The initial value is GL_LUMINANCE.
   */
  public TexParams depthTextureMode(int value) {
    return set(GL2.GL_DEPTH_TEXTURE_MODE, value);
  }

  /**
   * Specifies a boolean value that indicates if all levels of a mipmap array should be
   * automatically updated when any modification to the base level mipmap is done. The initial value
   * is GL_FALSE.
   */
  public TexParams generateMipmap(int value) {
    return set(GL2.GL_GENERATE_MIPMAP, value);
  }

  /**
   * Equivalent to glTexParameteri(target, pname, param).
   */
  public TexParams set(int pname, int param) {
    iParams.put(pname, param);
    return this;
  }

  /**
   * Equivalent to glTexParameterf(target, pname, param).
   */
  public TexParams set(int pname, float param) {
    fParams.put(pname, param);
    return this;
  }

  /**
   * Equivalent to glTexParameteriv(target, pname, params).
   */
  public TexParams set(int pname, int[] params) {
    ivParams.put(pname, params);
    return this;
  }

  /**
   * Equivalent to glTexParameterfv(target, pname, params).
   */
  public TexParams set(int pname, float[] params) {
    fvParams.put(pname, params);
    return this;
  }

  /**
   * Applies the set texture parameters.
   */
  public void apply(GL gl) {
    for (Entry<Integer, Integer> entry : iParams.entrySet())
      gl.glTexParameteri(target, entry.getKey(), entry.getValue());
    for (Entry<Integer, Float> entry : fParams.entrySet())
      gl.glTexParameterf(target, entry.getKey(), entry.getValue());
    for (Entry<Integer, int[]> entry : ivParams.entrySet())
      gl.glTexParameteriv(target, entry.getKey(), entry.getValue(), 0);
    for (Entry<Integer, float[]> entry : fvParams.entrySet())
      gl.glTexParameterfv(target, entry.getKey(), entry.getValue(), 0);
  }

  /**
   * Displays all set parameter values.
   */
  public void print() {
    for (Entry<Integer, Integer> entry : iParams.entrySet())
      System.out.println(strName(entry.getKey()) + " = " + strParam(entry.getValue()));
    for (Entry<Integer, Float> entry : fParams.entrySet())
      System.out.println(strName(entry.getKey()) + " = " + entry.getValue());
    for (Entry<Integer, int[]> entry : ivParams.entrySet())
      System.out.println(strName(entry.getKey()) + " = " + Arrays.toString(entry.getValue()));
    for (Entry<Integer, float[]> entry : fvParams.entrySet())
      System.out.println(strName(entry.getKey()) + " = " + Arrays.toString(entry.getValue()));
  }

  private static String strName(int pname) {
    switch (pname) {
    case GL.GL_TEXTURE_MIN_FILTER:
      return "GL_TEXTURE_MIN_FILTER";
    case GL.GL_TEXTURE_MAG_FILTER:
      return "GL_TEXTURE_MAG_FILTER";
    case GL2.GL_TEXTURE_MIN_LOD:
      return "GL_TEXTURE_MIN_LOD";
    case GL2.GL_TEXTURE_MAX_LOD:
      return "GL_TEXTURE_MAX_LOD";
    case GL2.GL_TEXTURE_BASE_LEVEL:
      return "GL_TEXTURE_BASE_LEVEL";
    case GL2.GL_TEXTURE_MAX_LEVEL:
      return "GL_TEXTURE_BASE_LEVEL";
    case GL.GL_TEXTURE_WRAP_S:
      return "GL_TEXTURE_WRAP_S";
    case GL.GL_TEXTURE_WRAP_T:
      return "GL_TEXTURE_WRAP_T";
    case GL2.GL_TEXTURE_WRAP_R:
      return "GL_TEXTURE_WRAP_R";
    case GL2.GL_TEXTURE_PRIORITY:
      return "GL_TEXTURE_PRIORITY";
    case GL2.GL_TEXTURE_COMPARE_MODE:
      return "GL_TEXTURE_COMPARE_MODE";
    case GL2.GL_TEXTURE_COMPARE_FUNC:
      return "GL_TEXTURE_COMPARE_FUNC";
    case GL2.GL_DEPTH_TEXTURE_MODE:
      return "GL_DEPTH_TEXTURE_MODE";
    case GL2.GL_GENERATE_MIPMAP:
      return "GL_GENERATE_MIPMAP";
    case GL.GL_TEXTURE_MAX_ANISOTROPY_EXT:
      return "GL_TEXTURE_MAX_ANISOTROPY_EXT";
    default:
      return String.format("%s (%d)", "Unknown PNAME", pname);
    }
  }

  private static String strParam(int param) {
    switch (param) {
    case GL.GL_NEAREST:
      return "GL_NEAREST";
    case GL.GL_LINEAR:
      return "GL_LINEAR";
    case GL.GL_NEAREST_MIPMAP_NEAREST:
      return "GL_NEAREST_MIPMAP_NEAREST";
    case GL.GL_LINEAR_MIPMAP_NEAREST:
      return "GL_LINEAR_MIPMAP_NEAREST";
    case GL.GL_NEAREST_MIPMAP_LINEAR:
      return "GL_NEAREST_MIPMAP_LINEAR";
    case GL.GL_LINEAR_MIPMAP_LINEAR:
      return "GL_LINEAR_MIPMAP_LINEAR";
    case GL2.GL_CLAMP:
      return "GL_CLAMP";
    case GL2.GL_CLAMP_TO_BORDER:
      return "GL_CLAMP_TO_BORDER";
    case GL2.GL_CLAMP_TO_EDGE:
      return "GL_CLAMP_TO_EDGE";
    case GL2.GL_MIRRORED_REPEAT:
      return "GL_MIRRORED_REPEAT";
    case GL2.GL_REPEAT:
      return "GL_REPEAT";
    case GL2.GL_COMPARE_R_TO_TEXTURE:
      return "GL_COMPARE_R_TO_TEXTURE";
    case GL2.GL_NONE:
      return "False / None / 0";
    case GL2.GL_LEQUAL:
      return "GL_LEQUAL";
    case GL2.GL_GEQUAL:
      return "GL_GEQUAL";
    case GL2.GL_LESS:
      return "GL_LESS";
    case GL2.GL_GREATER:
      return "GL_GREATER";
    case GL2.GL_EQUAL:
      return "GL_EQUAL";
    case GL2.GL_NOTEQUAL:
      return "GL_NOTEQUAL";
    case GL2.GL_ALWAYS:
      return "GL_ALWAYS";
    case GL2.GL_NEVER:
      return "GL_NEVER";
    case GL2.GL_LUMINANCE:
      return "GL_LUMINANCE";
    case GL2.GL_INTENSITY:
      return "GL_INTENSITY";
    case GL2.GL_ALPHA:
      return "GL_ALPHA";
    case GL.GL_TRUE:
      return "True / 1";
    default:
      return Integer.toString(param);
    }
  }

  @Override
  public TexParams clone() {
    TexParams copy = new TexParams(this.target);
    copy.iParams.putAll(iParams);
    copy.fParams.putAll(fParams);
    copy.ivParams.putAll(ivParams);
    copy.fvParams.putAll(fvParams);
    return copy;
  }
}
