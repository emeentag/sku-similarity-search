package server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.log4j.Logger;

import com.sun.net.httpserver.HttpServer;

import config.Config;

/**
 * Server
 */
public class Server {

  public final Logger logger = Logger.getLogger(this.getClass());

  private static Server instance;

  private HttpServer server;

  private Server() {
    try {
      server = HttpServer.create(new InetSocketAddress(Config.SERVER_PORT), 0);

      server.createContext("/", new GetHandler());

      logger.info("Server is started at port: " + Config.SERVER_PORT);

      server.setExecutor(null);

    } catch (IOException e) {
      logger.error(e);
    }
  }

  public void start() {
    server.start();
  }

  public static Server getInstance() {
    if (instance == null) {
      instance = new Server();
    }

    return instance;
  }
}