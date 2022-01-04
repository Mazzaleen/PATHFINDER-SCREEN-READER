package edu.brown.cs.cs32friends.graph.lazy;

import java.util.HashMap;
import java.util.Map;

import edu.brown.cs.cs32friends.graph.EdgeStorable;
import edu.brown.cs.cs32friends.graph.Graph;
import edu.brown.cs.cs32friends.graph.GraphSourceParser;
import edu.brown.cs.cs32friends.graph.ValuedEdge;
import edu.brown.cs.cs32friends.graph.ValuedVertex;
import edu.brown.cs.cs32friends.graph.VertexStorable;

/**
 * This is a "lazy" implementation of a graph. Edges are only loaded in when they are queried.
 *
 * @param <T> The type of Object that {@link ValuedVertex} stores
 * @param <W> The type of Object that {@link ValuedEdge} stores
 */
public class LazyGraph<T extends VertexStorable, W extends EdgeStorable> extends Graph<T, W> {
  private Map<T, ValuedVertex> lzVertices;

  /**
   * Constructs a new LazyGraph.
   *
   * @param g The data source for the LazyGraph
   */
  public LazyGraph(GraphSourceParser g) {
    super(g);
    lzVertices = new HashMap<>();
  }

  /**
   * Searches for a {@link ValuedVertex} in the Graph that contains the given value.
   *
   * @param val The valued contained in the wanted ValuedVertex
   * @return a {@link ValuedVertex}
   */
  @Override
  public ValuedVertex<T, W> getVertex(T val) {
    if (lzVertices.containsKey(val)) {
      return lzVertices.get(val);
    }
    ValuedVertex<T, W> vertex = new LazyVertex<T, W>(val, this.getGraphBuilder(), this);
    lzVertices.put(val, vertex);
    return vertex;
  }
}
