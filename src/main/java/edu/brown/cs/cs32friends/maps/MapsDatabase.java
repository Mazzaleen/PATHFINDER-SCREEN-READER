package edu.brown.cs.cs32friends.maps;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Dictionary;

import edu.brown.cs.cs32friends.graph.EdgeStorable;
import edu.brown.cs.cs32friends.graph.GraphSourceParser;
import edu.brown.cs.cs32friends.graph.VertexStorable;
import edu.brown.cs.cs32friends.graph.search.heuristic.HaversineHeuristic;
import edu.brown.cs.cs32friends.graph.search.heuristic.Heuristic;
import edu.brown.cs.cs32friends.graph.search.heuristic.Mappable;
import edu.brown.cs.cs32friends.kdtree.KDTree;
import edu.brown.cs.cs32friends.main.ParseCommands;

/**
 * MapsDatabase class to store information about the database from maps.
 */
public class MapsDatabase implements GraphSourceParser {
  private static Heuristic<Mappable> distCalc = new HaversineHeuristic();
  private static Connection conn = null;
  private static KDTree<MapNode> mapTree;
//  private static Dictionary<Double, List<Double[]>> cache;

  /**
   * Instantiates the maps database, creating tables if necessary. Automatically
   * loads files.
   *
   * @param filename file name of SQLite3 database to open.
   * @throws Exception if file does not exist.
   */
  public MapsDatabase(String filename) throws Exception {

    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + filename;

    // This is a check so we don't create files that don't exist
    if (new File(filename).exists()) {
      conn = DriverManager.getConnection(urlToDB);
      if (!validateDatabase()) {
        MapsDatabase.setMapTree(null);
        throw new Exception("ERROR: Invalid database.");
      } else {
        // Creating KDTree
        MapsDatabase
            .setMapTree(new KDTree<MapNode>(getTraversableWays(), 2, 0));
      }
    } else {
      MapsDatabase.setMapTree(null);
      throw new Exception("ERROR: No such SQL file found.");
    }
  }

  /**
   * Gets all the traversable ways from the database for the KDTree.
   *
   * @return a list of the traversable ways from this database.
   */
  private List<MapNode> getTraversableWays() {
    List<MapNode> output = null;

    try {
      output = new ArrayList<>();
      PreparedStatement prep;
      // Pulling the three relevant node characteristics
      prep = conn.prepareStatement(
          "SELECT DISTINCT node.id, node.latitude, node.longitude FROM node JOIN way ON "
              + "(node.id = way.start OR node.id = way.end) WHERE way.type <> 'unclassified' "
              + "AND way.type <> '';");
      ResultSet result = prep.executeQuery();
      while (result.next()) {
        MapNode node = new MapNode(result.getString(1), result.getDouble(2),
            result.getDouble(3));
        output.add(node);
      }
      result.close();
      prep.close();
    } catch (Exception e) {
      ParseCommands.setOutputString("ERROR: Invalid database operation.");
    }
    return output;
  }

  /**
   * Checks to see whether the database inputted can adequately be searched for
   * actors.
   *
   * @return whether the database inputted can adequately be searched for
   * actors.
   */
  private boolean validateDatabase() {
    try {
      // Performing a check of tables on each column of the table
      PreparedStatement prep;
      prep = conn.prepareStatement("SELECT id, latitude, longitude FROM node;");
      prep.cancel();
      prep = conn
          .prepareStatement("SELECT id, name, type, start, end FROM way;");
      prep.cancel();
      prep.close();
      return true;
    } catch (SQLException e) {
      return false;
    }
  }

