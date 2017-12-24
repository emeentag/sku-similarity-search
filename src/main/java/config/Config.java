package config;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Level;
import org.apache.log4j.PropertyConfigurator;

/**
 * Config
 */

public class Config {

  public static Level LOG_LEVEL = Level.ALL;

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