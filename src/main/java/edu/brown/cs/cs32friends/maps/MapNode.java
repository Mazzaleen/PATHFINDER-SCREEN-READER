package edu.brown.cs.cs32friends.maps;

import java.util.Arrays;
import java.util.Objects;

import edu.brown.cs.cs32friends.graph.VertexStorable;
import edu.brown.cs.cs32friends.graph.search.heuristic.Mappable;
import edu.brown.cs.cs32friends.kdtree.KDTreeNode;

/**
 * MapNode class to store information for a KDTree about a node's id, latitude,
 * and longitude.
 */
public class MapNode implements KDTreeNode, VertexStorable, Mappable {

  private String id;
  private double[] latlon = new double[2];

  /**
   * MapNode Constructor which takes in an ID, latitude, and longitude.
   *
   * @param id  the unique identifying ID of this node.
   * @param lat the latitude of this node.
   * @param lon the longitdue of this node.
   */
  public MapNode(String id, double lat, double lon) {
    this.id = id;
    latlon[0] = lat;
    latlon[1] = lon;
  }

  /**
   * Gets the coordinate of the MapNode in the given dimension. The 0th
   * dimension is defined to be the latitude, and 1st dimension is defined to be
   * the longitude.
   *
   * @return the coordinate of the MapNode in the given dimension.
   */
  @Override
  public double getCoord(int dimension) {
    return latlon[dimension];
  }

  /**
   * Gets the distance between this MapNode and a different KDTreeNode.
   *
   * @param node the node to compare this MapNode to.
   * @return the distance between this MapNode and another KDTreeNode.
   */
  @Override
  public double getDistance(KDTreeNode node) {
    try {
      double squared = Math.pow((this.getCoord(0) - node.getCoord(0)), 2)
          + Math.pow((this.getCoord(1) - node.getCoord(1)), 2);
      return Math.sqrt(squared);
    } catch (Exception e) {
      return 0;
    }
  }

  /**
   * Gets the distance between this MapNode and another point.
   *
   * @param point the node to compare this MapNode to.
   * @return the distance between this MapNode and point.
   */
  @Override
  public double getDistance(double[] point) {
    try {
      double squared = Math.pow((this.getCoord(0) - point[0]), 2)
          + Math.pow((this.getCoord(1) - point[1]), 2);
      return Math.sqrt(squared);
    } catch (Exception e) {
      return 0;
    }
  }

  /**
   * Gets the ID of this MapNode.
   *
   * @return the ID of this MapNode.
   */
  public String getID() {
    return id;
  }

  /**
   * Checks if a {@link edu.brown.cs.cs32friends.graph.ValuedVertex}
   * containing obj can be connected to another
   * {@link edu.brown.cs.cs32friends.graph.ValuedVertex} containing this
   * {@link MapNode}. Note that this operation is not symmetric.
   *
   * @param obj an {@link Object} to be compared to
   * @return true or false
   */
  @Override
  public boolean compatibleWith(VertexStorable obj) {
    if (!(obj instanceof MapNode)) {
      return false;
    }
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof MapNode)) {
      return false;
    }
    MapNode mapNode = (MapNode) o;
    return id.equals(mapNode.id) && Arrays.equals(latlon, mapNode.latlon);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = Objects.hash(id);
    result = prime * result + Arrays.hashCode(latlon);
    return result;
  }

  @Override
  public double getLat() {
    return getCoord(0);
  }

  @Override
  public double getLong() {
    return getCoord(1);
  }
}
