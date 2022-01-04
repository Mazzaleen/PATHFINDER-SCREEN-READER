package edu.brown.cs.cs32friends.graph.search.heuristic;

/**
 * An interface for objects that have latitude and longitude coordinates.
 * To be used with {@link HaversineHeuristic}.
 */
public interface Mappable {
  /**
   * @return the latitude of this Mappable (in degrees)
   */
  double getLat();

  /**
   * @return the longitude of this Mappable (in degrees)
   */
  double getLong();
}
