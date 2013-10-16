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

/**
 * Describes the arrangement of data in an vertex array.
 * 
 * @author justin
 */
public abstract class VertexFmt {

  protected int stride;

  /** Size in bytes of each vertex */
  public int getStride() {
    return stride;
  }

  /** Applies this vertex arrangement for any rendering with vertex arrays */
  public abstract void setState(GL2 gl);

  public abstract void unsetState(GL2 gl);

  /** Returns size of supported OpenGL data type sizes in bytes */
  protected int sizeOfType(int type) {
    switch (type) {
    case GL2.GL_DOUBLE:
      return 8;
    case GL2.GL_FLOAT:
    case GL2.GL_INT:
    case GL2.GL_UNSIGNED_INT:
      return 4;
    case GL2.GL_SHORT:
    case GL2.GL_UNSIGNED_SHORT:
      return 2;
    case GL2.GL_BYTE:
    case GL2.GL_UNSIGNED_BYTE:
      return 1;
    default:
      return 0;
    }
  }
}
