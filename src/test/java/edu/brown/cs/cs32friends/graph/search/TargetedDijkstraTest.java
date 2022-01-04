package edu.brown.cs.cs32friends.graph.search;

import edu.brown.cs.cs32friends.graph.EdgeStorable;
import edu.brown.cs.cs32friends.graph.ValuedEdge;
import edu.brown.cs.cs32friends.graph.ValuedVertex;
import edu.brown.cs.cs32friends.graph.VertexStorable;
import edu.brown.cs.cs32friends.graph.search.GraphSearch;
import edu.brown.cs.cs32friends.graph.search.TargetedDijkstra;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class TargetedDijkstraTest {
  GraphSearch<NumStorable, NumStorable> searcher;

  public void setUp() {
    searcher = new TargetedDijkstra();
  }

  public void tearDown() {
    searcher = null;
  }

  @Test
  public void singleNodeGraph() {
    setUp();
    NumStorable one = new NumStorable(1);
    BasicVertex node = new BasicVertex(one);
    assertTrue(searcher.search(node, one).isEmpty());
    tearDown();
  }

  @Test
  public void twoNodeGraphTest() {
    setUp();
    NumStorable one = new NumStorable(1);
    NumStorable two = new NumStorable(2);

    BasicVertex node1 = new BasicVertex(one);
    BasicVertex node2 = new BasicVertex(two);

    BasicEdge edge = new BasicEdge(node1, node2, one);
    node1.setEdges(Set.of(edge));

    assertEquals(searcher.search(node1, two), List.of(edge));
    assertTrue(searcher.search(node2, one).isEmpty());
    tearDown();
  }

  @Test
  public void ignoreLocalRewardTest() {
    setUp();
    NumStorable one = new NumStorable(1);
    NumStorable two = new NumStorable(2);
    NumStorable three = new NumStorable(3);

    BasicVertex node1 = new BasicVertex(one);
    BasicVertex node2 = new BasicVertex(two);
    BasicVertex node3 = new BasicVertex(three);

    BasicEdge edge12 = new BasicEdge(node1, node2, two);
    BasicEdge edge23 = new BasicEdge(node2, node3, two);
    BasicEdge edge13 = new BasicEdge(node1, node3, three);

    node1.setEdges(Set.of(edge12));
    node2.setEdges(Set.of(edge23));
    node1.setEdges(Set.of(edge13));

    assertEquals(searcher.search(node1, three), List.of(edge13));
    assertEquals(searcher.search(node2, three), List.of(edge23));
    assertTrue(searcher.search(node3, one).isEmpty());
    tearDown();
  }

  @Test
  public void twoPathsOneNodeTest() {
    setUp();
    NumStorable one = new NumStorable(1);
    NumStorable two = new NumStorable(2);

    BasicVertex node1 = new BasicVertex(one);
    BasicVertex node2 = new BasicVertex(two);

    BasicEdge edge1 = new BasicEdge(node1, node2, new NumStorable(10));
    BasicEdge edge2 = new BasicEdge(node1, node2, one);

    node1.setEdges(Set.of(edge1, edge2));
    assertEquals(searcher.search(node1, two), List.of(edge2));
    assertTrue(searcher.search(node2, one).isEmpty());
    tearDown();
  }

  @Test
  public void localWinsTest() {
    setUp();
    NumStorable three = new NumStorable(3);
    NumStorable four = new NumStorable(4);
    NumStorable five = new NumStorable(5);
    NumStorable ten = new NumStorable(10);

    BasicVertex node3 = new BasicVertex(three);
    BasicVertex node4 = new BasicVertex(four);
    BasicVertex node5 = new BasicVertex(five);
    BasicVertex node10 = new BasicVertex(ten);

    BasicEdge edge34 = new BasicEdge(node3, node4, three);
    BasicEdge edge45 = new BasicEdge(node4, node5, three);
    BasicEdge edge510 = new BasicEdge(node5, node10, three);
    BasicEdge edge310 = new BasicEdge(node3, node10, ten);

    node3.setEdges(Set.of(edge34, edge310));
    node4.addEdge(edge45);
    node5.addEdge(edge510);

    assertEquals(searcher.search(node3, ten), List.of(edge34, edge45, edge510));

    node4.setEdges(new HashSet<>());
    assertEquals(searcher.search(node3, ten), List.of(edge310));

    tearDown();
  }

  @Test
  public void searchTest() {
    setUp();
    BasicVertex<NumStorable, NumStorable> node0 = new BasicVertex<>(new NumStorable(0));
    BasicVertex<NumStorable, NumStorable> node1 = new BasicVertex<>(new NumStorable(1));
    BasicVertex<NumStorable, NumStorable> node2 = new BasicVertex<>(new NumStorable(2));
    BasicVertex<NumStorable, NumStorable> node3 = new BasicVertex<>(new NumStorable(3));
    BasicVertex<NumStorable, NumStorable> node4 = new BasicVertex<>(new NumStorable(4));
    BasicVertex<NumStorable, NumStorable> node5 = new BasicVertex<>(new NumStorable(5));
    BasicVertex<NumStorable, NumStorable> node6 = new BasicVertex<>(new NumStorable(6));
    BasicVertex<NumStorable, NumStorable> node7 = new BasicVertex<>(new NumStorable(7));
    BasicVertex<NumStorable, NumStorable> node8 = new BasicVertex<>(new NumStorable(8));

    BasicEdge<NumStorable, NumStorable> zeroToOne = new BasicEdge<>(node0, node1,
        new NumStorable(4));
    zeroToOne.getWeight();
    BasicEdge<NumStorable, NumStorable> oneToZero = new BasicEdge<>(node1, node0,
        new NumStorable(4));

    BasicEdge<NumStorable, NumStorable> oneToTwo =
        new BasicEdge<>(node1, node2, new NumStorable(8));
    BasicEdge<NumStorable, NumStorable> twoToOne =
        new BasicEdge<>(node2, node1, new NumStorable(8));

    BasicEdge<NumStorable, NumStorable> zeroToSeven = new BasicEdge<>(node0, node7,
        new NumStorable(8));
    BasicEdge<NumStorable, NumStorable> sevenToZero =
        new BasicEdge<>(node7, node0, new NumStorable(8));

    BasicEdge<NumStorable, NumStorable> oneToSeven =
        new BasicEdge<>(node1, node7, new NumStorable(11));
    BasicEdge<NumStorable, NumStorable> sevenToOne =
        new BasicEdge<>(node7, node1, new NumStorable(11));

    BasicEdge<NumStorable, NumStorable> sevenToEight =
        new BasicEdge<>(node7, node8, new NumStorable(7));
    BasicEdge<NumStorable, NumStorable> eightToSeven =
        new BasicEdge<>(node8, node7, new NumStorable(7));

    BasicEdge<NumStorable, NumStorable> sevenToSix =
        new BasicEdge<>(node7, node6, new NumStorable(1));
    BasicEdge<NumStorable, NumStorable> sixToSeven =
        new BasicEdge<>(node6, node7, new NumStorable(1));

    BasicEdge<NumStorable, NumStorable> eightToSix =
        new BasicEdge<>(node8, node6, new NumStorable(6));
    BasicEdge<NumStorable, NumStorable> sixToEight =
        new BasicEdge<>(node6, node8, new NumStorable(6));

    BasicEdge<NumStorable, NumStorable> eightToTwo =
        new BasicEdge<>(node8, node2, new NumStorable(2));
    BasicEdge<NumStorable, NumStorable> twoToEight =
        new BasicEdge<>(node2, node8, new NumStorable(2));

    BasicEdge<NumStorable, NumStorable> fiveToTwo =
        new BasicEdge<>(node5, node2, new NumStorable(4));
    BasicEdge<NumStorable, NumStorable> twoToFive =
        new BasicEdge<>(node2, node5, new NumStorable(4));

    BasicEdge<NumStorable, NumStorable> twoToThree =
        new BasicEdge<>(node2, node3, new NumStorable(7));
    BasicEdge<NumStorable, NumStorable> threeToTwo =
        new BasicEdge<>(node3, node2, new NumStorable(7));

    BasicEdge<NumStorable, NumStorable> threeToFive =
        new BasicEdge<>(node3, node5, new NumStorable(14));
    BasicEdge<NumStorable, NumStorable> fiveToThree =
        new BasicEdge<>(node5, node3, new NumStorable(14));

    BasicEdge<NumStorable, NumStorable> threeToFour =
        new BasicEdge<>(node3, node4, new NumStorable(9));
    BasicEdge<NumStorable, NumStorable> fourToThree =
        new BasicEdge<>(node4, node3, new NumStorable(9));

    BasicEdge<NumStorable, NumStorable> fiveToFour =
        new BasicEdge<>(node5, node4, new NumStorable(10));
    BasicEdge<NumStorable, NumStorable> fourToFive =
        new BasicEdge<>(node4, node5, new NumStorable(10));

    BasicEdge<NumStorable, NumStorable> sixToFive =
        new BasicEdge<>(node6, node5, new NumStorable(2));
    BasicEdge<NumStorable, NumStorable> fiveToSix =
        new BasicEdge<>(node5, node6, new NumStorable(2));

    node0.addEdge(zeroToOne);
    node0.addEdge(zeroToSeven);
    node1.addEdge(oneToSeven);
    node1.addEdge(oneToTwo);
    node1.addEdge(oneToZero);
    node2.addEdge(twoToEight);
    node2.addEdge(twoToFive);
    node2.addEdge(twoToThree);
    node2.addEdge(twoToOne);
    node3.addEdge(threeToFive);
    node3.addEdge(threeToFour);
    node3.addEdge(threeToTwo);
    node4.addEdge(fourToFive);
    node4.addEdge(fourToThree);
    node5.addEdge(fiveToFour);
    node5.addEdge(fiveToSix);
    node5.addEdge(fiveToThree);
    node5.addEdge(fiveToTwo);

    node6.addEdge(sixToEight);
    node6.addEdge(sixToFive);
    node6.addEdge(sixToSeven);

    node7.addEdge(sevenToEight);
    node7.addEdge(sevenToOne);
    node7.addEdge(sevenToSix);
    node7.addEdge(sevenToZero);

    node8.addEdge(eightToSeven);
    node8.addEdge(eightToSix);
    node8.addEdge(eightToTwo);

    assertEquals(Collections.emptyList(), searcher.search(node3, new NumStorable(3)));
    assertEquals(Arrays.asList(zeroToOne, oneToTwo, twoToEight),
        searcher.search(node0, new NumStorable(8)));
    assertEquals(Arrays.asList(eightToTwo, twoToFive, fiveToFour),
        searcher.search(node8, new NumStorable(4)));

    tearDown();
  }

  private class BasicVertex<T extends VertexStorable, W extends EdgeStorable>
      implements ValuedVertex<T, W> {
    private T value;
    private Set<ValuedEdge<T, W>> edges = new HashSet<>();

    public BasicVertex(T val) {
      value = val;
    }

    @Override
    public Set<ValuedEdge<T, W>> getEdges() {
      return edges;
    }

    public void addEdge(ValuedEdge<T, W> e) {
      edges.add(e);
    }
    public void setEdges(Set<ValuedEdge<T, W>> edges) {
      this.edges = edges;
    }

    @Override
    public T getValue() {
      return value;
    }

    @Override
    public String toString() {
      return "BasicVertex{" +
          "value=" + value + '}';
    }
  }

  private class BasicEdge<T extends VertexStorable, W extends EdgeStorable>
      implements ValuedEdge<T, W> {
    private BasicVertex<T, W> source, dest;
    private W value;

    public BasicEdge(BasicVertex<T, W> src, BasicVertex<T, W> dest, W val) {
      source = src;
      this.dest = dest;
      value = val;
    }

    @Override
    public W getValue() {
      return value;
    }

    public BasicVertex<T, W> getSource() {
      return source;
    }

    public BasicVertex<T, W> getDest() {
      return dest;
    }

    public double getWeight() {
      return value.getWeight();
    }

    @Override
    public String toString() {
      return "BasicEdge{" +
          "source=" + source +
          ", dest=" + dest +
          ", value=" + value +
          '}';
    }
  }

  private class NumStorable implements VertexStorable, EdgeStorable {
    private int num;

    public NumStorable(int num) {
      this.num = num;
    }

    @Override
    public boolean compatibleWith(VertexStorable obj) {
      if (obj instanceof NumStorable) {
        return true;
      }
      return false;
    }

    @Override
    public double getWeight() {
      return num;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof NumStorable)) {
        return false;
      }
      NumStorable that = (NumStorable) o;
      return num == that.num;
    }

    @Override
    public int hashCode() {
      return Objects.hash(num);
    }

    @Override
    public String toString() {
      return "NumStorable{" +
          "num=" + num +
          '}';
    }
  }
}
