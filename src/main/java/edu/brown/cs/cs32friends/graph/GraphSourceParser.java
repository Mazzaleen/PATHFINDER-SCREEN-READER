package edu.brown.cs.cs32friends.graph;

import java.util.Set;

/**
 * An interface that parses information from a data source into Objects to be stored in a graph.
 */
public interface GraphSourceParser {
  /**
   * Outputs an {@link Object} parsed from the data source that matches the query given.
   *
   * @param name An identifier for the requested Object.
   * @param <T>  The type of Object
   * @return An {@link Object} parsed from the data source
   */
  <T extends VertexStorable> T getVertexValue(String name);

  /**
   * Gets the values associated with a given {@link VertexStorable}.
   *
   * @param v   a value stored in a {@link ValuedVertex}
   * @param <W> The type of Object that a {@link ValuedEdge} stores
   * @return A set of values associated with this {@link VertexStorable}
   */
  <W extends EdgeStorable> Set<W> getEdgeValues(VertexStorable v);

  /**
   * Gets the values associated with a given {@link EdgeStorable}.
   *
   * @param e   a value stored in a {@link ValuedEdge}
   * @param <T> The type of Object that a {@link ValuedVertex} stores
   * @return A set of values associated with this {@link EdgeStorable}
   */
  <T extends VertexStorable> Set<T> getVertexValues(EdgeStorable e);
}
