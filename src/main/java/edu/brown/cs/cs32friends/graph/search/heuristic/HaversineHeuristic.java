package edu.brown.cs.cs32friends.graph.search.heuristic;

import edu.brown.cs.cs32friends.maps.MapNode;

/**
 * A {@link Heuristic} function for
 * {@link edu.brown.cs.cs32friends.graph.search.AStar} search algorithm.
 * Calculates the great-circle distance between 2 {@link MapNode}s.
 */
public class HaversineHeuristic implements Heuristic<Mappable> {
  private static final double EARTH_RADIUS = 6371.0; // in km

  @Override
  public double getEstimate(Mappable node1, Mappable node2) {
    double node1Lat = Math.toRadians(node1.getLat());
    double node1Long = Math.toRadians(node1.getLong());
    double node2Lat = Math.toRadians(node2.getLat());
    double node2Long = Math.toRadians(node2.getLong());

    double insideRadical = Math.pow(Math.sin((node2Lat - node1Lat) / 2), 2)
        + (Math.cos(node1Lat) * Math.cos(node2Lat)
            * Math.pow(Math.sin((node2Long - node1Long) / 2), 2));
    return 2 * EARTH_RADIUS * Math.asin(Math.sqrt(insideRadical));
  }
}

