package stream;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

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

  public SkuConsumer(Sku sku, PriorityBlockingQueue<Sku> orderedQueue, Sku searchObject,
      ConcurrentHashMap<String, Integer> weightsMap) {
    this.sku = sku;
    this.orderedQueue = orderedQueue;
    this.searchObject = searchObject;
    this.weightsMap = weightsMap;
  }

  @Override
  public void run() {
    // Calculate similarity.
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
  }
}