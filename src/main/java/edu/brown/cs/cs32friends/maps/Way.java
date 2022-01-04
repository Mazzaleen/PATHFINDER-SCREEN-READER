package edu.brown.cs.cs32friends.maps;

import java.util.Objects;

import edu.brown.cs.cs32friends.graph.EdgeStorable;

/**
 * Way class to store information about Ways as an Edge for a search algorithm.
 */
public class Way implements EdgeStorable {
  private String id;
  private double wt;
  private MapNode endNode;

  /**
   * Constructs a new Way.
   *
   * @param id      the ID of the Way
   * @param weight  the great circle between the endpoints of the Way
   * @param endNode the {@link MapNode} that this Way points to
   */
  public Way(String id, double weight, MapNode endNode) {
    this.id = id;
    this.wt = weight;
    this.endNode = endNode;
  }

  /**
   * Acquires the weight a
   * {@link edu.brown.cs.cs32friends.graph.lazy.StoredEdge} would output if
   * it contained this EdgeStorable.
   *
   * @return a weight
   */
  @Override
  public double getWeight() {
    return wt;
  }

  /**
   * Acquires this Way's ID.
   *
   * @return the ID
   */
  public String getId() {
    return id;
  }

  /**
   * Gets the {@link MapNode} to be contained in the destination
   * {@link edu.brown.cs.cs32friends.graph.ValuedVertex}.
   *
   * @return a {@link MapNode} to be contained in the destination
   * {@link edu.brown.cs.cs32friends.graph.ValuedVertex}
   */
  public MapNode getEndNode() {
    return endNode;
  }

  @Override
  /**
   * Checks if 2 Ways are equal. Because each Way has a unique ID, this is the only field checked
   * for equality.
   */
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Way)) {
      return false;
    }
    Way way = (Way) o;
    return getId().equals(way.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}

