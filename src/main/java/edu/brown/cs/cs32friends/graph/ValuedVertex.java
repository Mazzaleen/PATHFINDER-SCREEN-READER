package edu.brown.cs.cs32friends.graph;

import java.util.Set;

/**
 * @param <T> The type of Object that ValuedVertex stores
 * @param <W> The type of Object that ValuedEdge stores
 */
public interface ValuedVertex<T extends VertexStorable, W extends EdgeStorable> {
  /**
   * Acquires the value stored in this ValuedVertex.
   *
   * @return The valued stored
   */
  T getValue();

  /**
   * Returns all the ValuedEdges that connected from this ValuedVertex.
   *
   * @return a Set of ValuedEdges
   */
  Set<ValuedEdge<T, W>> getEdges();
}
