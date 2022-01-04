package edu.brown.cs.cs32friends.gui;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.cs32friends.graph.Graph;
import edu.brown.cs.cs32friends.graph.GraphSourceParser;
import edu.brown.cs.cs32friends.graph.ValuedEdge;
import edu.brown.cs.cs32friends.graph.search.AStar;
import edu.brown.cs.cs32friends.graph.search.GraphSearch;
import edu.brown.cs.cs32friends.graph.search.heuristic.HaversineHeuristic;
import edu.brown.cs.cs32friends.handlers.maps.MapsHandler;
import edu.brown.cs.cs32friends.maps.MapNode;
import edu.brown.cs.cs32friends.maps.MapsDatabase;
import edu.brown.cs.cs32friends.maps.NearestMap;
import edu.brown.cs.cs32friends.maps.Way;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Sample nearest GUI
 */
public class RouteGUI implements Route {

  @Override
  public String handle(Request request, Response response) throws Exception {
    double lat1, lon1, lat2, lon2;
    String start, end;
    MapsDatabase mapData;
    List<Double[]> output = null;
    if (MapsHandler.getMapData() == null) {
      System.out.println("Map Data not initialized");
    }
    mapData = MapsHandler.getMapData();
    try {
      JSONArray startArray;
      JSONArray endArray;
      System.out.println("Route Request: " + request.body());
      JSONObject json = new JSONObject(request.body());
      if (json.has("startStreet") && json.has("endStreet")) {
        startArray = json.getJSONArray("startStreet");
        endArray = json.getJSONArray("endStreet");
        start = mapData.getIntersection(startArray.getString(0), startArray.getString(1));
        end = mapData.getIntersection(endArray.getString(0), endArray.getString(1));
      } else if (json.has("start") && json.has("end")) {
        startArray = json.getJSONArray("start");
        endArray = json.getJSONArray("end");
        lat1 = startArray.getDouble(0);
        lon1 = startArray.getDouble(1);
        lat2 = endArray.getDouble(0);
        lon2 = endArray.getDouble(1);
        // Gets the nearest coordinates for start and end
        NearestMap finder = new NearestMap(new double[] {
            lat1, lon1
        });
        finder.nearestFind(MapsDatabase.getMapTree());
        start = finder.getBestNode().getID();
        finder = new NearestMap(new double[] {
            lat2, lon2
        });
        finder.nearestFind(MapsDatabase.getMapTree());
        end = finder.getBestNode().getID();

      } else {
        throw new Exception("JSON doesn't contain correct data");
      }
      // Performs the graph search
      if (start != null && end != null) {
        output = this.findRoute(start, end);
      } else {
        Gson gson = new Gson();
        Map<String, String> variables = ImmutableMap.of("route", "error: invalid start or end nodes");
        return gson.toJson(variables);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    Gson gson = new Gson();
    Map<String, List<Double[]>> variables = ImmutableMap.of("route", output);
    return gson.toJson(variables);
  }

  private List<Double[]> findRoute(String start, String end) {
    GraphSearch<MapNode, Way> searcher = new AStar(new HaversineHeuristic());
    GraphSourceParser parser = MapsHandler.getGraphSource();
    Graph<MapNode, Way> graph = MapsHandler.getGraph();
    MapNode startNode = parser.getVertexValue(start);
    MapNode endNode = parser.getVertexValue(end);
    List<ValuedEdge<MapNode, Way>> path = searcher
        .search(graph.getVertex(startNode), endNode);
    List<Double[]> output = new ArrayList<>();
    for (ValuedEdge<MapNode, Way> e : path) {
      // For your Route GUI Handler, maybe modify this to try to use the getLat and getLong functions instead of getID
      // Then you can send the quads of [srcLat, srcLong, destLat, destLong] back as a list of coordinates
      MapNode srcNode = e.getSource().getValue();
      MapNode destNode = e.getDest().getValue();
      output.add(new Double[] {srcNode.getLat(), srcNode.getLong(),
          destNode.getLat(), destNode.getLong()});
    }
    return output;
  }

}
