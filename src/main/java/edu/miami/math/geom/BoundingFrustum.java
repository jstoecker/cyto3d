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

package edu.miami.math.geom;

import edu.miami.math.vector.Mat4;
import edu.miami.math.vector.Vec3;
import edu.miami.math.vector.Vec4;

/**
 * Bounding frustum of a view that can be used for intersection tests NOT COMPLETE
 * 
 * @author justin
 */
public class BoundingFrustum {

  public enum ContainmentType {
    Disjoint,
    Contains,
    Intersects,
  }

  private Mat4 viewProjection;

  public BoundingFrustum(Mat4 view, Mat4 projection) {
    viewProjection = projection.times(view);
  }

  public BoundingFrustum(Mat4 viewProjection) {
    this.viewProjection = viewProjection;
  }

  public boolean contains(Vec3 point) {
    return contains(new Vec4(point, 1));
  }

  public boolean contains(Vec4 point) {
    // transform point to clip space
    Vec4 p = viewProjection.transform(point);

    // perform homogeneous division for normalize device coordinates
    p = p.over(p.w);

    // now check if point is inside cube
    if (p.x > 1 || p.x < -1 || p.y > 1 || p.y < -1 || p.z > 1 || p.z < -1) return false;
    return true;
  }

  public boolean intersects(BoundingBox box) {
    // TODO: check all cases

    // check if any corners of the box are inside the frustum
    Vec3[] corners = box.getCorners();
    for (int i = 0; i < corners.length; i++)
      if (contains(corners[i])) return true;

    // check if an edge intersects

    // check if entire frustum is contained by box

    return false;
  }
}
