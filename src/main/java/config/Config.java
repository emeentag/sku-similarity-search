package config;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Level;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONObject;

import utils.FileReader;
import utils.Utils;

/**
 * Config
 */

public class Config {

  public static Level LOG_LEVEL;
  public static ConcurrentHashMap<String, Integer> WEIGHTS_MAP;
  public static int SERVER_PORT;
  public static String FILE_PATH;
  public static JSONObject SKU_OBJECTS;

  /**
   * Set env vars.
   */
  public static void initConfig() {
    String logLevel = System.getenv("LOG_LEVEL");
    String serverPort = System.getenv("SERVER_PORT");
    String filePath = System.getenv("FILE_PATH");
    String weights = System.getenv("WEIGHTS_MAP");

    checkLogLevel(logLevel);

    checkServerPort(serverPort);

    checkFilePath(filePath);

    checkWeights(weights);

    configureLogger();
  }

  private static void checkWeights(String weights) {
    if (weights != null) {
      //Sample weight config "a:10,b:9,c:8,d:7,e:6,f:5,g:4,h:3,i:2,j:1"
      WEIGHTS_MAP = new ConcurrentHashMap<>();
      String[] weightListParts = weights.split(",");
      for (String weight : weightListParts) {
        String[] weightParts = weight.split(":");
        WEIGHTS_MAP.put(weightParts[0], Integer.valueOf(weightParts[1]));
      }
    } else {
      WEIGHTS_MAP = new ConcurrentHashMap<>();
      WEIGHTS_MAP.put("a", 10);
      WEIGHTS_MAP.put("b", 9);
      WEIGHTS_MAP.put("c", 8);
      WEIGHTS_MAP.put("d", 7);
      WEIGHTS_MAP.put("e", 6);
      WEIGHTS_MAP.put("f", 5);
      WEIGHTS_MAP.put("g", 4);
      WEIGHTS_MAP.put("h", 3);
      WEIGHTS_MAP.put("i", 2);
      WEIGHTS_MAP.put("j", 1);
    }
  }

  private static void checkFilePath(String filePath) {
    if (filePath != null) {
      FILE_PATH = filePath;
    } else {
      FileReader fileReader = new FileReader();
      FILE_PATH = fileReader.findFilePath("test-data.json", new File(System.getProperty("user.dir")));
    }
  }

  private static void checkServerPort(String serverPort) {
    if (serverPort != null) {
      SERVER_PORT = Integer.valueOf(serverPort);

      if (SERVER_PORT == 0) {
        SERVER_PORT = 8080;
      }
    } else {
      SERVER_PORT = 8080;
    }
  }

  private static void checkLogLevel(String logLevel) {
    if (logLevel != null) {
      if (logLevel.equals("all")) {
        LOG_LEVEL = Level.ALL;
      } else if (logLevel.equals("debug")) {
        LOG_LEVEL = Level.DEBUG;
      } else if (logLevel.equals("info")) {
        LOG_LEVEL = Level.INFO;
      } else {
        LOG_LEVEL = Level.OFF;
      }
    } else {
      LOG_LEVEL = Level.ALL;
    }
  }

  /**
  * Setups the loader.
  * Uses the log4j.properties
  */
  public static void configureLogger() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hhmmss");
    System.setProperty("currentDate", dateFormat.format(new Date()));
    System.setProperty("logLevel", Config.LOG_LEVEL.toString());

    FileReader fileReader = new FileReader();
    String log4jProperties = fileReader.findFilePath("log4j.properties", new File(System.getProperty("user.dir")));

    if (log4jProperties != null) {
      PropertyConfigurator.configure(log4jProperties);
    } else {
      System.out.println("There is not log4j.properties file found in this directory.");
    }
  }
}