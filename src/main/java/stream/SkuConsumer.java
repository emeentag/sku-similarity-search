package stream;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import entities.Sku;

/**
 * SkuConsumer
 */
public class SkuConsumer implements Runnable {
  private Sku sku;
  private PriorityBlockingQueue<Sku> orderedQueue;
  private Sku searchObject;
  private ConcurrentHashMap<String, Integer> weightsMap;
  private int attrAcc;
  private AtomicInteger skuCounter;
  private int limit;
  private AtomicBoolean inService;

  public SkuConsumer(Sku sku, PriorityBlockingQueue<Sku> orderedQueue, Sku searchObject,
      ConcurrentHashMap<String, Integer> weightsMap, AtomicBoolean inService, AtomicInteger skuCounter, int limit) {
    this.sku = sku;
    this.orderedQueue = orderedQueue;
    this.searchObject = searchObject;
    this.weightsMap = weightsMap;
    this.skuCounter = skuCounter;
    this.limit = limit;
    this.inService = inService;
  }

  @Override
  public void run() {
    // Calculate similarity.
    if (this.inService.get()) {
      this.sku.getBody().keys().forEachRemaining((key) -> {
        String[] attrParts = this.sku.getBody().getString(key).split("-");
        String[] searchSkuAttrParts = searchObject.getBody().getString(key).split("-");
        int attributeWeight = weightsMap.get(attrParts[1]);
        int attrValue = Math.abs(Integer.valueOf(attrParts[2]) - Integer.valueOf(searchSkuAttrParts[2]));
        attrAcc += (attributeWeight * attrValue);
      });

      // Add this sku to orderedQueue
      this.sku.setSimilarity(attrAcc);
      this.orderedQueue.put(this.sku);

      if (this.skuCounter.incrementAndGet() == limit) {
        inService.set(false);
      }
    }
  }
}