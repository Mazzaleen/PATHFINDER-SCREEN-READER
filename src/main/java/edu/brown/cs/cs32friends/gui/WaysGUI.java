package edu.brown.cs.cs32friends.gui;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import edu.brown.cs.cs32friends.handlers.maps.MapsHandler;
import edu.brown.cs.cs32friends.maps.MapsDatabase;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class WaysGUI implements Route {
  @Override
  public Object handle(Request request, Response response) throws Exception {
    double[] northwest = {0.0, 0.0};
    double[] southeast = {0.0, 0.0};

    try {
      System.out.println("Ways Request: " + request.body());
      JSONObject json = new JSONObject(request.body());
      JSONArray northwestArray = json.getJSONArray("northwest");
      JSONArray southeastArray = json.getJSONArray("southeast");
      northwest[0] = northwestArray.getDouble(0);
      northwest[1] = northwestArray.getDouble(1);
      southeast[0] = southeastArray.getDouble(0);
      southeast[1] = southeastArray.getDouble(1);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    MapsDatabase mapData = MapsHandler.getMapData();
    if (mapData == null) {
      throw new Exception("Map data not initialized");
    }
    List<Double[]> ways = mapData.getWaysAPI(northwest[0], northwest[1], southeast[0], southeast[1]);
    Map<String, List<Double[]>> variables = ImmutableMap.of("ways", ways);
    return new Gson().toJson(variables);
  }
}
