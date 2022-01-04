package edu.brown.cs.cs32friends.graph.search;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import edu.brown.cs.cs32friends.graph.EdgeStorable;
import edu.brown.cs.cs32friends.graph.ValuedEdge;
import edu.brown.cs.cs32friends.graph.ValuedVertex;
import edu.brown.cs.cs32friends.graph.VertexStorable;
import edu.brown.cs.cs32friends.graph.search.heuristic.Heuristic;

/**
 * A {@link GraphSearch} implementation of A* search algorithm.
 *
 * @param <T> Value contained in a vertex
 * @param <W> Value contained in an edge
 */
public class AStar<T extends VertexStorable, W extends EdgeStorable>
    implements GraphSearch<T, W> {
  private Heuristic<T> heuristicFunc;

  /**
   * Constructor for the A star search algorithm.
   *
   * @param h the Heuristic function used to estimate distance.
   */
  public AStar(Heuristic<T> h) {
    heuristicFunc = h;
  }

  /**
   * Searches the graph.
   *
   * @param start a {@link ValuedVertex} to start from
   * @param query a value that the destination {@link ValuedVertex} should
   *              contain
   * @return a List of {@link ValuedEdge}s
   */
  @Override
  public List<ValuedEdge<T, W>> search(ValuedVertex<T, W> start, T query) {
    Map<T, ValuedEdge<T, W>> parentMap = new HashMap<>();
    Map<ValuedVertex<T, W>, Double> gScores = new HashMap<>();
    gScores.put(start, 0.0);
    Map<ValuedVertex<T, W>, Double> fScores = new HashMap<>();
    fScores.put(start, heuristicFunc.getEstimate(start.getValue(), query));
    PriorityQueue<ValuedVertex<T, W>> nextVertices = new PriorityQueue<ValuedVertex<T, W>>(
        new DistanceComp<T, W>(fScores));

    ValuedVertex<T, W> currentVert;
    nextVertices.add(start);
    while (!nextVertices.isEmpty()) {
      // Get vertex w/ lowest f(n) value
      currentVert = nextVertices.poll();

      // Checks if this vertex is the goal
      if (currentVert.getValue().equals(query)) {
        return reconstructPath(parentMap, query);
      }

      // Parse over neighbors of this vertex
      for (ValuedEdge<T, W> e : currentVert.getEdges()) {
        ValuedVertex<T, W> dest = e.getDest();
        T destNode = dest.getValue();
        double newG = gScores.get(currentVert) + e.getWeight();

        // Checking if we have seen this vertex and if it's better
        if (gScores.get(dest) == null || newG < gScores.get(dest)) {
          gScores.put(dest, gScores.get(currentVert) + e.getWeight());
          fScores.put(dest,
              gScores.get(dest) + heuristicFunc.getEstimate(destNode, query));
          parentMap.put(destNode, e);
          nextVertices.add(dest);
        }
      }
    }
    return Collections.emptyList();
  }

  /**
   * Reconstructs the path from the search starting from the end node all the
   * way to the beginning.
   *
   * @param parentMap the map between connected nodes and edges.
   * @param endNode   the final node of the search.
   * @return the path of edges and nodes from this search.
   */
  private List<ValuedEdge<T, W>> reconstructPath(
      Map<T, ValuedEdge<T, W>> parentMap, T endNode) {
    List<ValuedEdge<T, W>> path = new LinkedList<>();
    T currentVal = endNode;
    while (parentMap.get(currentVal) != null) {
      ValuedEdge<T, W> nextEdge = parentMap.get(currentVal);
      path.add(nextEdge);
      nextEdge.getSource();
      currentVal = nextEdge.getSource().getValue();
    }
    Collections.reverse(path);
    return path;
  }

  /**
   * DistanceComp Comparator to see which Vertex has the lower weight.
   *
   * @param <T> Value contained in a vertex
   * @param <W> Value contained in an edge
   */
  private class DistanceComp<T extends VertexStorable, W extends EdgeStorable>
      implements Comparator<ValuedVertex<T, W>> {

    private Map<ValuedVertex<T, W>, Double> weights;

    /**
     * Constructor for Distance Comparison taking in the refernce to the map
     * which holds the weights for the search.
     *
     * @param weightsMap a refernce to the map that has the weights for the
     *                   search.
     */
    DistanceComp(Map<ValuedVertex<T, W>, Double> weightsMap) {
      weights = weightsMap;
    }

    @Override
    public int compare(ValuedVertex<T, W> o1, ValuedVertex<T, W> o2) {
      return Double.compare(weights.get(o1), weights.get(o2));
    }
  }
}

