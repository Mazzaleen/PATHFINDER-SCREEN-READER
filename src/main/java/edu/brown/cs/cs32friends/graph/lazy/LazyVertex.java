package edu.brown.cs.cs32friends.graph.lazy;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import edu.brown.cs.cs32friends.graph.EdgeStorable;
import edu.brown.cs.cs32friends.graph.GraphSourceParser;
import edu.brown.cs.cs32friends.graph.ValuedEdge;
import edu.brown.cs.cs32friends.graph.ValuedVertex;
import edu.brown.cs.cs32friends.graph.VertexStorable;

/**
 * A "lazy" implementation of a {@link ValuedVertex} that only loads the ValuedVertices when needed.
 *
 * @param <T> The type of Object that {@link ValuedVertex} stores
 * @param <W> The type of Object that {@link ValuedEdge} stores
 */
public class LazyVertex<T extends VertexStorable, W extends EdgeStorable> implements
    ValuedVertex<T, W> {

  private T val;
  private GraphSourceParser parser;
  private LazyGraph<T, W> lzGraph;
  private Set<ValuedEdge<T, W>> edges;

  /**
   * Constructs a new LazyVertex.
   * @param val The value to be stored in this LazyVertex
   * @param parser The data source for the edges of this ValuedVertex
   * @param graph The {@link LazyGraph} that contains this LazyVertex
   */
  public LazyVertex(T val, GraphSourceParser parser, LazyGraph<T, W> graph) {
    this.val = val;
    this.parser = parser;
    this.lzGraph = graph;
    this.edges = null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof LazyVertex)) {
      return false;
    }
    LazyVertex<?, ?> that = (LazyVertex<?, ?>) o;
    return Objects.equals(val, that.val);
  }

  @Override
  public int hashCode() {
    return Objects.hash(val);
  }

  /**
   * Acquires the value stored in this ValuedVertex.
   *
   * @return The valued stored
   */
  @Override
  public T getValue() {
    return val;
  }

  /**
   * Returns all the ValuedEdges that connected from this ValuedVertex.
   *
   * @return a Set of ValuedEdges
   */
  @Override
  public Set<ValuedEdge<T, W>> getEdges() {
    // if edges is null, load them
    if (edges == null) {
      edges = new HashSet<>();
      // obtain edge values from source
      Set<W> edgeVals = parser.getEdgeValues(val);
      for (W eVal : edgeVals) {
        // find corresponding vertex values form edge value, add if they are compatible
        Set<T> vertexVals = parser.getVertexValues(eVal);
        for (T vVal : vertexVals) {
          if (val.compatibleWith(vVal) && !val.equals(vVal)) {
            edges.add(new StoredEdge<T, W>(this,
                lzGraph.getVertex(vVal),
                eVal));
          }
        }
      }
    }
    return edges;
  }
}
