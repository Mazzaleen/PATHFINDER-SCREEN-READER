package edu.brown.cs.cs32friends.maps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import edu.brown.cs.cs32friends.handlers.maps.MapsHandler;
import edu.brown.cs.cs32friends.main.ParseCommands;

/**
 * Tests for MapsHandler to ensure that we are correctly handling the map
 * command.
 *
 */
public class MapsHandlerTest {

  @Test
  public void testWorkingMap() {
    ParseCommands.setInputLine("map data/maps/smallMaps.sqlite3");
    MapsHandler handler = new MapsHandler();
    handler.handle();
    assertNotNull(MapsHandler.getGraph());
    assertNotNull(MapsHandler.getGraphSource());
    assertNotNull(MapsHandler.getMapData());
    assertEquals("map set to data/maps/smallMaps.sqlite3",
        ParseCommands.getOutputString());
  }

  @Test
  public void testFailingMap() {
    ParseCommands.setInputLine("map pom.xml");
    MapsHandler handler = new MapsHandler();
    handler.handle();
    assertEquals("ERROR: Invalid database.", ParseCommands.getOutputString());
  }

  @Test
  public void testFakeMap() {
    ParseCommands.setInputLine("map asdf");
    MapsHandler handler = new MapsHandler();
    handler.handle();
    assertEquals("ERROR: No such SQL file found.",
        ParseCommands.getOutputString());
  }
}
