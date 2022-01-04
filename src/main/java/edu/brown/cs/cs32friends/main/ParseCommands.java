package edu.brown.cs.cs32friends.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import edu.brown.cs.cs32friends.handlers.maps.MapsHandler;
import edu.brown.cs.cs32friends.handlers.maps.NearestHandler;
import edu.brown.cs.cs32friends.handlers.maps.RouteHandler;
import edu.brown.cs.cs32friends.handlers.maps.WaysHandler;

/**
 * REPL class for interpreting data from the command line and preparing the
 * proper out put.
 */
public class ParseCommands {

  private HashMap<String, CommandHandler> commandMap;
  private static String inputLine;
  private static String outputString;
  private static List<String> argsList;

  /**
   * REPL class constructor that adds all of the necessary commands that this
   * specific REPL must have.
   */
  public ParseCommands() {
    this.commandMap = new HashMap<String, CommandHandler>();
    this.commandMap.put("map", new MapsHandler());
    this.commandMap.put("route", new RouteHandler());
    this.commandMap.put("nearest", new NearestHandler());
    this.commandMap.put("ways", new WaysHandler());
  }

  /**
   * Sets the static variable input line to the parameter input.
   *
   * @param input the String to set the static inputLine to
   */
  public static void setInputLine(String input) {
    inputLine = input;
    ArrayList<String> args = new ArrayList<>(Arrays.asList(
        ParseCommands.getInputLine().split(" (?=([^\"]*\"[^\"]*\")*[^\"]*$)")));
    ParseCommands.argsList = args;
  }

  /**
   * Given a string command, sees if the command is in the commandMap, and, if
   * it is, calls its handle method (since all things in the commandMap map a
   * string to a CommandHandler with the method handle).
   *
   * @param command the command that we wish to execute
   */
  public void handleArgs(String command) {
    if (this.commandMap.containsKey(command)) {
      this.commandMap.get(command).handle();
    } else {
      System.out.println("ERROR: not recognized command");
      ParseCommands.setOutputString("ERROR: not recognized command");
    }
  }

  /**
   * Sets the static variable outputString to output.
   *
   * @param output the new value for outputString
   */
  public static void setOutputString(String output) {
    outputString = output;
  }

  /**
   * Returns the static variable outputString.
   *
   * @return what should be printed to the command line or website
   */
  public static String getOutputString() {
    return outputString;
  }

  /**
   * Returns the static variable inputString.
   *
   * @return the string that was fed as input, either through the command line
   *         or through the website.
   */
  public static String getInputLine() {
    return inputLine;
  }

  /**
   * Returns the arguments in an easily digestable list form.
   *
   * @return the arguments in an easily digestable list form.
   */
  public static List<String> getArguments() {
    return new ArrayList<String>(ParseCommands.argsList);
  }

}
