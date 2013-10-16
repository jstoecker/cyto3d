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

import java.util.ArrayList;

import edu.miami.math.vector.RVec3;
import edu.miami.math.vector.Vec3;

/**
 * Plane class
 * 
 * @author Justin Stoecker
 */
public class Plane {
  protected Vec3 a; // a point on the plane
  protected Vec3 n; // normal of the surface of the plane

  public Vec3 getNormal() {
    return n;
  }

  public Vec3 getPoint() {
    return a;
  }

  /**
   * A plane defined by a point on the plane and its normal
   */
  public Plane(RVec3 a, RVec3 n) {
    this.a = a.clone();
    this.n = n.clone();
  }

  public Vec3 intersect(Ray r) {
    // x = p + dt
    // (x-a).n = 0
    // (p+dt - a).n = 0
    // n.p + n.dt - n.a = 0
    // t = (n.a - n.p) / n.d

    Vec3 p = r.getPosition();
    Vec3 d = r.getDirection();

    // ray's direction is parallel to surface of the plane
    float nDotD = n.dot(d);
    if (nDotD == 0) return null;

    // time at which ray intersects plane
    float t = (n.dot(a) - n.dot(p)) / nDotD;

    // intersection is in the opposite direction
    if (t < 0) return null;

    // intersection is good
    return p.plus(d.times(t));
  }

  public ArrayList<Vec3> intersect(BoundingBox b) {

    Line[] boxEdges = b.getEdges();
    ArrayList<Vec3> pts = new ArrayList<Vec3>(6);

    // line / plane intersection for every edge of box
    // max of 6 intersections
    for (Line l : boxEdges) {
      Vec3 intersection = l.intersectSegment(this);
      if (intersection != null) {
        pts.add(intersection);
      }
    }

    return pts;
  }
}
