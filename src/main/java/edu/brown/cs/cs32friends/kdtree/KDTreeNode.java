package edu.brown.cs.cs32friends.kdtree;

/**
 * Interface KDTreeNode is a type that allows for more generic things to be
 * stored in this KDTree provided that they have these specific arguments.
 *
 */
public interface KDTreeNode {

  /**
   * Gets this KDTreeNode's coordinate in the specified dimension.
   *
   * @param dimension the dimension we need to get from this node.
   * @return the 'dimension' Coordinate of this KDTreeNode
   */
  double getCoord(int dimension);

  /**
   * Gets the distance between this KDTreeNode and node.
   *
   * @param node the node to find the distance to.
   * @return the distance between this KDTreeNode and node.
   */
  double getDistance(KDTreeNode node);

  /**
   * Gets the distance between this KDTreeNode and point.
   *
   * @param point the node to find the distance to.
   * @return the distance between this KDTreeNode and point.
   */
  double getDistance(double[] point);
}

