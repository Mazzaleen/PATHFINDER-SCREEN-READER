package edu.brown.cs.cs32friends.graph.search;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import edu.brown.cs.cs32friends.graph.EdgeStorable;
import edu.brown.cs.cs32friends.graph.ValuedEdge;
import edu.brown.cs.cs32friends.graph.ValuedVertex;
import edu.brown.cs.cs32friends.graph.VertexStorable;

/**
 * * A {@link GraphSearch} that is a version of Dijkstra's algorithm that finds
 * the shortest path * between 2 vertices.
 *
 * @param <T> Value contained in a vertex
 * @param <W> Value contained in an edge
 */
public class TargetedDijkstra<T extends VertexStorable, W extends EdgeStorable>
    implements GraphSearch<T, W> {

  @Override
  /**
   * Searches the graph.
   *
   * @param start a {@link ValuedVertex} to start from
   * @param query a value that the destination {@link ValuedVertex} should
   *              contain
   * @return a List of {@link ValuedEdge}s
   */
  public List<ValuedEdge<T, W>> search(ValuedVertex<T, W> start, T query) {
    VertexDistance<T, W> currentVert = new VertexDistance(start,
        new LinkedList<ValuedEdge<T, W>>(), 0.0);
    PriorityQueue<VertexDistance> nextVertices = new PriorityQueue(
        new DistanceComp<T, W>());
    Set<ValuedVertex<T, W>> visitedVertices = new HashSet<>();
    while (true) {
      if (currentVert.getVertex().getValue().equals(query)) {
        break;
      }
      // Add all the possible nodes to a PriorityQueue
      for (ValuedEdge<T, W> e : currentVert.getVertex().getEdges()) {
        LinkedList<ValuedEdge<T, W>> newPath = new LinkedList<>(
            currentVert.getPath());
        newPath.add(e);
        double newWeight = e.getWeight() + currentVert.getDistance();
        nextVertices.add(new VertexDistance(e.getDest(), newPath, newWeight));
      }

      // Get the next node to visit by going through the PriorityQueue
      boolean foundNewNodeToVisit = false;
      while (!foundNewNodeToVisit && !nextVertices.isEmpty()) {
        VertexDistance<T, W> nextVert = nextVertices.poll();
        if (!visitedVertices.contains(nextVert.getVertex())) {
          foundNewNodeToVisit = true;
          visitedVertices.add(currentVert.getVertex());
          currentVert = nextVert;
        }
      }
      if (!foundNewNodeToVisit) {
        return Collections.emptyList();
      }
    }
    return currentVert.getPath();
  }

  /**
   * A temporary object that holds a ValuedVertex, the path to get to that
   * ValuedVertex from a starting ValuedVertex, and the total weight of the
   * path.
   *
   * @param <T> The type of Object that ValuedVertex stores
   * @param <W> The type of Object that ValuedEdge stores
   */
  private class VertexDistance<T extends VertexStorable, W extends EdgeStorable> {
    private ValuedVertex<T, W> vertex;
    private List<ValuedEdge<T, W>> path;
    private double distance;

    VertexDistance(ValuedVertex<T, W> v, List<ValuedEdge<T, W>> path,
                   double dist) {
      vertex = v;
      this.path = path;
      distance = dist;
    }

    public ValuedVertex<T, W> getVertex() {
      return vertex;
    }

    public List<ValuedEdge<T, W>> getPath() {
      return path;
    }

    public double getDistance() {
      return distance;
    }
  }

  /**
   * DistanceComp Comparator to see which Vertex has the lower weight.
   *
   * @param <T> Value contained in a vertex
   * @param <W> Value contained in an edge
   */
  private class DistanceComp<T extends VertexStorable, W extends EdgeStorable>
      implements Comparator<VertexDistance<T, W>> {

    @Override
    public int compare(VertexDistance<T, W> o1, VertexDistance<T, W> o2) {
      return Double.compare(o1.getDistance(), o2.getDistance());
    }
  }
}


