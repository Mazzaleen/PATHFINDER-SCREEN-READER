package edu.brown.cs.cs32friends.handlers.maps;

public class Tuple2<K, V> {
  private final K first;
  private final V second;

  public Tuple2(K first, V second) {
    this.first = first;
    this.second = second;
  }

  public K getFirst() {
    return first;
  }

  public V getSecond() {
    return second;
  }

  public String toString() {
    return "Tuple2(" + first.toString() + ", " + second.toString() + ")";
  }
}
