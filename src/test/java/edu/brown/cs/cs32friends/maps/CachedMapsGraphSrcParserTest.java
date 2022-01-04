package edu.brown.cs.cs32friends.maps;

import com.google.common.util.concurrent.UncheckedExecutionException;

import edu.brown.cs.cs32friends.maps.CachedMapsGraphSrcParser;
import edu.brown.cs.cs32friends.maps.MapNode;
import edu.brown.cs.cs32friends.maps.MapsDatabase;
import edu.brown.cs.cs32friends.maps.Way;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for CachedMapsGraphSrcParser to ensure that we are caching correctly.
 *
 */
public class CachedMapsGraphSrcParserTest {
  CachedMapsGraphSrcParser<MapNode, Way> cachedParser;
  MapsDatabase parser;

  @Before
  public void setUp() throws Exception {
    parser = new MapsDatabase("data/maps/modifiedSmallMaps.sqlite3");
    cachedParser = new CachedMapsGraphSrcParser<>(parser, 500, 500);
  }

  @Test
  public void getVertexValueTest() {
    // Valid node ID
    assertEquals(new MapNode("/n/2", 41.8206, -71.4), cachedParser.getVertexValue("/n/2"));
    // Invalid node ID
    assertThrows(UncheckedExecutionException.class, () -> cachedParser.getVertexValue("/n/8"));
  }

  @Test
  public void getEdgeValuesTest() {
    Set<Way> onlyOneTraversableWay = new HashSet<>();
    onlyOneTraversableWay.add(new Way("/w/5", 5.0, null));
    assertEquals(onlyOneTraversableWay, cachedParser.getEdgeValues(new MapNode("/n/3", 41.82, -71.4003)));
    assertEquals(Collections.EMPTY_SET, cachedParser.getEdgeValues(new MapNode("/n/7", 90, 100)));
    Set<Way> multipleTraversableWays = new HashSet<>();
    multipleTraversableWays.add(new Way("/w/0", 9, null));
    multipleTraversableWays.add(new Way("/w/2", 9, null));
    assertEquals(multipleTraversableWays, cachedParser.getEdgeValues(new MapNode("/n/0", 41.82, -71.4)));
  }

  @Test
  public void getVertexValuesTest() {
    Way testWay = new Way("/w/0", 90, new MapNode("/n/2", 41.8206, -71.4));
    Set<MapNode> testNodeSet = new HashSet<>();
    testNodeSet.add(new MapNode("/n/2", 41.8206, -71.4));
    assertEquals(testNodeSet, cachedParser.getVertexValues(testWay));
  }
}
