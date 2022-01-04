package edu.brown.cs.cs32friends.maps;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import edu.brown.cs.cs32friends.handlers.maps.MapsHandler;
import edu.brown.cs.cs32friends.handlers.maps.RouteHandler;
import edu.brown.cs.cs32friends.main.ParseCommands;

/**
 * Tests for RouteHandler to ensure that we are correctly handling the route
 * command.
 *
 */
public class RouteHandlerTest {

  @Before
  public void setUp() {
    ParseCommands.setInputLine("map data/maps/smallMaps.sqlite3");
    MapsHandler handler = new MapsHandler();
    handler.handle();
  }

  @Test
  public void simpleRoute() {
    setUp();
    ParseCommands.setInputLine("route 41.82 -71.4 41.8207 -71.4");
    RouteHandler handler = new RouteHandler();
    handler.handle();
    assertEquals("/n/0 -> /n/1 : /w/0" + System.lineSeparator()
        + "/n/1 -> /n/2 : /w/1" + System.lineSeparator(),
        ParseCommands.getOutputString());
  }

  @Test
  public void noRoute() {
    setUp();
    ParseCommands.setInputLine("route 41.8203 -71.4003 41.8203 -71.4");
    RouteHandler handler = new RouteHandler();
    handler.handle();
    assertEquals("/n/4 -/- /n/1" + System.lineSeparator(),
        ParseCommands.getOutputString());
  }

  @Test
  public void routeIntersection() {
    setUp();
    ParseCommands.setInputLine(
        "route \"Radish Spirit Blvd\" \"Chihiro Ave\" \"Kamaji Pl\" \"Yubaba St\"");
    RouteHandler handler = new RouteHandler();
    handler.handle();
    assertEquals("/n/0 -> /n/1 : /w/0" + System.lineSeparator()
        + "/n/1 -> /n/2 : /w/1" + System.lineSeparator() + "/n/2 -> /n/5 : /w/4"
        + System.lineSeparator(), ParseCommands.getOutputString());
  }

  @Test
  public void routeNoIntersection() {
    setUp();
    ParseCommands.setInputLine(
        "route \"Radish Spirit Blvd\" \"Kamaji Pl\" \"Kamaji Pl\" \"Yubaba St\"");
    RouteHandler handler = new RouteHandler();
    handler.handle();
    assertEquals("ERROR: Invalid start or end node." + System.lineSeparator(),
        ParseCommands.getOutputString());
  }

  @Test
  public void routeBadArgs() {
    setUp();
    ParseCommands.setInputLine("route 12 12 12 30 30");
    RouteHandler handler = new RouteHandler();
    handler.handle();
    assertEquals("ERROR: Invalid number of arguments." + System.lineSeparator(),
        ParseCommands.getOutputString());
  }

  @Test
  public void routeNoMap() {
    MapsHandler.setMapData(null);
    ParseCommands.setInputLine("route 12 12 12 30");
    RouteHandler handler = new RouteHandler();
    handler.handle();
    assertEquals("ERROR: No map uploaded." + System.lineSeparator(),
        ParseCommands.getOutputString());
  }

  @Test
  public void routeInvalidData() {
    setUp();
    ParseCommands.setInputLine("route 12 12 12 obama");
    RouteHandler handler = new RouteHandler();
    handler.handle();
    assertEquals("ERROR: Invalid data." + System.lineSeparator(),
        ParseCommands.getOutputString());
  }

}
