package stream;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import entities.Sku;

/**
 * SkuConsumerManager
 */
public class SkuConsumerManager implements Runnable {

  private final Logger logger = Logger.getLogger(this.getClass());

  private LinkedBlockingQueue<Sku> skuQueue;
  private AtomicBoolean inService;
  private ExecutorService pool;
  private PriorityBlockingQueue<Sku> orderedQueue;
  private Sku searchObject;
  private AtomicInteger skuCounter;
  private int limitForExit;
  private ConcurrentHashMap<String, Integer> weightsMap;

  public SkuConsumerManager(LinkedBlockingQueue<Sku> skuQueue, PriorityBlockingQueue<Sku> orderedQueue,
      AtomicBoolean inService, ExecutorService pool, Sku searchObject, int limitForExit,
      ConcurrentHashMap<String, Integer> weightsMap) {
    this.skuQueue = skuQueue;
    this.inService = inService;
    this.pool = pool;
    this.orderedQueue = orderedQueue;
    this.searchObject = searchObject;
    this.limitForExit = limitForExit;
    this.weightsMap = weightsMap;
    this.skuCounter = new AtomicInteger(0);
  }

  @Override
  public void run() {
    // Assign SKUs to consumers.
    while (this.inService.get()) {
      Sku sku = skuQueue.poll();
      this.pool
          .submit(new SkuConsumer(sku, orderedQueue, searchObject, weightsMap, inService, skuCounter, limitForExit));
    }

    this.printResults();

    this.pool.shutdown();
  }

  /**
   * Print similar SKUs.
   */
  private void printResults() {
    for (int i = 0; i < 11; i++) {
      Sku s = orderedQueue.poll();
      if (s.getName().equals(searchObject.getName())) {
        logger.info("Your query SKU object: " + s.getName() + ", " + s.getSimilarity());
      } else {
        logger.info(s.getName() + ", " + s.getSimilarity());
      }
    }
  }
}