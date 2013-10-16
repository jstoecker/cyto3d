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
package edu.miami.math.vector;

/**
 * Describes the local coordinate system axes of an object in world space coordinates.
 * 
 * @author justin
 */
public class Orientation {

  private Vec3 forward, up, right;
  private Mat4 rotation;

  public Orientation(Vec3 forward, Vec3 up, Vec3 right) {
    this.forward = forward;
    this.up = up;
    this.right = right;
    if (forward != null && up != null && right != null) {
      forward.normalize();
      right.normalize();
      up.normalize();
      rotation = Mat4.createOrientation(right, up, forward.times(-1));
    } else {
      rotation = Mat4.createIdentity();
    }
  }

  public Orientation(Mat4 m) {
    this.right = m.transform(Vec3.unitX());
    this.up = m.transform(Vec3.unitY());
    this.forward = m.transform(Vec3.unitZ().times(-1));
  }

  /** Returns the up direction (y-axis) */
  public Vec3 getUp() {
    return up == null ? null : up.clone();
  }

  /** Returns the down direction (negative y-axis) */
  public Vec3 getDown() {
    return up == null ? null : up.times(-1);
  }

  /** Returns the backward direction (z-axis) */
  public Vec3 getBackward() {
    return forward == null ? null : forward.times(-1);
  }

  /** Returns the forward direction (negative z-axis) */
  public Vec3 getForward() {
    return forward == null ? null : forward.clone();
  }

  /** Returns the right direction (x-axis) */
  public Vec3 getRight() {
    return right == null ? null : right.clone();
  }

  /** Returns the left direction (negative x-axis) */
  public Vec3 getLeft() {
    return right == null ? null : right.times(-1);
  }

  public Mat4 getRotation() {
    return rotation;
  }
}
