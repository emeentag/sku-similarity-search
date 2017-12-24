package stream;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import entities.Sku;

/**
 * SkuConsumerManager
 */
public class SkuConsumerManager implements Runnable {

  private LinkedBlockingQueue<Sku> skuQueue;
  private AtomicBoolean inService;
  private ExecutorService pool;
  private PriorityBlockingQueue<Sku> orderedQueue;
  private Sku searchObject;
  private ConcurrentHashMap<String, Integer> weightsMap;

  public SkuConsumerManager(LinkedBlockingQueue<Sku> skuQueue, PriorityBlockingQueue<Sku> orderedQueue,
      AtomicBoolean inService, ExecutorService pool, Sku searchObject) {
    this.skuQueue = skuQueue;
    this.inService = inService;
    this.pool = pool;
    this.orderedQueue = orderedQueue;
    this.searchObject = searchObject;

    weightsMap = new ConcurrentHashMap<>();
    weightsMap.put("a", 10);
    weightsMap.put("b", 9);
    weightsMap.put("c", 8);
    weightsMap.put("d", 7);
    weightsMap.put("e", 6);
    weightsMap.put("f", 5);
    weightsMap.put("g", 4);
    weightsMap.put("h", 3);
    weightsMap.put("i", 2);
    weightsMap.put("j", 1);
  }

  @Override
  public void run() {
    int sizeOfSkuQueue = skuQueue.size();
    while (this.inService.get() && orderedQueue.size() < sizeOfSkuQueue) {
      Sku sku = skuQueue.poll();
      this.pool.submit(new SkuConsumer(sku, orderedQueue, searchObject, weightsMap));
    }

    for (int i = 0; i < 10; i++) {
      //System.out.println(this.orderedQueue.poll().getName() + ", " + this.orderedQueue.poll().getSimilarity());
    }

    this.inService.set(false);
    this.pool.shutdown();
  }
}