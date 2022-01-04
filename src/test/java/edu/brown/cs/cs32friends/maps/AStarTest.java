package edu.brown.cs.cs32friends.maps;

import static org.junit.Assert.assertEquals;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import edu.brown.cs.cs32friends.graph.EdgeStorable;
import edu.brown.cs.cs32friends.graph.ValuedEdge;
import edu.brown.cs.cs32friends.graph.ValuedVertex;
import edu.brown.cs.cs32friends.graph.VertexStorable;
import edu.brown.cs.cs32friends.graph.search.AStar;
import edu.brown.cs.cs32friends.graph.search.heuristic.Heuristic;

public class AStarTest {
  private AStar<PointNode, IntWrapper> searcher = new AStar<>(
      new ManhattanHeuristic());
  private Map<String, BasicVertex<PointNode, IntWrapper>> graph = new HashMap<>();
  private Map<String, BasicVertex<PointNode, IntWrapper>> graph2 = new HashMap<>();

  private class PointNode extends Point implements VertexStorable {
    public PointNode(int x, int y) {
      super(x, y);
    }

    @Override
    public boolean compatibleWith(VertexStorable obj) {
      if (obj instanceof PointNode) {
        return true;
      }
      return false;
    }

    @Override
    public boolean equals(Object obj) {
      return super.equals(obj);
    }
  }

  private class IntWrapper implements EdgeStorable {
    private int value;

    public IntWrapper(int value) {
      this.value = value;
    }

