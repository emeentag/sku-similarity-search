package stream;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import entities.Sku;

/**
 * SkuConsumer
 */
public class SkuConsumer implements Runnable {

  private Sku sku;
  private PriorityBlockingQueue<Sku> orderedQueue;
  private AtomicBoolean inService;
  private Sku searchObject;
  private ConcurrentHashMap<String, Integer> weightsMap;

  public SkuConsumer(Sku sku, PriorityBlockingQueue<Sku> orderedQueue, AtomicBoolean inService, Sku searchObject,
      ConcurrentHashMap<String, Integer> weightsMap) {
    this.sku = sku;
    this.inService = inService;
    this.orderedQueue = orderedQueue;
    this.searchObject = searchObject;
    this.weightsMap = weightsMap;
  }

  @Override
  public void run() {
    if (inService.get()) {

    }
  }

}