  /**
   * Returns a list of double arrays, each containing a starting lat, starting long,
   * ending lat, ending long for use in API.
   *
   * @param lat1 the northern position of the bounding box.
   * @param lon1 the western position of the bounding box.
   * @param lat2 the southern position of the bounding box.
   * @param lon2 the eastern position of the bounding box.
   * @return a list of the coordinates for each wayID between lat1, lon1 and lat2, lon2.
   */
  public List<Double[]> getWaysAPI(double lat1, double lon1, double lat2,
                                   double lon2) throws SQLException, ClassNotFoundException {
//    double cacheKey = lat1 + lat2 + lon1 + lon2; // creates key for the cache
//    if (cache.get(cacheKey) != null){ //checks if this has been checked for before
//      return cache.get(cacheKey);
//    }

    List<Double[]> output = new ArrayList<>();
    String coordQuery = "SELECT a.latitude, a.longitude, b.latitude, b.longitude FROM way " +
        "JOIN node AS a JOIN node AS b " +
        "ON a.id = way.start AND b.id = way.end" +
        " WHERE (a.latitude BETWEEN " + lat2 + " AND " + lat1 +
        " AND a.longitude BETWEEN " + lon1 + " AND " + lon2 + ")" +
        " OR (b.latitude BETWEEN " + lat2 + " AND " + lat1 +
        " AND b.longitude BETWEEN " + lon1 + " AND " + lon2 + ")";
    Class.forName("org.sqlite.JDBC");
    Statement stat = conn.createStatement();
    stat.executeUpdate("PRAGMA foreign_keys=ON;");
    PreparedStatement prep = conn.prepareStatement(coordQuery);
    ResultSet ways = prep.executeQuery();
    while (ways.next()) { // loops through all arrays until there are no more to find strings
      Double[] coord = new Double[4];
      for (int i = 0; i < 4; i++) {
        coord[i] = ways.getDouble(i + 1);
      }
      output.add(coord);
    }
//    cache.put(cacheKey, output); // adds this call to the cache
    return output;
  }

  /**
   * Returns a list of way IDs between lat1, lon1, and lat2, lon2.
   *
   * @param lat1 the northern position of the bounding box.
   * @param lon1 the western position of the bounding box.
   * @param lat2 the southern position of the bounding box.
   * @param lon2 the eastern position of the bounding box.
   * @return a list of the way IDs between lat1, lon1 and lat2, lon2.
   */
  public List<String> getWays(double lat1, double lon1, double lat2,
                              double lon2) throws SQLException, ClassNotFoundException {
    List<String> output = new ArrayList<>();
    String wayQuery = "SELECT way.* FROM way JOIN node AS a JOIN node AS b " +
        "ON a.id = way.start AND b.id = way.end" +
        " WHERE (a.latitude BETWEEN " + lat2 + " AND " + lat1 +
        " AND a.longitude BETWEEN " + lon1 + " AND " + lon2 + ")" +
        " OR (b.latitude BETWEEN " + lat2 + " AND " + lat1 +
        " AND b.longitude BETWEEN " + lon1 + " AND " + lon2 + ")";

    Class.forName("org.sqlite.JDBC");
    Statement stat = conn.createStatement();
    stat.executeUpdate("PRAGMA foreign_keys=ON;");
    PreparedStatement prep = conn.prepareStatement(wayQuery);
    ResultSet ways = prep.executeQuery();

    while (ways.next()) { // loops through all arrays until there are no more to find strings
      output.add(ways.getString(1));
    }
    return output;
  }

  /**
   * Gets the intersection, if any between street and cross.
   *
   * @param street one of the streets in the potential intersection.
   * @param cross  the other street in the potential intersection.
   * @return the node common to both street and cross.
   */
  public String getIntersection(String street, String cross) {

    String output;
    try {
      PreparedStatement prep;
      prep = conn.prepareStatement(
          "SELECT way.start FROM way WHERE (way.name = ?)  AND way.start IN "
              + "(SELECT way.end FROM way WHERE (way.name = ?) );");
      prep.setString(1, street);
      prep.setString(2, cross);

      ResultSet result = prep.executeQuery();
      if (result.next()) {
        output = result.getString(1);
        prep.close();
        result.close();
        return (output);
      } else {
        prep = conn.prepareStatement(
            "SELECT way.start FROM way WHERE (way.name = ?)  AND way.start IN "
                + "(SELECT way.end FROM way WHERE (way.name = ?) );");
        prep.setString(1, cross);
        prep.setString(2, street);
        result = prep.executeQuery();
        if (result.next()) {
          output = result.getString(1);
          prep.close();
          result.close();
          return (output);
        } else {
          prep = conn.prepareStatement(
              "SELECT way.start FROM way WHERE (way.name = ?)  AND way.start IN "
                  + "(SELECT way.start FROM way WHERE (way.name = ?) );");
          prep.setString(1, street);
          prep.setString(2, cross);
          result = prep.executeQuery();
          if (result.next()) {
            output = result.getString(1);
            prep.close();
            result.close();
            return (output);
          } else {
            prep = conn.prepareStatement(
                "SELECT way.end FROM way WHERE (way.name = ?)  AND way.end IN "
                    + "(SELECT way.end FROM way WHERE (way.name = ?) );");
            prep.setString(1, street);
            prep.setString(2, cross);
            result = prep.executeQuery();
            if (result.next()) {
              output = result.getString(1);
              prep.close();
              result.close();
              return (output);
            }
          }
        }
      }
    } catch (SQLException e) {
      ParseCommands.setOutputString("ERROR: Invalid database operation.");
    }
    return null;
  }

