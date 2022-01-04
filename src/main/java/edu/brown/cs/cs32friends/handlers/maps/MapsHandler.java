package edu.brown.cs.cs32friends.handlers.maps;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import edu.brown.cs.cs32friends.API.WayAPI;
import edu.brown.cs.cs32friends.graph.Graph;
import edu.brown.cs.cs32friends.graph.GraphSourceParser;
import edu.brown.cs.cs32friends.graph.lazy.LazyGraph;
import edu.brown.cs.cs32friends.main.CommandHandler;
import edu.brown.cs.cs32friends.main.ParseCommands;
import edu.brown.cs.cs32friends.maps.CachedMapsGraphSrcParser;
import edu.brown.cs.cs32friends.maps.MapNode;
import edu.brown.cs.cs32friends.maps.MapsDatabase;
import edu.brown.cs.cs32friends.maps.Way;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

/**
 * MapsHandler class to provide output for the map command.
 */
public class MapsHandler implements CommandHandler {

  private static MapsDatabase mapData;
  private static final int VERT_CACHE_SIZE = 65536;
  private static final int EDGE_CACHE_SIZE = 65536;
  private static Graph<MapNode, Way> graph;
  private static GraphSourceParser parser;
  /**
   * Runs the necessary functions to handle the input for the map command.
   */
  @Override
  public void handle() {
    List<String> args = ParseCommands.getArguments();
    // Too many arguments
    if (args.size() != 2) {
      ParseCommands
          .setOutputString("ERROR: Improper arguments for command maps");
    } else {
      String path = args.get(1);

      // Loading the database, if it exists
      try {
        MapsDatabase db = new MapsDatabase(path);
        MapsHandler.setMapData(db);
        ParseCommands.setOutputString("map set to " + path);
      } catch (Exception e) {
        ParseCommands.setOutputString(e.getMessage());
      }
    }
    System.out.println(ParseCommands.getOutputString());
  }

  /**
   * Returns the static MapsDatabase object for use between classes.
   *
   * @return the static MapsDatabase object for use between classes.
   */
  public static MapsDatabase getMapData() {
    return mapData;
  }

  /**
   * Returns the static {@link GraphSourceParser} for use between classes.
   *
   * @return the static {@link GraphSourceParser} for use between classes.
   */
  public static GraphSourceParser getGraphSource() {
    return parser;
  }

  /**
   * Returns the static {@link GraphSourceParser} for use between classes.
   *
   * @return the static {@link GraphSourceParser} for use between classes.
   */
  public static Graph<MapNode, Way> getGraph() {
    return graph;
  }

  /**
   * Sets the static variable mapData to the parameter mapData.
   *
   * @param mapData the new value for mapData.
   */
  public static void setMapData(MapsDatabase mapData) {
    MapsHandler.mapData = mapData;
    parser = new CachedMapsGraphSrcParser<>(mapData, VERT_CACHE_SIZE,
        EDGE_CACHE_SIZE);
    graph = new LazyGraph<MapNode, Way>(parser);
  }
}

