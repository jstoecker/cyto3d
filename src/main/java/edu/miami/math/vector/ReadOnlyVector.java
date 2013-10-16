package edu.miami.math.vector;

/**
 * A vector with read-only visibility. The methods described in this interface will never modify the
 * contents of the vector. The purpose of this interface is to pass around vectors while making it
 * clear they should not be modified.
 * 
 * @param <T_R> - read-only type
 * @param <T_RW> - read/write type
 * 
 * @author justin
 */
interface ReadOnlyVector<T_R, T_RW> {

  /** @return The vector's length. */
  float length();

  /** @return The vector's length squared */
  float lengthSquared();

  /** @return A copy of the vector's values in an array. */
  float[] values();

  /** @return The number of components in the vector. */
  int size();

  /** @return The component at index i. */
  float get(int i);

  /** @return The smallest component in the vector. */
  float min();

  /** @return The largest component in the vector. */
  float max();

  /** @return The average value of the components in the vector. */
  float avg();

  /** @return A copy of the vector's values. */
  T_RW clone();

  /** @return The result of (this * val). */
  T_RW times(float val);

  /** @return The result of (this / val). */
  T_RW over(float val);

  /** @return The result of (this + that). */
  T_RW minus(T_R that);

  /** @return The result of (this - that). */
  T_RW plus(T_R that);

  /** @return The dot product of this and that. */
  float dot(T_R that);

  /** @return A unit vector from this. */
  T_RW normalize();

  /**
   * Performs a linear interpolation.
   * 
   * @param that - second vector.
   * @param s - amount to interpolate [0,1].
   * @return A vector with values interpolated between this and that.
   */
  T_RW lerp(T_R that, float s);
}
