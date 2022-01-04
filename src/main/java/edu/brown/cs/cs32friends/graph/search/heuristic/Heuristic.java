package edu.brown.cs.cs32friends.graph.search.heuristic;

import edu.brown.cs.cs32friends.graph.search.AStar;

/**
 * An interface for a heuristic function for {@link AStar} search algorithm.
 *
 * @param <T> The type of values to be compared
 */
public interface Heuristic<T> {
  /**
   * Calculates the estimate between the values contained in 2 vertices.
   *
   * @param node1 Value contained in the 1st node
   * @param node2 Value containted in the 2nd node
   * @return An estimate value
   */
  double getEstimate(T node1, T node2);
}
