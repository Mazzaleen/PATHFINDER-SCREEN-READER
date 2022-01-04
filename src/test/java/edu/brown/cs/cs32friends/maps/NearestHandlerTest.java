package edu.brown.cs.cs32friends.maps;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import edu.brown.cs.cs32friends.handlers.maps.MapsHandler;
import edu.brown.cs.cs32friends.handlers.maps.NearestHandler;
import edu.brown.cs.cs32friends.main.ParseCommands;

/**
 * Tests for NearestHandler to ensure that we are correctly handling the nearest
 * command.
 *
 */
public class NearestHandlerTest {

  @Before
  public void setUp() {
    ParseCommands.setInputLine("map data/maps/smallMaps.sqlite3");
    MapsHandler handler = new MapsHandler();
    handler.handle();
  }

  @Test
  public void simpleNearest() {
    setUp();
    ParseCommands.setInputLine("nearest 0 0");
    NearestHandler handler = new NearestHandler();
    handler.handle();
    assertEquals("/n/0", ParseCommands.getOutputString());
  }

  @Test
  public void complexNearest() {
    setUp();
    ParseCommands.setInputLine("nearest 41.8206 -71.4003");
    NearestHandler handler = new NearestHandler();
    handler.handle();
    assertEquals("/n/5", ParseCommands.getOutputString());
  }

  @Test
  public void noMap() {
    MapsHandler.setMapData(null);
    ParseCommands.setInputLine("nearest 41.8206 -71.4003");
    NearestHandler handler = new NearestHandler();
    handler.handle();
    assertEquals("ERROR: No map uploaded.", ParseCommands.getOutputString());
  }

  @Test
  public void badData() {
    ParseCommands.setInputLine("map pom.xml");
    MapsHandler mapper = new MapsHandler();
    mapper.handle();
    ParseCommands.setInputLine("nearest 41.8206 -71.4003");
    NearestHandler handler = new NearestHandler();
    handler.handle();
    assertEquals("ERROR: Invalid data.", ParseCommands.getOutputString());
  }

  @Test
  public void wrongArgs() {
    setUp();
    ParseCommands.setInputLine("nearest 41.8206 -71.4003 10");
    NearestHandler handler = new NearestHandler();
    handler.handle();
    assertEquals("ERROR: Invalid number of arguments.",
        ParseCommands.getOutputString());
  }
}