    @Override
    public double getWeight() {
      return value;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof IntWrapper)) {
        return false;
      }
      IntWrapper that = (IntWrapper) o;
      return value == that.value;
    }

    @Override
    public int hashCode() {
      return Objects.hash(value);
    }
  }

  private class ManhattanHeuristic implements Heuristic<PointNode> {

    @Override
    public double getEstimate(PointNode node1, PointNode node2) {
      return Math.abs(node1.getX() - node2.getX())
          + Math.abs(node1.getY() - node2.getY());
    }
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
      return "BasicVertex{" + "value=" + value + '}';
    }
  }

  private class BasicEdge<T extends VertexStorable, W extends EdgeStorable>
      implements ValuedEdge<T, W> {
    private String src, dest;
    private Map<String, BasicVertex<T, W>> nodeList;
    private W value;

    public BasicEdge(String src, String dest, W val,
        Map<String, BasicVertex<T, W>> nodeList) {
      this.src = src;
      this.dest = dest;
      value = val;
      this.nodeList = nodeList;
    }

    @Override
    public W getValue() {
      return value;
    }

    @Override
    public BasicVertex<T, W> getSource() {
      return nodeList.get(src);
    }

    @Override
    public BasicVertex<T, W> getDest() {
      return nodeList.get(dest);
    }

    @Override
    public double getWeight() {
      return value.getWeight();
    }

    @Override
    public String toString() {
      return "BasicEdge{" + "dest='" + dest + '\'' + ", nodeList=" + nodeList
          + ", value=" + value + '}';
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof BasicEdge)) {
        return false;
      }
      BasicEdge<?, ?> basicEdge = (BasicEdge<?, ?>) o;
      return src.equals(basicEdge.src) && getDest().equals(basicEdge.getDest())
          && getValue().equals(basicEdge.getValue());
    }

    @Override
    public int hashCode() {
      return Objects.hash(src, getDest(), getValue());
    }
  }

  @Before
  public void setUp() {
    BasicVertex<PointNode, IntWrapper> a = new BasicVertex<>(
        new PointNode(0, 0));
    BasicVertex<PointNode, IntWrapper> b = new BasicVertex<>(
        new PointNode(1, 0));
    BasicVertex<PointNode, IntWrapper> c = new BasicVertex<>(
        new PointNode(2, 0));
    BasicVertex<PointNode, IntWrapper> d = new BasicVertex<>(
        new PointNode(0, 1));
    BasicVertex<PointNode, IntWrapper> e = new BasicVertex<>(
        new PointNode(1, 1));
    BasicVertex<PointNode, IntWrapper> f = new BasicVertex<>(
        new PointNode(2, 1));
    BasicVertex<PointNode, IntWrapper> g = new BasicVertex<>(
        new PointNode(2, 2));
    BasicVertex<PointNode, IntWrapper> h = new BasicVertex<>(
        new PointNode(1, 3));
    BasicVertex<PointNode, IntWrapper> i = new BasicVertex<>(
        new PointNode(2, 3));
    BasicVertex<PointNode, IntWrapper> j = new BasicVertex<>(
        new PointNode(3, 3));
    BasicVertex<PointNode, IntWrapper> k = new BasicVertex<>(
        new PointNode(4, 3));
    BasicVertex<PointNode, IntWrapper> l = new BasicVertex<>(
        new PointNode(5, 3));
    BasicVertex<PointNode, IntWrapper> m = new BasicVertex<>(
        new PointNode(5, 2));
    BasicVertex<PointNode, IntWrapper> n = new BasicVertex<>(
        new PointNode(6, 2));
    BasicVertex<PointNode, IntWrapper> o = new BasicVertex<>(
        new PointNode(6, 3));
    BasicVertex<PointNode, IntWrapper> p = new BasicVertex<>(
        new PointNode(7, 2));
    BasicVertex<PointNode, IntWrapper> q = new BasicVertex<>(
        new PointNode(7, 3));
    BasicVertex<PointNode, IntWrapper> r = new BasicVertex<>(
        new PointNode(10, 3));

    graph.put("a", a);
    graph.put("b", b);
    graph.put("c", c);
    graph.put("d", d);
    graph.put("e", e);
    graph.put("f", f);
    graph.put("g", g);
    graph.put("h", h);
    graph.put("i", i);
    graph.put("j", j);
    graph.put("k", k);
    graph.put("l", l);
    graph.put("m", m);
    graph.put("n", n);
    graph.put("o", o);
    graph.put("p", p);
    graph.put("q", q);
    graph.put("r", r);

    a.addEdge(new BasicEdge<>("a", "b", new IntWrapper(1), graph));
    a.addEdge(new BasicEdge<>("a", "d", new IntWrapper(1), graph));
    a.addEdge(new BasicEdge<>("a", "e", new IntWrapper(1), graph));

    b.addEdge(new BasicEdge<>("b", "a", new IntWrapper(1), graph));
    b.addEdge(new BasicEdge<>("b", "d", new IntWrapper(1), graph));
    b.addEdge(new BasicEdge<>("b", "e", new IntWrapper(1), graph));
    b.addEdge(new BasicEdge<>("b", "f", new IntWrapper(1), graph));
    b.addEdge(new BasicEdge<>("b", "c", new IntWrapper(1), graph));

    c.addEdge(new BasicEdge<>("c", "b", new IntWrapper(1), graph));
    c.addEdge(new BasicEdge<>("c", "e", new IntWrapper(1), graph));
    c.addEdge(new BasicEdge<>("c", "f", new IntWrapper(1), graph));

    d.addEdge(new BasicEdge<>("d", "a", new IntWrapper(1), graph));
    d.addEdge(new BasicEdge<>("d", "b", new IntWrapper(1), graph));
    d.addEdge(new BasicEdge<>("d", "e", new IntWrapper(1), graph));

    e.addEdge(new BasicEdge<>("e", "a", new IntWrapper(1), graph));
    e.addEdge(new BasicEdge<>("e", "d", new IntWrapper(1), graph));
    e.addEdge(new BasicEdge<>("e", "b", new IntWrapper(1), graph));
    e.addEdge(new BasicEdge<>("e", "f", new IntWrapper(1), graph));
    e.addEdge(new BasicEdge<>("e", "c", new IntWrapper(1), graph));
    e.addEdge(new BasicEdge<>("e", "g", new IntWrapper(1), graph));

    f.addEdge(new BasicEdge<>("f", "b", new IntWrapper(1), graph));
    f.addEdge(new BasicEdge<>("f", "e", new IntWrapper(1), graph));
    f.addEdge(new BasicEdge<>("f", "c", new IntWrapper(1), graph));
    f.addEdge(new BasicEdge<>("f", "g", new IntWrapper(1), graph));

    g.addEdge(new BasicEdge<>("g", "h", new IntWrapper(1), graph));
    g.addEdge(new BasicEdge<>("g", "f", new IntWrapper(1), graph));
    g.addEdge(new BasicEdge<>("g", "i", new IntWrapper(1), graph));
    g.addEdge(new BasicEdge<>("g", "j", new IntWrapper(1), graph));

    h.addEdge(new BasicEdge<>("h", "g", new IntWrapper(1), graph));
    h.addEdge(new BasicEdge<>("h", "i", new IntWrapper(1), graph));

    i.addEdge(new BasicEdge<>("i", "g", new IntWrapper(1), graph));
    i.addEdge(new BasicEdge<>("i", "h", new IntWrapper(1), graph));
    i.addEdge(new BasicEdge<>("i", "j", new IntWrapper(1), graph));

    j.addEdge(new BasicEdge<>("j", "i", new IntWrapper(1), graph));
    j.addEdge(new BasicEdge<>("j", "k", new IntWrapper(1), graph));
    j.addEdge(new BasicEdge<>("j", "g", new IntWrapper(1), graph));

    k.addEdge(new BasicEdge<>("k", "j", new IntWrapper(1), graph));
    k.addEdge(new BasicEdge<>("k", "l", new IntWrapper(1), graph));
    k.addEdge(new BasicEdge<>("k", "m", new IntWrapper(1), graph));

    l.addEdge(new BasicEdge<>("l", "k", new IntWrapper(1), graph));
    l.addEdge(new BasicEdge<>("l", "m", new IntWrapper(1), graph));
    l.addEdge(new BasicEdge<>("l", "n", new IntWrapper(1), graph));
    l.addEdge(new BasicEdge<>("l", "o", new IntWrapper(1), graph));

    m.addEdge(new BasicEdge<>("m", "k", new IntWrapper(1), graph));
    m.addEdge(new BasicEdge<>("m", "l", new IntWrapper(1), graph));
    m.addEdge(new BasicEdge<>("m", "n", new IntWrapper(1), graph));
    m.addEdge(new BasicEdge<>("m", "o", new IntWrapper(1), graph));

    n.addEdge(new BasicEdge<>("n", "m", new IntWrapper(1), graph));
    n.addEdge(new BasicEdge<>("n", "l", new IntWrapper(1), graph));
    n.addEdge(new BasicEdge<>("n", "o", new IntWrapper(1), graph));
    n.addEdge(new BasicEdge<>("n", "p", new IntWrapper(1), graph));
    n.addEdge(new BasicEdge<>("n", "q", new IntWrapper(1), graph));

    o.addEdge(new BasicEdge<>("o", "m", new IntWrapper(1), graph));
    o.addEdge(new BasicEdge<>("o", "l", new IntWrapper(1), graph));
    o.addEdge(new BasicEdge<>("o", "n", new IntWrapper(1), graph));
    o.addEdge(new BasicEdge<>("o", "p", new IntWrapper(1), graph));
    o.addEdge(new BasicEdge<>("o", "q", new IntWrapper(1), graph));

    p.addEdge(new BasicEdge<>("p", "q", new IntWrapper(1), graph));
    p.addEdge(new BasicEdge<>("p", "o", new IntWrapper(1), graph));
    p.addEdge(new BasicEdge<>("p", "n", new IntWrapper(1), graph));

    q.addEdge(new BasicEdge<>("q", "p", new IntWrapper(1), graph));
    q.addEdge(new BasicEdge<>("q", "n", new IntWrapper(1), graph));
    q.addEdge(new BasicEdge<>("q", "o", new IntWrapper(1), graph));

    a = new BasicVertex<>(new PointNode(0, 0));
    b = new BasicVertex<>(new PointNode(0, 1));
    c = new BasicVertex<>(new PointNode(1, 0));

    graph2.put("a", a);
    graph2.put("b", b);
    graph2.put("c", c);

    a.addEdge(new BasicEdge<>("a", "b", new IntWrapper(1), graph2));
    a.addEdge(new BasicEdge<>("a", "c", new IntWrapper(5), graph2));
    b.addEdge(new BasicEdge<>("b", "c", new IntWrapper(1), graph2));
  }

  @Test
  public void oneNodeSearchTest() {
    BasicVertex<PointNode, IntWrapper> node = new BasicVertex<>(
        new PointNode(2, 1));
    assertEquals(Collections.EMPTY_LIST,
        searcher.search(node, new PointNode(2, 1)));
  }

  @Test
  public void searchTest() {
    List<ValuedEdge<PointNode, IntWrapper>> actual = searcher
        .search(graph.get("a"), new PointNode(7, 3));
    List<ValuedEdge<PointNode, IntWrapper>> expected = new ArrayList<>();
    expected.add(new BasicEdge<>("a", "e", new IntWrapper(1), graph));
    expected.add(new BasicEdge<>("e", "g", new IntWrapper(1), graph));
    expected.add(new BasicEdge<>("g", "j", new IntWrapper(1), graph));
    expected.add(new BasicEdge<>("j", "k", new IntWrapper(1), graph));
    expected.add(new BasicEdge<>("k", "l", new IntWrapper(1), graph));
    expected.add(new BasicEdge<>("l", "o", new IntWrapper(1), graph));
    expected.add(new BasicEdge<>("o", "q", new IntWrapper(1), graph));
    assertEquals(expected, actual);
  }

  @Test
  public void noPathSearchTest() {
    assertEquals(Collections.EMPTY_LIST,
        searcher.search(graph.get("a"), new PointNode(10, 3)));
    assertEquals(Collections.EMPTY_LIST,
        searcher.search(graph.get("r"), new PointNode(7, 3)));
  }

  @Test
  public void notGreedyBestFirstSearchTest() {
    List<ValuedEdge<PointNode, IntWrapper>> expected = new ArrayList<>();
    expected.add(new BasicEdge<>("a", "b", new IntWrapper(1), graph2));
    expected.add(new BasicEdge<>("b", "c", new IntWrapper(1), graph2));
    assertEquals(expected,
        searcher.search(graph2.get("a"), new PointNode(1, 0)));
  }
}
