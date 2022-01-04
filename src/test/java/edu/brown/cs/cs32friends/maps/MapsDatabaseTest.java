package edu.brown.cs.cs32friends.maps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import edu.brown.cs.cs32friends.maps.MapNode;
import edu.brown.cs.cs32friends.maps.MapsDatabase;
import edu.brown.cs.cs32friends.maps.Way;

/**
 * Tests for the MapsDatabase to ensure proper storage, caching, and SQL queries
 * throughout our code.
 *
 */
public class MapsDatabaseTest {
  MapsDatabase db;

  @Before
  public void setUp() throws Exception {
    db = new MapsDatabase("data/maps/modifiedSmallMaps.sqlite3");
  }

  // THIS COMMAND PASSES!!
  @Test
  public void getWaysTest() throws SQLException, ClassNotFoundException {
    // One way
    List<String> expectedOneWay = new ArrayList<>();
    expectedOneWay.add("/w/8");
    assertEquals(expectedOneWay, db.getWays(44.1, -72.1, 42, -71.9));
    // Inclusive of the boundary
    assertEquals(expectedOneWay, db.getWays(44.1, -72.1, 44, -72));
    // Does not filter out any ways
    List<String> allWays = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      allWays.add("/w/" + i);
    }
    assertEquals(allWays, db.getWays(44.1, -72.1, 40, -70));
    // No ways
    assertEquals(Collections.EMPTY_LIST, db.getWays(45, -73, 44.5, -72.9));
  }

  @Test
  public void getIntersectionTest() {
    // Same start nodes
    assertEquals("/n/0",
        db.getIntersection("Chihiro Ave", "Radish Spirit Blvd"));
    // Checks that it's a symmetrical operation
    assertEquals("/n/0",
        db.getIntersection("Radish Spirit Blvd", "Chihiro Ave"));
    // Same end nodes
    assertEquals("/n/4", db.getIntersection("Yubaba St", "Sootball Ln"));
    // One's start node is other's end
    assertEquals("/n/0", db.getIntersection("Floral St", "Chihiro Ave"));
    assertEquals("/n/3", db.getIntersection("Yubaba St", "Radish Spirit Blvd"));
    // No intersection
    assertNull(db.getIntersection("Kamaji Pl", "Radish Spirit Blvd"));
  }

  @Test
  public void getMapTreeTest() {
    assertNotNull(MapsDatabase.getMapTree());
  }

  @Test
  public void getVertexValueTest() {
    // Valid node ID
    assertEquals(new MapNode("/n/2", 41.8206, -71.4),
        db.getVertexValue("/n/2"));
    // Invalid node ID
    assertThrows(IllegalArgumentException.class,
        () -> db.getVertexValue("/n/8"));
  }

  @Test
  public void getEdgeValuesTest() {
    Set<Way> onlyOneTraversableWay = new HashSet<>();
    onlyOneTraversableWay.add(new Way("/w/5", 5.0, null));
    assertEquals(onlyOneTraversableWay,
        db.getEdgeValues(new MapNode("/n/3", 41.82, -71.4003)));
    assertEquals(Collections.EMPTY_SET,
        db.getEdgeValues(new MapNode("/n/7", 90, 100)));
    Set<Way> multipleTraversableWays = new HashSet<>();
    multipleTraversableWays.add(new Way("/w/0", 9, null));
    multipleTraversableWays.add(new Way("/w/2", 9, null));
    assertEquals(multipleTraversableWays,
        db.getEdgeValues(new MapNode("/n/0", 41.82, -71.4)));
  }

  @Test
  public void getVertexValuesTest() {
    Way testWay = new Way("/w/0", 90, new MapNode("/n/2", 41.8206, -71.4));
    Set<MapNode> testNodeSet = new HashSet<>();
    testNodeSet.add(new MapNode("/n/2", 41.8206, -71.4));
    assertEquals(testNodeSet, db.getVertexValues(testWay));
  }
}
