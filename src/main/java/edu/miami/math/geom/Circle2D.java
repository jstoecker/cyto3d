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

import edu.miami.math.vector.Vec2;

/**
 * A circle on the XY plane
 * 
 * @author Justin
 */
public class Circle2D {
  private Vec2  center;
  private float radius;

  public Vec2 getCenter() {
    return center;
  }

  public float getRadius() {
    return radius;
  }

  /**
   * Creates a circle from a central point and radius
   */
  public Circle2D(Vec2 center, float radius) {
    this.center = center;
    this.radius = radius;
  }

  /**
   * Creates a circle that goes through 3 points
   */
  public static Circle2D createCircumcircle(Vec2 a, Vec2 b, Vec2 c) {
    // find intersection perpindicular bisectors
    Vec2 abM = a.plus(b).over(2);
    Vec2 bcM = b.plus(c).over(2);

    Vec2 cbU = b.minus(c).normalize();

    Vec2 abN = a.minus(b).rot90().normalize();

    float t = -cbU.dot(abM.minus(bcM)) / cbU.dot(abN);

    Vec2 center = abM.plus(abN.times(t));
    float radius = center.minus(a).length();

    return new Circle2D(center, radius);
  }

  /**
   * Creates a circle that lies within the triangle made by 3 points
   */
  public static Circle2D createInscribedCircle(Vec2 a, Vec2 b, Vec2 c) {
    return null;
  }
}
