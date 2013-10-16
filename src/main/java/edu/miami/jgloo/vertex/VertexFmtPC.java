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
package edu.miami.jgloo.vertex;

import javax.media.opengl.GL2;
import javax.media.opengl.fixedfunc.GLPointerFunc;

/**
 * Vertex format that contains position and color data.
 * 
 * @author justin
 */
public class VertexFmtPC extends VertexFmtP {

  protected int colorCount;
  protected int colorType;
  protected int colorPointer;

  /**
   * Initializes a vertex format that stores position and color data.
   * 
   * @param vertexSize - Specifies the number of coordinates per vertex. Must be 2, 3, or 4.
   * @param vertexType - Specifies the data type of each vertex coordinate in the array. Symbolic
   *          constants GL_SHORT, GL_INT, GL_FLOAT, or GL_DOUBLE are accepted.
   * @param colorSize - Specifies the number of components per color. Must be 3 or 4.
   * @param colorType - Specifies the data type of each color component in the array. Symbolic
   *          constants GL_BYTE, GL_UNSIGNED_BYTE, GL_SHORT, GL_UNSIGNED_SHORT, GL_INT,
   *          GL_UNSIGNED_INT, GL_FLOAT, and GL_DOUBLE are accepted.
   */
  public VertexFmtPC(int vertexSize, int vertexType, int colorSize, int colorType) {
    super(vertexSize, vertexType);
    this.colorCount = colorSize;
    this.colorType = colorType;
    colorPointer = stride;
    stride += colorSize * sizeOfType(colorType);
  }

  @Override
  public void setState(GL2 gl) {
    super.setState(gl);
    gl.glEnableClientState(GLPointerFunc.GL_COLOR_ARRAY);
    gl.glColorPointer(colorCount, colorType, stride, colorPointer);
  }

  @Override
  public void unsetState(GL2 gl) {
    super.unsetState(gl);
    gl.glDisableClientState(GLPointerFunc.GL_COLOR_ARRAY);
  }
}
