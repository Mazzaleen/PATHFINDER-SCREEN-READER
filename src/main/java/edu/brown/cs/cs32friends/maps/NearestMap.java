package edu.brown.cs.cs32friends.maps;

import edu.brown.cs.cs32friends.kdtree.KDTree;

/**
 * NearestMap class to find the MapNode closest to a targetPoint.
 *
 */
public class NearestMap {

  private double[] targetPoint;
  private MapNode bestNode;

  /**
   * NearestMap Constructor wherein the target point is passed in.
   *
   * @param targetPoint the point we are looking for a node closest to.
   */
  public NearestMap(double[] targetPoint) {
    this.targetPoint = targetPoint;
    bestNode = null;
  }

  /**
   * Performs a nearest neighbors search on searchTree to find the node closest
   * to targetPoint.
   *
   * @param searchTree the KDTree to perform the nearest search on.
   */
  public void nearestFind(KDTree<MapNode> searchTree) {
    if (searchTree != null && searchTree.getCurrentNode() != null) {

      MapNode startNode = searchTree.getCurrentNode();
      double currentDistance = startNode.getDistance(targetPoint);

      // See if better and update if necessary
      if (bestNode == null
          || currentDistance < bestNode.getDistance(targetPoint)) {
        bestNode = startNode;
      }

      // Finds the relevant axis (x, y) according to the depth.
      int relevantAxis = searchTree.getDimension();

      // Calculate the node axis coordinate and target point axis coordinate
      double nodeAxisCoord = startNode.getCoord(relevantAxis);
      double targetAxisCoord = targetPoint[relevantAxis];

      // current distance... try startnode
      if (bestNode == null || bestNode.getDistance(targetPoint) >= Math
          .abs(nodeAxisCoord - targetAxisCoord)) {
        // Need to recur on both trees
        nearestFind(searchTree.getLargerTree());
        nearestFind(searchTree.getSmallerTree());

      } else {
        if (nodeAxisCoord < targetAxisCoord) {
          // Recur only on larger tree
          nearestFind(searchTree.getLargerTree());
        } else if (nodeAxisCoord > targetAxisCoord) {
          // Recur only on smaller tree
          nearestFind(searchTree.getSmallerTree());
        } else {
          // Recur on both due to tie
          nearestFind(searchTree.getLargerTree());
          nearestFind(searchTree.getSmallerTree());
        }
      }
    }
  }

  /**
   * Returns the closest MapNode found by the search.
   *
   * @return the closest MapNode found by the search.
   */
  public MapNode getBestNode() {
    return bestNode;
  }
}

