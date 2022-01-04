package edu.brown.cs.cs32friends.graph;

/**
 * A Graph implementation that requires a source for its information.
 *
 * @param <T> The type of Object that ValuedVertex stores
 * @param <W> The type of Object that ValuedEdge stores
 */
public abstract class Graph<T extends VertexStorable, W extends EdgeStorable> {
  private GraphSourceParser graphBuilder;

  /**
   * Constructs a new Graph.
   *
   * @param g The data source
   */
  public Graph(GraphSourceParser g) {
    graphBuilder = g;
  }

  /**
   * Gets the data source for the Graph.
   *
   * @return The data source
   */
  public GraphSourceParser getGraphBuilder() {
    return graphBuilder;
  }

  /**
   * Searches for a ValuedVertex in the Graph that contains the given value.
   *
   * @param val The valued contained in the wanted ValuedVertex
   * @return a ValuedVertex
   */
  public abstract ValuedVertex<T, W> getVertex(T val);

}
