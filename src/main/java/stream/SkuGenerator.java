package stream;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONObject;

import entities.Sku;

/**
 * SkuGenerator
 */
public class SkuGenerator implements Runnable {

  private JSONObject jsonObjects;
  private LinkedBlockingQueue<Sku> skuQueue;
  private AtomicBoolean inService;

  public SkuGenerator(JSONObject jsonObjects, LinkedBlockingQueue<Sku> skuQueue, AtomicBoolean inService) {
    this.jsonObjects = jsonObjects;
    this.skuQueue = skuQueue;
    this.inService = inService;
  }

  @Override
  public void run() {
    if (inService.get()) {
      jsonObjects.keys().forEachRemaining((key) -> {
        skuQueue.add(Sku.Map.toSku(key, jsonObjects.getJSONObject(key)));
      });
    }
  }

  /**
   * @return the skuQueue
   */
  public LinkedBlockingQueue<Sku> getSkuQueue() {
    return skuQueue;
  }
}