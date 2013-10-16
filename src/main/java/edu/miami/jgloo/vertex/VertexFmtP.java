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
 * Vertex format that only stores position data.
 * 
 * @author justin
 */
public class VertexFmtP extends VertexFmt {

  protected int vertexSize;
  protected int vertexType;
  protected int vertexPointer;

  /**
   * Initializes a vertex format that stores position data.
   * 
   * @param vertexSize - Specifies the number of coordinates per vertex. Must be 2, 3, or 4.
   * @param vertexType - Specifies the data type of each coordinate in the array. Symbolic constants
   *          GL_SHORT, GL_INT, GL_FLOAT, or GL_DOUBLE are accepted.
   */
  public VertexFmtP(int vertexSize, int vertexType) {
    this.vertexSize = vertexSize;
    this.vertexType = vertexType;
    this.vertexPointer = 0;

    stride = vertexSize * sizeOfType(vertexType);
  }

  @Override
  public void setState(GL2 gl) {
    gl.glEnableClientState(GLPointerFunc.GL_VERTEX_ARRAY);
    gl.glVertexPointer(vertexSize, vertexType, stride, vertexPointer);
  }

  @Override
  public void unsetState(GL2 gl) {
    gl.glDisableClientState(GLPointerFunc.GL_VERTEX_ARRAY);
  }
}
