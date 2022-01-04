package edu.brown.cs.cs32friends.handlers.maps;

import java.util.List;

import edu.brown.cs.cs32friends.API.WayAPI;
import edu.brown.cs.cs32friends.main.CommandHandler;
import edu.brown.cs.cs32friends.main.ParseCommands;

/**
 * WaysHandler class to provide output for the way command.
 */
public class WaysHandler implements CommandHandler {
  /**
   * Runs the necessary functions to handle the input for the way command.
   */
  @Override
  public void handle() {
    List<String> args = ParseCommands.getArguments();
    double lat1, lon1, lat2, lon2;
    if (MapsHandler.getMapData() != null) {
      if (args.size() == 5) {
        try {
          lat1 = Double.parseDouble(args.get(1));
          lon1 = Double.parseDouble(args.get(2));
          lat2 = Double.parseDouble(args.get(3));
          lon2 = Double.parseDouble(args.get(4));
          // Checks that the 1st coordintate is the NW corner and that 2nd
          // coordinate is the SE
          // corner
          if (lat1 <= lat2 || lon1 >= lon2) {
            throw new IllegalArgumentException();
          } else {
        	  // This will work once the TODO is finished in MapsDatabase
            List<String> outputLister = MapsHandler.getMapData().getWays(lat1,
                lon1, lat2, lon2);
            StringBuffer sb = new StringBuffer();

            for (String s : outputLister) {
              sb.append(s);
              sb.append(System.lineSeparator());
            }
            if (outputLister.size() == 0) {
              sb.append(System.lineSeparator());
            }
            ParseCommands.setOutputString(sb.toString());

          }
        } catch (Exception e) {
          ParseCommands.setOutputString(
              "ERROR: Invalid data formats." + System.lineSeparator());
        }
      } else if (args.get(1).equals("setup")) {
        new WayAPI(MapsHandler.getMapData()).setupRoute();
      } else {
        ParseCommands.setOutputString(
            "ERROR: Invalid number of arguments." + System.lineSeparator());
      }
    } else {
      ParseCommands
          .setOutputString("ERROR: No map uploaded." + System.lineSeparator());
    }
    System.out.print(ParseCommands.getOutputString());
  }
}
