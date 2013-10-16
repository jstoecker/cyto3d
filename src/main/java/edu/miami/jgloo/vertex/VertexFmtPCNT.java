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
 * Vertex format that stores position, color, normal, and tex. coordinates.
 */
public class VertexFmtPCNT extends VertexFmtPCN {

  protected int[] texCoordSizes;
  protected int[] texCoordTypes;
  protected int[] texCoordPointers;

  /**
   * Initializes a vertex format that stores position, color, normal, and texture coordinate data.
   * 
   * @param vertexSize - Specifies the number of coordinates per vertex. Must be 2, 3, or 4.
   * @param vertexType - Specifies the data type of each vertex coordinate in the array. Symbolic
   *          constants GL_SHORT, GL_INT, GL_FLOAT, or GL_DOUBLE are accepted.
   * @param colorSize - Specifies the number of components per color. Must be 3 or 4.
   * @param colorType - Specifies the data type of each color component in the array. Symbolic
   *          constants GL_BYTE, GL_UNSIGNED_BYTE, GL_SHORT, GL_UNSIGNED_SHORT, GL_INT,
   *          GL_UNSIGNED_INT, GL_FLOAT, and GL_DOUBLE are accepted.
   * @param normalType - Specifies the data type of each normal coordinate in the array. Symbolic
   *          constants GL_BYTE, GL_SHORT, GL_INT, GL_FLOAT, and GL_DOUBLE are accepted.
   * @param texCoordSize - Specifies the number of coordinates per texture coordinate array element.
   *          Must be 1, 2, 3, or 4.
   * @param texCoordType - Specifies the data type of each texture coordinate. Symbolic constants
   *          GL_SHORT, GL_INT, GL_FLOAT, or GL_DOUBLE are accepted.
   */
  public VertexFmtPCNT(int vertexSize, int vertexType, int colorSize, int colorType,
      int normalType, int texCoordSize, int texCoordType) {
    this(vertexSize, vertexType, colorSize, colorType, normalType, new int[] { texCoordSize },
        new int[] { texCoordType });
  }

  /**
   * Initializes a vertex format that stores position, color, normal, and texture coordinate data.
   * 
   * @param vertexSize - Specifies the number of coordinates per vertex. Must be 2, 3, or 4.
   * @param vertexType - Specifies the data type of each vertex coordinate in the array. Symbolic
   *          constants GL_SHORT, GL_INT, GL_FLOAT, or GL_DOUBLE are accepted.
   * @param colorSize - Specifies the number of components per color. Must be 3 or 4.
   * @param colorType - Specifies the data type of each color component in the array. Symbolic
   *          constants GL_BYTE, GL_UNSIGNED_BYTE, GL_SHORT, GL_UNSIGNED_SHORT, GL_INT,
   *          GL_UNSIGNED_INT, GL_FLOAT, and GL_DOUBLE are accepted.
   * @param normalType - Specifies the data type of each normal coordinate in the array. Symbolic
   *          constants GL_BYTE, GL_SHORT, GL_INT, GL_FLOAT, and GL_DOUBLE are accepted.
   * @param texCoordSizes - Specifies the number of coordinates per texture coordinate array
   *          element. Must be 1, 2, 3, or 4.
   * @param texCoordTypes - Specifies the data type of each texture coordinate. Symbolic constants
   *          GL_SHORT, GL_INT, GL_FLOAT, or GL_DOUBLE are accepted.
   */
  public VertexFmtPCNT(int vertexSize, int vertexType, int colorSize, int colorType,
      int normalType, int[] texCoordSizes, int[] texCoordTypes) {
    super(vertexSize, vertexType, colorSize, colorType, normalType);
    this.texCoordSizes = texCoordSizes;
    this.texCoordTypes = texCoordTypes;
    this.texCoordPointers = new int[texCoordSizes.length];

    for (int i = 0; i < texCoordSizes.length; i++) {
      texCoordPointers[i] = stride;
      stride += texCoordSizes[i] * sizeOfType(texCoordTypes[i]);
    }
  }

  @Override
  public void setState(GL2 gl) {
    super.setState(gl);
    gl.glEnableClientState(GLPointerFunc.GL_TEXTURE_COORD_ARRAY);
    if (texCoordSizes.length > 0) {
      for (int i = 0; i < texCoordSizes.length; i++) {
        gl.glClientActiveTexture(i);
        gl.glTexCoordPointer(texCoordSizes[i], texCoordTypes[i], stride, texCoordPointers[i]);
      }
      gl.glClientActiveTexture(0);
    } else {
      gl.glTexCoordPointer(texCoordSizes[0], texCoordTypes[0], stride, texCoordPointers[0]);
    }
  }

  @Override
  public void unsetState(GL2 gl) {
    super.unsetState(gl);
    gl.glDisableClientState(GLPointerFunc.GL_TEXTURE_COORD_ARRAY);
  }
}
