package edu.brown.cs.cs32friends.graph.search;

import java.util.List;

import edu.brown.cs.cs32friends.graph.EdgeStorable;
import edu.brown.cs.cs32friends.graph.ValuedEdge;
import edu.brown.cs.cs32friends.graph.ValuedVertex;
import edu.brown.cs.cs32friends.graph.VertexStorable;

/**
 * An interface for graph search algorithms.
 * @param <T> Value contained in a vertex
 * @param <W> Value contained in a edge
 */
public interface GraphSearch<T extends VertexStorable, W extends EdgeStorable> {
  /**
   * Searches the graph.
   *
   * @param start a {@link ValuedVertex} to start from
   * @param query a value that the destination {@link ValuedVertex} should contain
   * @return a List of {@link ValuedEdge}s
   */
  List<ValuedEdge<T, W>> search(ValuedVertex<T, W> start, T query);
}
