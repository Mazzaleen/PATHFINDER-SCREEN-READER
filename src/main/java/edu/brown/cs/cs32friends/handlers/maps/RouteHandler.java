package edu.brown.cs.cs32friends.handlers.maps;

import java.util.List;

import edu.brown.cs.cs32friends.graph.Graph;
import edu.brown.cs.cs32friends.graph.GraphSourceParser;
import edu.brown.cs.cs32friends.graph.ValuedEdge;
import edu.brown.cs.cs32friends.graph.search.AStar;
import edu.brown.cs.cs32friends.graph.search.GraphSearch;
import edu.brown.cs.cs32friends.graph.search.heuristic.HaversineHeuristic;
import edu.brown.cs.cs32friends.main.CommandHandler;
import edu.brown.cs.cs32friends.main.ParseCommands;
import edu.brown.cs.cs32friends.maps.MapNode;
import edu.brown.cs.cs32friends.maps.MapsDatabase;
import edu.brown.cs.cs32friends.maps.NearestMap;
import edu.brown.cs.cs32friends.maps.Way;

/**
 * RouteHandler class to provide output for the route command.
 */
public class RouteHandler implements CommandHandler {
  private static Graph<MapNode, Way> graph;
  private static GraphSearch<MapNode, Way> searcher = new AStar(
      new HaversineHeuristic());
  private static GraphSourceParser parser;

  /**
   * Runs the necessary functions to handle the input for the route command.
   */
  @Override
  public void handle() {

    List<String> args = ParseCommands.getArguments();
    double lat1, lon1, lat2, lon2;
    String street1, cross1, street2, cross2;
    if (MapsHandler.getMapData() != null) {
      if (args.size() == 5) {
        try {
          // If we are given coordinates
          lat1 = Double.parseDouble(args.get(1));
          lon1 = Double.parseDouble(args.get(2));
          lat2 = Double.parseDouble(args.get(3));
          lon2 = Double.parseDouble(args.get(4));

          // Gets the nearest coordinates for start and end
          NearestMap finder = new NearestMap(new double[] {
              lat1, lon1
          });
          finder.nearestFind(MapsDatabase.getMapTree());
          String start = finder.getBestNode().getID();
          finder = new NearestMap(new double[] {
              lat2, lon2
          });
          finder.nearestFind(MapsDatabase.getMapTree());
          String end = finder.getBestNode().getID();

          // Performs the graph search
          if (start != null && end != null) {
            RouteHandler.findRoute(start, end);
          } else {
            ParseCommands.setOutputString("ERROR: Invalid start or end node.");
          }

        } catch (Exception e) {
          try {
            if (!(args.get(1).startsWith("\"") && args.get(1).endsWith("\"")
                && args.get(2).startsWith("\"") && args.get(2).endsWith("\"")
                && args.get(3).startsWith("\"") && args.get(3).endsWith("\"")
                && args.get(4).startsWith("\"")
                && args.get(4).endsWith("\""))) {
              throw new Exception("ERROR: Invalid data formats.");
            }

            // If we are given street names
            street1 = args.get(1).replaceAll("\"", "");
            cross1 = args.get(2).replaceAll("\"", "");
            street2 = args.get(3).replaceAll("\"", "");
            cross2 = args.get(4).replaceAll("\"", "");

            // Gets the start and end node for the search
            String start = MapsHandler.getMapData().getIntersection(street1,
                cross1);
            String end = MapsHandler.getMapData().getIntersection(street2,
                cross2);

            // Performs the search
            if (start != null && end != null) {
              RouteHandler.findRoute(start, end);
            } else {
              ParseCommands
                  .setOutputString("ERROR: Invalid start or end node." + System.lineSeparator());
            }

          } catch (Exception e2) {
            ParseCommands.setOutputString("ERROR: Invalid data." + System.lineSeparator());
          }
        }
      } else {
        ParseCommands
            .setOutputString("ERROR: Invalid number of arguments." + System.lineSeparator());
      }
    } else {
      ParseCommands.setOutputString("ERROR: No map uploaded." + System.lineSeparator());
    }

    System.out.print(ParseCommands.getOutputString());
  }

  /**
   * Gives two node ids, finds the route. You can use this in tandem with Nearest to find your routes.
   * @param start node ID
   * @param end node ID
   */
  private static void findRoute(String start, String end) {
    parser = MapsHandler.getGraphSource();
    graph = MapsHandler.getGraph();
    MapNode startNode = parser.getVertexValue(start);
    MapNode endNode = parser.getVertexValue(end);
    List<ValuedEdge<MapNode, Way>> path = searcher
        .search(graph.getVertex(startNode), endNode);
    StringBuilder outputString = new StringBuilder();
    if (path.isEmpty()) {
      outputString.append(start + " -/- " + end + System.lineSeparator());
    }
    for (ValuedEdge<MapNode, Way> e : path) {
    // For your Route GUI Handler, maybe modify this to try to use the getLat and getLong functions instead of getID
    // Then you can send the quads of [srcLat, srcLong, destLat, destLong] back as a list of coordinates
      String srcNodeID = e.getSource().getValue().getID(); 
      String destNodeID = e.getDest().getValue().getID();
      outputString.append(srcNodeID + " -> " + destNodeID + " : "
          + e.getValue().getId() + System.lineSeparator());
    }
    ParseCommands.setOutputString(outputString.toString());
  }
}

