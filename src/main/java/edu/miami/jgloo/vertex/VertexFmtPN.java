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
public class VertexFmtPN extends VertexFmtP {

  protected int normalType;
  protected int normalPointer;

  /**
   * Initializes a vertex format that stores position and color data.
   * 
   * @param vertexSize - Specifies the number of coordinates per vertex. Must be 2, 3, or 4.
   * @param vertexType - Specifies the data type of each vertex coordinate in the array. Symbolic
   *          constants GL_SHORT, GL_INT, GL_FLOAT, or GL_DOUBLE are accepted.
   * @param normalType - Specifies the data type of each normal coordinate in the array. Symbolic
   *          constants GL_BYTE, GL_SHORT, GL_INT, GL_FLOAT, and GL_DOUBLE are accepted.
   */
  public VertexFmtPN(int vertexSize, int vertexType, int normalType) {
    super(vertexSize, vertexType);
    this.normalType = normalType;
    normalPointer = stride;
    stride += 3 * sizeOfType(normalType);
  }

  @Override
  public void setState(GL2 gl) {
    super.setState(gl);
    gl.glEnableClientState(GLPointerFunc.GL_NORMAL_ARRAY);
    gl.glNormalPointer(normalType, stride, normalPointer);
  }

  @Override
  public void unsetState(GL2 gl) {
    super.unsetState(gl);
    gl.glDisableClientState(GLPointerFunc.GL_NORMAL_ARRAY);
  }
}
