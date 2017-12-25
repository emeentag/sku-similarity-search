package server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import org.apache.log4j.Logger;

import config.Config;
import entities.Sku;
import stream.SkuConsumerManager;
import stream.SkuGenerator;

/**
 * GetHandler
 */
public class GetHandler implements HttpHandler {

  private final Logger logger = Logger.getLogger(this.getClass());

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    URI requestedURI = exchange.getRequestURI();

    String searchSKU = requestedURI.getPath().replace("/", "");

    String response = "This is not a valid query. Please query like /sku-1";

    if (validateQuery(searchSKU)) {
      response = getSimilarity(searchSKU);
    }

    logger.info(response);

    exchange.sendResponseHeaders(200, response.length());
    OutputStream os = exchange.getResponseBody();
    os.write(response.getBytes());
    os.close();
  }

  private boolean validateQuery(String searchSKU) {
    return searchSKU.matches("^sku-.\\d*$");
  }

  private String getSimilarity(String searchSKU) {

    ExecutorService pool = Executors.newCachedThreadPool();

    AtomicBoolean inService = new AtomicBoolean(true);

    LinkedBlockingQueue<Sku> skuQueue = new LinkedBlockingQueue<Sku>();

    PriorityBlockingQueue<Sku> orderedQueue = new PriorityBlockingQueue<Sku>(2048,
        (s1, s2) -> Integer.compare(s1.getSimilarity(), s2.getSimilarity()));

    Sku searchObject = Sku.Map.toSku(searchSKU, Config.SKU_OBJECTS.getJSONObject(searchSKU));

    // Create sku stream by reading the json file.
    SkuGenerator skuGenerator = new SkuGenerator(Config.SKU_OBJECTS, skuQueue, inService);

    // Create sku consumer manager for consuming the skus.
    int limitForExit = Config.SKU_OBJECTS.keySet().size();
    SkuConsumerManager skuConsumerManager = new SkuConsumerManager(skuQueue, orderedQueue, inService, pool,
        searchObject, limitForExit, Config.WEIGHTS_MAP);

    // Initialize stream and consumers.
    pool.submit(skuGenerator);
    pool.submit(skuConsumerManager);

    // Wait until analyze is done.
    while (inService.get()) {
    }

    inService.set(false);
    pool.shutdown();

    return prepareResponse(orderedQueue, searchObject).toString();

  }

  /**
   * Print similar SKUs.
   */
  private StringBuffer prepareResponse(PriorityBlockingQueue<Sku> orderedQueue, Sku searchObject) {
    StringBuffer response = new StringBuffer();

    for (int i = 0; i < 11; i++) {
      Sku s = orderedQueue.poll();
      if (s.getName().equals(searchObject.getName())) {
        response.append("Your query SKU object: ").append(s.getName()).append(", ").append(s.getSimilarity())
            .append("\n");
      } else {
        response.append(s.getName()).append(", ").append(s.getSimilarity()).append("\n");
      }
    }

    return response;
  }

}