  /**
   * Gets the KDTree of MapNodes created from this database.
   *
   * @return the KDTree of MapNodes created from this database.
   */
  public static KDTree<MapNode> getMapTree() {
    return mapTree;
  }

  /**
   * Sets the database KDTree to mapTree.
   *
   * @param mapTree the new KDTree to set for this database.
   */
  public static void setMapTree(KDTree<MapNode> mapTree) {
    MapsDatabase.mapTree = mapTree;
  }

  @Override
  /**
   * Gets the vertex with the value name from this database.
   *
   * @param name the name of the vertex
   * @return {@link MapNode} with a given ID
   */
  public MapNode getVertexValue(String name) throws IllegalArgumentException {
    try {
      PreparedStatement prep = conn
          .prepareStatement("SELECT * FROM node AS n " + "WHERE n.id = ?;");
      prep.setString(1, name);
      ResultSet rs = prep.executeQuery();
      if (!rs.next()) {
        throw new IllegalArgumentException("Node '" + name + "' not found.");
      }
      String id = rs.getString(1);
      double lat = rs.getDouble(2);
      double longitude = rs.getDouble(3);
      rs.close();
      prep.close();
      return new MapNode(id, lat, longitude);
    } catch (SQLException e) {
      throw new IllegalArgumentException("Invalid database.");
    }
  }

  @Override
  /**
   * Gets the edges connected to v.
   *
   * @param v the vertex whose edges we wish to get.
   * @return A set of {@link Way}s that a {@link MapNode} can go to.
   */
  public Set<Way> getEdgeValues(VertexStorable v)
      throws IllegalArgumentException {
    try {
      if (v == null || !(v instanceof MapNode)) {
        throw new IllegalArgumentException(
            "Cannot pass non-Actor to find edges on graph.");
      }
      MapNode m = (MapNode) v;
      // Get all the Ways
      PreparedStatement prep = conn.prepareStatement("SELECT * FROM way AS w "
          + " WHERE w.start = ? AND NOT (w.type = '' OR w.type = 'unclassified');");
      prep.setString(1, m.getID());
      ResultSet rs = prep.executeQuery();

      Set<Way> traversableWays = new HashSet<>();
      while (rs.next()) {
        // Get the end MapNode so that the destinations can be calculated
        String id = rs.getString(1);
        String endNodeID = rs.getString(5);
        MapNode endnode = getVertexValue(endNodeID);
        double edgeWt = distCalc.getEstimate(m, endnode);
        traversableWays.add(new Way(id, edgeWt, endnode));
      }
      rs.close();
      prep.close();

      return traversableWays;
    } catch (SQLException e) {
      throw new IllegalArgumentException("Invalid database.");
    }
  }

  @Override
  /**
   * Gets the destination MapNode from e.
   *
   * @param e whose connected MapNode we wish to find.
   * @return 1 MapNode (contained in a Set) that a {@link Way} points to.
   */
  public Set<MapNode> getVertexValues(EdgeStorable e)
      throws IllegalArgumentException {
    if (e == null || !(e instanceof Way)) {
      throw new IllegalArgumentException(
          "Cannot pass non-Way to find edges on the graph");
    }
    Way w = (Way) e;
    Set<MapNode> destEdge = new HashSet<MapNode>();
    destEdge.add(w.getEndNode());
    return destEdge;
  }
}

