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

public class Sphere {
  private Vec3  c;
  private float r;

  public Vec3 getCenter() {
    return c;
  }

  public float getRadius() {
    return r;
  }

  public Sphere(Vec3 c, float r) {
    this.c = c;
    this.r = r;
  }

  public Vec3 intersect(Ray r) {
    Vec3 p = r.getPosition();
    Vec3 d = r.getDirection();

    Vec3 pMinusC = p.minus(c);
    float a = d.dot(d);
    float b = 2 * d.dot(pMinusC);
    float c = pMinusC.dot(pMinusC) - this.r * this.r;
    float disc = b * b - 4 * a * c;

    // no solution
    if (disc < 0) return null;

    // one solution, barely touches side
    float t;
    if (disc == 0)
      t = (float) (-b + Math.sqrt(disc)) / (2 * a);
    else {
      // two solutions, get the one with smallest t
      float t1 = (float) (-b + Math.sqrt(disc)) / (2 * a);
      float t2 = (float) (-b - Math.sqrt(disc)) / (2 * a);
      t = Math.min(t1, t2);
    }

    return p.plus(d.times(t));
  }
}
