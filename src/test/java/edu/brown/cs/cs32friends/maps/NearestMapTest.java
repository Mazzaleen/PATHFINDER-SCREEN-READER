package edu.brown.cs.cs32friends.maps;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import edu.brown.cs.cs32friends.kdtree.KDTree;
import edu.brown.cs.cs32friends.maps.MapNode;
import edu.brown.cs.cs32friends.maps.MapsDatabase;
import edu.brown.cs.cs32friends.maps.NearestMap;

/**
 * Tests for NearestMap to ensure that we are correctly correctly finding
 * nearest nodes.
 *
 */
public class NearestMapTest {

  MapsDatabase db;

  @Before
  public void setUp() throws Exception {
    db = new MapsDatabase("data/maps/smallMaps.sqlite3");
  }

  @Test
  public void testSimpleFind() {
    try {
      setUp();
      KDTree<MapNode> searchTree = MapsDatabase.getMapTree();
      NearestMap finder = new NearestMap(new double[] {
          0, 0
      });
      finder.nearestFind(searchTree);
      assertEquals(new MapNode("/n/0", 41.82, -71.4), finder.getBestNode());
    } catch (Exception e) {
    }
  }

  @Test
  public void testTargetFind() {
    try {
      setUp();
      KDTree<MapNode> searchTree = MapsDatabase.getMapTree();
      NearestMap finder = new NearestMap(new double[] {
          41.8206, -71.4003
      });
      finder.nearestFind(searchTree);
      assertEquals(new MapNode("/n/5", 41.8206, -71.4003),
          finder.getBestNode());
    } catch (Exception e) {
    }
  }

}
