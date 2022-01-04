package edu.brown.cs.cs32friends.handlers.maps;

import java.util.List;

import edu.brown.cs.cs32friends.main.CommandHandler;
import edu.brown.cs.cs32friends.main.ParseCommands;
import edu.brown.cs.cs32friends.maps.MapsDatabase;
import edu.brown.cs.cs32friends.maps.NearestMap;

/**
 * NearestHandler class to provide output for the nearest command.
 *
 */
public class NearestHandler implements CommandHandler {

  /**
   * Runs the necessary functions to handle the input for the nearest command.
   */
  @Override
  public void handle() {
    List<String> args = ParseCommands.getArguments();
    double lat, lon;
    if (MapsHandler.getMapData() != null) {
      if (args.size() == 3) {
        try {
          lat = Double.parseDouble(args.get(1));
          lon = Double.parseDouble(args.get(2));

          NearestMap mapfinder = new NearestMap(new double[] {
              lat, lon
          });

          mapfinder.nearestFind(MapsDatabase.getMapTree());
          
          // For your Nearest GUI, try to use getLat and getLong, instead of getID, 
          // and send those back to the React Server
          ParseCommands.setOutputString(mapfinder.getBestNode().getID());

        } catch (Exception e) {
          ParseCommands.setOutputString("ERROR: Invalid data.");
        }
      } else {
        ParseCommands.setOutputString("ERROR: Invalid number of arguments.");
      }
    } else {
      ParseCommands.setOutputString("ERROR: No map uploaded.");
    }
    System.out.println(ParseCommands.getOutputString());
  }
}

