package edu.brown.cs.cs32friends.maps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.brown.cs.cs32friends.maps.MapNode;

/**
 * Tests for MapNode to ensure that we are storing and accessing data correctly.
 *
 */
public class MapNodeTest {

  @Test
  public void testBasicFunctions() {
    MapNode noder = new MapNode("id", 40, 50);
    assertNotNull(noder);
    assertEquals(50.0, noder.getCoord(1), 0.1);
    assertEquals("id", noder.getID());
    assertEquals(40.0, noder.getLat(), 0.1);
    assertEquals(50.0, noder.getLong(), 0.1);
  }

  @Test
  public void testDistanceFunctions() {
    MapNode noder = new MapNode("id", 40, 50);
    MapNode noderSecond = new MapNode("i2d", 50, 40);
    assertEquals(1.0, noder.getDistance(new double[] {
        40, 51
    }), 0.1);
    assertEquals(0, noder.getDistance(noder), 0.1);
    assertTrue(noder.compatibleWith(noderSecond));
  }

}
