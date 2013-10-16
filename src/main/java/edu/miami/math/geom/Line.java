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

import edu.miami.math.vector.Vec3;

public class Line {

  private Vec3 a;
  private Vec3 b;

  public Line(Vec3 a, Vec3 b) {
    this.a = a;
    this.b = b;
  }

  /** Intersects infinite line with a plane */
  public Vec3 intersect(Plane p) {
    Vec3 n = p.getNormal();
    Vec3 d = b.minus(a);

    // ray's direction is parallel to surface of the plane
    float nDotD = n.dot(d);
    if (nDotD == 0) return null;

    float t = (n.dot(p.getPoint()) - n.dot(a)) / nDotD;

    return a.plus(d.times(t));
  }

  /** Intersects line segment defined by this line's two points and a plane */
  public Vec3 intersectSegment(Plane p) {
    Vec3 n = p.getNormal();
    Vec3 d = b.minus(a);

    // ray's direction is parallel to surface of the plane
    float nDotD = n.dot(d);
    if (nDotD == 0) return null;

    float t = (n.dot(p.getPoint()) - n.dot(a)) / nDotD;

    if (t >= 0 && t <= 1) return a.plus(d.times(t));

    return null;
  }
}
