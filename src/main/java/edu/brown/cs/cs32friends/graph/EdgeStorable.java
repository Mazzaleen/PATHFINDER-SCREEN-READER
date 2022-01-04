package edu.brown.cs.cs32friends.graph;

/**
 * An interface for Objects to be stored in a {@link ValuedEdge}.
 */
public interface EdgeStorable {
  /**
   * Acquires the weight a {@link ValuedEdge} would output if it contained this EdgeStorable.
   *
   * @return a weight
   */
  double getWeight();
}
