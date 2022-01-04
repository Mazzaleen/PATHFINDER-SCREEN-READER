package edu.brown.cs.cs32friends.graph.lazy;


import edu.brown.cs.cs32friends.graph.EdgeStorable;
import edu.brown.cs.cs32friends.graph.Graph;
import edu.brown.cs.cs32friends.graph.GraphSourceParser;
import edu.brown.cs.cs32friends.graph.ValuedEdge;
import edu.brown.cs.cs32friends.graph.ValuedVertex;
import edu.brown.cs.cs32friends.graph.VertexStorable;
import edu.brown.cs.cs32friends.graph.lazy.LazyGraph;
import edu.brown.cs.cs32friends.graph.lazy.StoredEdge;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class LazyGraphTest {

  @Test
  public void minimalThreeNodeTest() {
    // minimal setup for a three-node three-edge graph
    Node a = new Node("A");
    Node b = new Node("B");
    Node c = new Node("C");

    a.setConnNodes(Set.of(b, c));
    b.setConnNodes(Set.of(a, c));
    c.setConnNodes(Set.of(a, b));

    Line ab = new Line("AB");
    Line bc = new Line("BC");
    Line ac = new Line("AC");

    GraphSourceParser miniParser = new MiniParser(Set.of(a, b, c), Set.of(ab, bc, ac));
    Graph<Node, Line> graph = new LazyGraph(miniParser);

    ValuedVertex<Node, Line> vertA = graph.getVertex(a);
    ValuedVertex<Node, Line> vertB = graph.getVertex(b);
    ValuedVertex<Node, Line> vertC = graph.getVertex(c);

    assertEquals(vertA.getValue(), a);
    assertEquals(vertB.getValue(), b);
    assertEquals(vertC.getValue(), c);

    Set<ValuedEdge<Node, Line>> edgesA = Set.of(new StoredEdge(vertA, vertB, ab), new StoredEdge(vertA, vertC, ac));
    Set<ValuedEdge<Node, Line>> edgesB = Set.of(new StoredEdge(vertB, vertC, bc), new StoredEdge(vertB, vertA, ab));
    Set<ValuedEdge<Node, Line>> edgesC = Set.of(new StoredEdge(vertC, vertA, ac), new StoredEdge(vertC, vertB, bc));

    assertTrue(isEdgeSubset(vertA.getEdges(), edgesA));
    assertTrue(isEdgeSubset(edgesA, vertA.getEdges()));
    assertTrue(isEdgeSubset(vertB.getEdges(), edgesB));
    assertTrue(isEdgeSubset(edgesB, vertB.getEdges()));
    assertTrue(isEdgeSubset(vertC.getEdges(), edgesC));
    assertTrue(isEdgeSubset(edgesC, vertC.getEdges()));
  }

  @Test
  public void extraThreeNodeTest() {
    // adding extra node D, but three-nodes do not connect to it (even if line exists)
    Node a = new Node("A");
    Node b = new Node("B");
    Node c = new Node("C");
    Node d = new Node("D");

    a.setConnNodes(Set.of(b, c));
    b.setConnNodes(Set.of(a, c));
    c.setConnNodes(Set.of(a, b));

    Line ab = new Line("AB");
    Line bc = new Line("BC");
    Line ac = new Line("AC");
    Line ad = new Line("AD");
    Line bd = new Line("BD");
    Line cd = new Line("CD");


    GraphSourceParser miniParser = new MiniParser(Set.of(a, b, c, d), Set.of(ab, bc, ac, ad, bd, cd));
    Graph<Node, Line> graph = new LazyGraph(miniParser);

    ValuedVertex<Node, Line> vertA = graph.getVertex(a);
    ValuedVertex<Node, Line> vertB = graph.getVertex(b);
    ValuedVertex<Node, Line> vertC = graph.getVertex(c);

    assertEquals(vertA.getValue(), a);
    assertEquals(vertB.getValue(), b);
    assertEquals(vertC.getValue(), c);

    Set<ValuedEdge<Node, Line>> edgesA = Set.of(new StoredEdge(vertA, vertB, ab), new StoredEdge(vertA, vertC, ac));
    Set<ValuedEdge<Node, Line>> edgesB = Set.of(new StoredEdge(vertB, vertC, bc), new StoredEdge(vertB, vertA, ab));
    Set<ValuedEdge<Node, Line>> edgesC = Set.of(new StoredEdge(vertC, vertA, ac), new StoredEdge(vertC, vertB, bc));

    assertTrue(isEdgeSubset(vertA.getEdges(), edgesA));
    assertTrue(isEdgeSubset(edgesA, vertA.getEdges()));
    assertTrue(isEdgeSubset(vertB.getEdges(), edgesB));
    assertTrue(isEdgeSubset(edgesB, vertB.getEdges()));
    assertTrue(isEdgeSubset(vertC.getEdges(), edgesC));
    assertTrue(isEdgeSubset(edgesC, vertC.getEdges()));
  }

  @Test
  public void noEdgeToSelfTest() {
    Node a = new Node("A");
    a.setConnNodes(Set.of(a));

    Line aa = new Line("AA");

    GraphSourceParser miniParser = new MiniParser(Set.of(a), Set.of(aa));
    Graph graph = new LazyGraph(miniParser);

    ValuedVertex<Node, Line> vertA = graph.getVertex(a);
    assertTrue(vertA.getEdges().isEmpty());
  }

  @Test
  public void directedEdgeTest() {
    //Tests single directed edge.
    Node a = new Node("A");
    Node b = new Node("B");
    a.setConnNodes(Set.of(b));

    Line ab = new Line("AB");

    GraphSourceParser miniParser = new MiniParser(Set.of(a, b), Set.of(ab));
    Graph graph = new LazyGraph(miniParser);

    ValuedVertex<Node, Line> vertA = graph.getVertex(a);
    ValuedVertex<Node, Line> vertB = graph.getVertex(b);

    assertEquals(vertA.getValue(), a);
    assertEquals(vertB.getValue(), b);

    Set<ValuedEdge<Node,Line>> edgesA = Set.of(new StoredEdge<>(vertA, vertB, ab));

    assertTrue(isEdgeSubset(edgesA, vertA.getEdges()));
    assertTrue(isEdgeSubset(vertA.getEdges(), edgesA));
    assertTrue(vertB.getEdges().isEmpty());
  }



  public boolean isEdgeSubset(Set<ValuedEdge<Node, Line>> se1, Set<ValuedEdge<Node, Line>> se2) {
    boolean isSubset = true;
    for (ValuedEdge e1 : se1) {
      boolean isPresent = false;
      for (ValuedEdge e2: se2) {
        if(isSameEdge(e1, e2)) isPresent = true;
      }
      isSubset = isSubset && isPresent;
    }
    return isSubset;
  }

  public boolean isSameEdge(ValuedEdge e1, ValuedEdge e2) {
    return e1.getValue().equals(e2.getValue())
        && e1.getSource().getValue().equals(e2.getSource().getValue())
        && e2.getSource().getValue().equals(e2.getSource().getValue());
  }



  private class MiniParser implements GraphSourceParser {
    Set<Node> nodes;
    Set<Line> lines;
    public MiniParser(Set<Node> nodes, Set<Line> lines) {
      this.nodes = nodes;
      this.lines = lines;
    }
    @Override
    public Node getVertexValue(String name) {
      for (Node n : nodes) {
        if (n.name.equals(name)) return n;
      }
      return null;
    }
    @Override
    public Set<Line> getEdgeValues(VertexStorable v) {
      Set<Line> out = new HashSet<>();
      Node n = (Node) v;
      for (Line l : lines) {
        if (l.isConnected(n)) out.add(l);
      }
      return out;
    }
    @Override
    public Set<Node> getVertexValues(EdgeStorable e) {
      Set<Node> out = new HashSet<>();
      Line l = (Line) e;
      for (Node n : nodes) {
        if (l.isConnected((n))) out.add(n);
      }
      return out;
    }
  }

  private class Node implements VertexStorable {
    Set<Node> connNodes;
    String name;
    public Node(String name) {
      this.name = name;
      connNodes = new HashSet<>();
    }
    @Override
    public boolean compatibleWith(VertexStorable obj) {
      return connNodes.contains(obj);
    }
    public void setConnNodes(Set<Node> nodes) {
      connNodes = nodes;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Node node = (Node) o;
      return Objects.equals(name, node.name);
    }

    @Override
    public int hashCode() {
      return Objects.hash(name);
    }

    @Override
    public String toString() {
      return "Node{" +
          ", name='" + name + '\'' +
          '}';
    }
  }

  private class Line implements EdgeStorable {
    String containingString;
    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Line line = (Line) o;
      return Objects.equals(containingString, line.containingString);
    }
    @Override
    public int hashCode() {
      return Objects.hash(containingString);
    }
    public Line (String containingString) {
      this.containingString = containingString;
    }
    @Override
    public double getWeight() {
      return 0;
    }
    public boolean isConnected(Node node) {
      return containingString.contains(node.name);
    }

    @Override
    public String toString() {
      return "Line{" +
          "containingString='" + containingString + '\'' +
          '}';
    }
  }

}
