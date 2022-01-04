package edu.brown.cs.cs32friends.maps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import edu.brown.cs.cs32friends.maps.MapNode;
import edu.brown.cs.cs32friends.maps.Way;

/**
 * Tests for Way to ensure that we are correctly storing and accessing
 * information about ways.
 */
public class WayTest {

  @Test
  public void testAllWayFunctions() {
    MapNode noder = new MapNode("id", 10, 20);
    Way thatWay = new Way("wayid", 30, noder);
    assertEquals("wayid", thatWay.getId());
    assertEquals(noder, thatWay.getEndNode());
    assertEquals(30, thatWay.getWeight(), 0.1);
  }

  @Test
  public void testAlternativeWay() {
    MapNode noder = new MapNode("id", 10, 20);
    MapNode noderSecond = new MapNode("id2", 30, 40);
    Way thatWay = new Way("wayway", 100, noder);
    assertEquals("wayway", thatWay.getId());
    assertNotEquals(noderSecond, thatWay.getEndNode());
    assertEquals(100, thatWay.getWeight(), 0.1);
  }

}
