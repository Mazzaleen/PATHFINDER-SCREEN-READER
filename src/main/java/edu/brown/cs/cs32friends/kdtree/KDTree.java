package edu.brown.cs.cs32friends.kdtree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * KDTree class to store data as a KDTree, a data structure that allows for
 * quick access of multidimensional data.
 *
 * @param <T> the type of the KDTreeNode being stored in this tree
 */
public class KDTree<T extends KDTreeNode> {

  private List<T> starList;
  private int numDimensions;
  private int currentDimension;
  private T currentNode;
  private KDTree<T> smallerTree;
  private KDTree<T> largerTree;
  private int medianPos;

  /**
   * Constructs a KDTree object with the given list of stars and the given
   * dimension at which to partition.
   *
   * @param givenStars the list of stars to store in the KDTree
   * @param dim        the number of dimensions in the tree
   * @param current    the current dimension of the tree
   */
  public KDTree(List<T> givenStars, int dim, int current) {
    this.starList = givenStars;
    this.numDimensions = dim;
    this.currentDimension = current;

    // end recursion if list is empty
    if (!starList.isEmpty()) {
      this.currentNode = this.getMedian();
      this.smallerTree = this.makeSmallerTree();
      this.largerTree = this.makeLargerTree();
    }
  }

  /**
   * Returns the list of stars that this KDTree contains.
   *
   * @return the list of stars that this KDTree contains
   */
  public List<T> getStarList() {
    return new ArrayList<T>(this.starList);
  }

  /**
   * Sorts the starList based on the given partition dimension of the KDTree
   * instance.
   */
  private void sortByDimension() {
    Collections.sort(this.starList, new SortDimension(this.currentDimension));
  }

  /**
   * Gets the median of StarList in the current dimension.
   *
   * @return the Star that is the median in the dimension
   */
  private T getMedian() {
    // chooses higher number when even number of elements
    this.sortByDimension();
    this.medianPos = (this.starList.size() / 2);
    return this.starList.get(this.medianPos);
  }

  /**
   * Returns a List of Stars whose dimensional value is smaller than the median.
   *
   * @return the List of Stars that are smaller than the KDTree's median
   */
  private List<T> smallerThanMedian() {
    List<T> smallerStars = new ArrayList<>();
    for (int i = 0; i < this.medianPos; i++) {
      smallerStars.add(this.starList.get(i));
    }
    return new ArrayList<T>(smallerStars);
  }

  /**
   * Returns a List of Stars whose dimensional value is greater than the median.
   *
   * @return the List of Stars that are greater than the KDTree's median
   */
  private List<T> greaterThanMedian() {
    List<T> greaterStars = new ArrayList<>();
    for (int i = 1 + this.medianPos; i < starList.size(); i++) {
      greaterStars.add(this.starList.get(i));
    }
    return new ArrayList<T>(greaterStars);
  }

  /**
   * Creates the smaller KDTree subtree, passing in the next dimension to
   * partition.
   *
   * @return the smaller KDTree
   */
  private KDTree<T> makeSmallerTree() {
    List<T> smallerList = smallerThanMedian();
    return new KDTree<T>(smallerList, this.numDimensions,
        (this.currentDimension + 1) % this.numDimensions);
  }

  /**
   * Creates the larger KDTree subtree, passing in the next dimension to
   * partition.
   *
   * @return the larger KDTree
   */
  private KDTree<T> makeLargerTree() {
    List<T> greaterList = greaterThanMedian();
    return new KDTree<T>(greaterList, this.numDimensions,
        (this.currentDimension + 1) % this.numDimensions);
  }

  /**
   * Returns this KDTree's smaller subtree.
   *
   * @return the smaller KDTree from this KDTree
   */
  public KDTree<T> getSmallerTree() {
    return this.smallerTree;
  }

  /**
   * Returns this KDTree's larger subtree.
   *
   * @return the larger KDTree from this KDTree
   */
  public KDTree<T> getLargerTree() {
    return this.largerTree;
  }

  /**
   * Gets the current split dimension for this KDTree.
   *
   * @return the current partition dimension of this KDTree
   */
  public int getDimension() {
    return this.currentDimension;
  }

  /**
   * Gets the current star at the head of the KDTree.
   *
   * @return the current star at the head of this KDTree
   */
  public T getCurrentNode() {
    return this.currentNode;
  }
}



/**
 * Comparator to sort by X dimension.
 *
 */
class SortDimension implements Comparator<KDTreeNode> {

  /**
   * Compares two Stars to see which has a greater X coordinate.
   *
   * @return 1 if Star a's X Coordinate is greater than Star b's; 0 if the two
   *         are equal; and -1 if Star a's X Coordinate is less than Star b's
   */
  private int dim;

  SortDimension(int currentDimension) {
    this.dim = currentDimension;
  }

  @Override
  public int compare(KDTreeNode a, KDTreeNode b) {
    double diff = a.getCoord(dim) - b.getCoord(dim);
    if (diff > 0) {
      return 1;
    } else if (diff < 0) {
      return -1;
    } else {
      return 0;
    }
  }
}

