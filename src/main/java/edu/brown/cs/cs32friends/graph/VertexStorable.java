package edu.brown.cs.cs32friends.graph;

/**
 * An interface for Objects to be stored in a {@link ValuedVertex}.
 */
public interface VertexStorable {
  /**
   * Checks if the current VertexStorable is compatible with a given Object.
   * Note that this operation is not symmetric.
   *
   * @param obj an {@link Object} to be compared to
   * @return true or false
   */
  boolean compatibleWith(VertexStorable obj);
}
