package edu.brown.cs.cs32friends.graph;

/**
 * An interface for edges on a graph that holds an Object.
 *
 * @param <T> The type of Object that ValuedVertex stores
 * @param <W> The type of Object that ValuedEdge stores
 */
public interface ValuedEdge<T extends VertexStorable, W extends EdgeStorable> {

  /**
   * Acquires the value stored in this ValueEdge.
   *
   * @return The value stored
   */
  W getValue();

  /**
   * Returns the ValuedVertex that this ValuedEdge originated from.
   *
   * @return The source ValuedEdge
   */
  ValuedVertex<T, W> getSource();

  /**
   * Returns the ValuedVertex that this ValuedEdge points to.
   *
   * @return The destination ValuedEdge
   */
  ValuedVertex<T, W> getDest();

  /**
   * Returns the weight of this ValuedEdge.
   *
   * @return The weight
   */
  double getWeight();
}

