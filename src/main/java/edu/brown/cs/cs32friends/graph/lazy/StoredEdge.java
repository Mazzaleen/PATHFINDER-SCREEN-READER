package edu.brown.cs.cs32friends.graph.lazy;

import edu.brown.cs.cs32friends.graph.EdgeStorable;
import edu.brown.cs.cs32friends.graph.ValuedEdge;
import edu.brown.cs.cs32friends.graph.ValuedVertex;
import edu.brown.cs.cs32friends.graph.VertexStorable;

/**
 * An edge of a graph that stores any kind of value.
 *
 * @param <T> The type of {@link Object} that {@link ValuedVertex} stores
 * @param <W> The type of {@link Object} that ValuedEdge stores
 */
public class StoredEdge<T extends VertexStorable, W extends EdgeStorable> implements
    ValuedEdge<T, W> {
  private W val;
  private ValuedVertex<T, W> source;
  private ValuedVertex<T, W> dest;

  /**
   * Constructs a new StoredEdge.
   *
   * @param source the {@link ValuedVertex} this StoredEdge comes from
   * @param dest the {@link ValuedVertex} this StoredEdge points to
   * @param val the value to be stored
   */
  public StoredEdge(ValuedVertex<T, W> source, ValuedVertex<T, W> dest, W val) {
    this.source = source;
    this.dest = dest;
    this.val = val;
  }

  /**
   * Acquires the value stored in this ValueEdge.
   *
   * @return The value stored
   */
  @Override
  public W getValue() {
    return val;
  }

  /**
   * Returns the ValuedVertex that this ValuedEdge originated from.
   *
   * @return The source ValuedEdge
   */
  @Override
  public ValuedVertex<T, W> getSource() {
    return source;
  }

  /**
   * Returns the ValuedVertex that this ValuedEdge points to.
   *
   * @return The destination ValuedEdge
   */
  @Override
  public ValuedVertex<T, W> getDest() {
    return dest;
  }

  /**
   * Returns the weight of this ValuedEdge.
   *
   * @return The weight
   */
  @Override
  public double getWeight() {
    return val.getWeight();
  }
}
