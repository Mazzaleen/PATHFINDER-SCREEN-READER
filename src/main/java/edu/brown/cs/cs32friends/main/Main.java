package edu.brown.cs.cs32friends.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import edu.brown.cs.cs32friends.gui.RouteGUI;
import edu.brown.cs.cs32friends.gui.WaysGUI;
import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * The Main class of our project. This is where execution begins.
 */

public final class Main {

  private static final int DEFAULT_PORT = 4567;

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
    ParseCommands replit = new ParseCommands();

    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);

    OptionSet options = parser.parse(args);

    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }

    try (BufferedReader br = new BufferedReader(
        new InputStreamReader(System.in))) {
      String input;
      while ((input = br.readLine()) != null) {
        input = input.trim();
        ParseCommands.setInputLine(input);
        String command = ParseCommands.getArguments().get(0);
        replit.handleArgs(command);
      }
    } catch (Exception e) {
      System.out.println("ERROR: Invalid input for REPL");
      ParseCommands.setOutputString("ERROR: Invalid input for REPL");
    }
  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private void runSparkServer(int port) {
//    Spark.port(port);
//    Spark.externalStaticFileLocation("src/main/resources/static");
//    Spark.exception(Exception.class, new ExceptionPrinter());
//
//    FreeMarkerEngine freeMarker = createEngine();
//
//    // Setup Spark Routes
//    Spark.options("/*", (request, response) -> {
//      response.header("Access-Control-Allow-Methods", "GET,POST");
//      response.header("Access-Control-Allow-Origin", "*");
//      response.header("Access-Control-Allow-Headers",
//          "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,access-control-allow-origin");
//      return "OK";
//    });
//    Spark.get("/nearest", new NearestGUI()); // THINK: would a GET or POST request be better?

    Spark.port(port);

    Spark.options("/*", (request, response) -> {
      String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
      if (accessControlRequestHeaders != null) {
        response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
      }

      String accessControlRequestMethod = request.headers("Access-Control-Request-Method");

      if (accessControlRequestMethod != null) {
        response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
      }

      return "OK";
    });

    Spark.before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
    Spark.exception(Exception.class, new ExceptionPrinter());
    Spark.post("/route", new RouteGUI());
    Spark.post("/ways", new WaysGUI());
  }

  /**
   * Display an error page when an exception occurs in the server.
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }
}
