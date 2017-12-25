package config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Level;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONObject;

/**
 * Config
 */

public class Config {

  public static Level LOG_LEVEL;
  public static ConcurrentHashMap<String, Integer> WEIGHTS_MAP;
  public static int SERVER_PORT;
  public static String FILE_PATH;
  public static JSONObject SKU_OBJECTS;

  public static void initConfig() {
    SERVER_PORT = 8080;
    LOG_LEVEL = Level.ALL;
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

    FILE_PATH = "/Users/ssimsek/projects/recommendation_service/test-data.json";

    configureLogger();
  }

  /**
  * Setups the loader.
  * Uses the log4j.properties
  */
  public static void configureLogger() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hhmmss");
    System.setProperty("currentDate", dateFormat.format(new Date()));
    System.setProperty("logLevel", Config.LOG_LEVEL.toString());

    String log4jProperties = Config.class.getClassLoader().getResource("log4j.properties").getPath();

    if (log4jProperties != null) {
      PropertyConfigurator.configure(log4jProperties);
    } else {
      System.out.println("There is not log4j.properties file found in this directory.");
    }
  }
}