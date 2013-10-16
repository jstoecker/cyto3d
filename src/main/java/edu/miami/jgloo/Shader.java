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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GL3;

import com.jogamp.common.nio.Buffers;

/**
 * Creates a compiled GLSL shader from a source code. Supports both OpenGL 2.0+ shaders and the ARB
 * extension versions.
 * 
 * @author Justin Stoecker
 */
public class Shader implements GLDisposable {
  private boolean  disposed = false;
  private int      id;
  private String   fileName;
  private String[] srcLines;

  public int getID() {
    return id;
  }

  public String getFileName() {
    return fileName;
  }

  /**
   * Returns an array of lines containing the source code of the shader
   * 
   * @return
   */
  public String[] getSourceLines() {
    return srcLines;
  }

  private Shader(int id, String file, String[] srcLines) {
    this.id = id;
    this.fileName = file;
    this.srcLines = srcLines;
  }

  /**
   * Creates a compiled GLSL fragment shader from source. Requires GL version 2.0 or greater.
   */
  public static Shader createFragmentShader(GL2 gl, String file, InputStream is) {
    return createShaderObject(gl, GL2ES2.GL_FRAGMENT_SHADER, file, is);
  }

  /**
   * Creates a compiled GLSL vertex shader from source. Requires GL version 2.0 or greater.
   */
  public static Shader createVertexShader(GL2 gl, String file, InputStream is) {
    return createShaderObject(gl, GL2ES2.GL_VERTEX_SHADER, file, is);
  }

  private static Shader createShaderObject(GL2 gl, int type, String file, InputStream is) {
    int id = gl.glCreateShader(type);

    String[] src = copySourceToArray(file, is);
    if (src == null) {
      gl.glDeleteShader(id);
      return null;
    }
    IntBuffer lineLengths = getLineLengths(src);
    if (lineLengths == null) {
      gl.glDeleteShader(id);
      return null;
    }

    gl.glShaderSource(id, src.length, src, lineLengths);
    gl.glCompileShader(id);

    if (!checkCompileStatus(gl, id, file)) {
      gl.glDeleteShader(id);
      return null;
    }

    return new Shader(id, file, src);
  }

  private static String[] copySourceToArray(String fileName, InputStream is) {

    ArrayList<String> src = new ArrayList<String>();
    BufferedReader in = new BufferedReader(new InputStreamReader(is));

    try {
      String currentLine;
      while ((currentLine = in.readLine()) != null)
        src.add(currentLine + "\n");
      in.close();
    } catch (IOException e) {
      System.out.printf("Error creating shader (%s): could not copy source.\n%s\n", fileName,
          e.getMessage());
    }

    return src.toArray(new String[src.size()]);
  }

  private static IntBuffer getLineLengths(String[] src) {
    IntBuffer buf = Buffers.newDirectIntBuffer(src.length);
    for (int i = 0; i < src.length; i++)
      buf.put(src[i].length());
    buf.rewind();
    return buf;
  }

  private static boolean checkCompileStatus(GL2 gl, int id, String file) {
    IntBuffer status = Buffers.newDirectIntBuffer(1);
    gl.glGetShaderiv(id, GL2ES2.GL_COMPILE_STATUS, status);

    if (status.get() == GL.GL_FALSE) {
      IntBuffer iLogLengthBuf = Buffers.newDirectIntBuffer(1);
      gl.glGetShaderiv(id, GL2ES2.GL_INFO_LOG_LENGTH, iLogLengthBuf);
      int infoLogLength = iLogLengthBuf.get();
      ByteBuffer infoLog = Buffers.newDirectByteBuffer(infoLogLength);
      gl.glGetShaderInfoLog(id, infoLogLength, null, infoLog);
      CharBuffer info = Charset.forName("US-ASCII").decode(infoLog);

      System.err.printf("Error compiling shader (%s):\n%s\n", file, info.toString());
      return false;
    }
    return true;
  }

  @Override
  public void dispose(GL gl) {
    if (gl instanceof GL2)
      gl.getGL2().glDeleteShader(id);
    else if (gl instanceof GL3) gl.getGL3().glDeleteShader(id);
    disposed = true;
  }

  @Override
  public boolean isDisposed() {
    return disposed;
  }
}
