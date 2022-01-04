package edu.brown.cs.cs32friends.maps;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import edu.brown.cs.cs32friends.graph.search.heuristic.HaversineHeuristic;
import edu.brown.cs.cs32friends.graph.search.heuristic.Heuristic;
import edu.brown.cs.cs32friends.graph.search.heuristic.Mappable;
import edu.brown.cs.cs32friends.maps.MapNode;

/**
 * Tests for HaversineHeuristic to ensure that we are correctly calculating the
 * haversine.
 *
 */
public class HaversineHeuristicTest {
  Heuristic<Mappable> greatCircDist;
  MapNode posLatLong1, posLatLong2, posLatNegLong, negLatLong, zeroLat,
      zeroLong, zero;

  @Before
  public void setUp() {
    greatCircDist = new HaversineHeuristic();
    zero = new MapNode("0", 0, 0);
    posLatLong1 = new MapNode("1", 40.7486, 59.23);
    posLatLong2 = new MapNode("2", 34.89, 112.0);
    posLatNegLong = new MapNode("3", 34.89, -23.90);
    negLatLong = new MapNode("5", -34.89, -23.90);
    zeroLat = new MapNode("6", 0, 56);
    zeroLong = new MapNode("7", -23, 0);
  }

  @Test
  public void getEstimateTest() {
    assertEquals(0, greatCircDist.getEstimate(posLatLong1, posLatLong1), 0.01);
    assertEquals(7471.911, greatCircDist.getEstimate(zero, posLatLong1), 0.01);
    assertEquals(7471.911, greatCircDist.getEstimate(zero, posLatLong1), 0.01);
    assertEquals(9087.930, greatCircDist.getEstimate(zeroLat, posLatNegLong),
        0.013);
    assertEquals(2664.648, greatCircDist.getEstimate(zeroLong, negLatLong),
        0.01);
    assertEquals(greatCircDist.getEstimate(posLatLong2, posLatLong1),
        greatCircDist.getEstimate(posLatLong1, posLatLong2), 0.001);
  }
}
