package edu.brown.cs.cs32friends.API;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import edu.brown.cs.cs32friends.maps.MapsDatabase;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import spark.*;

import java.util.List;
import java.util.Map;

public class WayAPI implements Route {
  private MapsDatabase db;

  public WayAPI(MapsDatabase db) {
    this.db = db;
  }

  public Object handle(Request request, Response response) throws Exception {
    double[] northwest = {0.0, 0.0};
    double[] southeast = {0.0, 0.0};

    try {
      System.out.println("Request: " + request.body());
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
    List<String> ways = db.getWays(northwest[0], northwest[1], southeast[0], southeast[1]);
    Map<String, List<String>> variables = ImmutableMap.of("ways", ways);
    return new Gson().toJson(variables);
  }

  public String getEndpoint() {
    return "/ways";
  }


  public void setupRoute() {
    Spark.post("/ways", this);
    System.out.println("/ways endpoint set up successfully");
  }
}


