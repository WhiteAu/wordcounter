package wordcounter.bo;


import java.util.ArrayList;
import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.map.LRUMap;

import wordcounter.models.CommonValues;

/*
* Based on code from https://crunchify.com/how-to-create-a-simple-in-memory-cache-in-java-lightweight-cache/
*/

public class MemoryCache<K, T> {
    private long timeToLive;
    private LRUMap CacheMap;

    protected class CacheObject {
        public long lastAccessed = System.currentTimeMillis();
        public T value;

        protected CacheObject(T value) {
            this.value = value;
        }
    }

    public MemoryCache(long TimeToLive, final long TimerInterval, int maxItems) {
        this.timeToLive = TimeToLive * CommonValues.MILLISECONDS_TO_SECONDS;

        CacheMap = new LRUMap(maxItems);

        if (timeToLive > 0 && TimerInterval > 0) {

            Thread t = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(TimerInterval * CommonValues.MILLISECONDS_TO_SECONDS);
                        } catch (InterruptedException ex) {
                        }
                        cleanup();
                    }
                }
            });

            t.setDaemon(true);
            t.start();
        }
    }

    public void put(K key, T value) {
        synchronized (CacheMap) {
            CacheMap.put(key, new CacheObject(value));
        }
    }

    @SuppressWarnings("unchecked")
    public T get(K key) {
        synchronized (CacheMap) {
            CacheObject c = (CacheObject) CacheMap.get(key);

            if (c == null)
                return null;
            else {
                System.out.println(String.format("A cache hit was found for %s", key.toString()));
                c.lastAccessed = System.currentTimeMillis();
                return c.value;
            }
        }
    }

    public void remove(K key) {
        synchronized (CacheMap) {
            CacheMap.remove(key);
        }
    }

    public int size() {
        synchronized (CacheMap) {
            return CacheMap.size();
        }
    }

    @SuppressWarnings("unchecked")
    public void cleanup() {

        long now = System.currentTimeMillis();
        ArrayList<K> deleteKey = null;

        synchronized (CacheMap) {
            MapIterator itr = CacheMap.mapIterator();

            deleteKey = new ArrayList<K>((CacheMap.size() / 2) + 1);
            K key = null;
            CacheObject c = null;

            while (itr.hasNext()) {
                key = (K) itr.next();
                c = (CacheObject) itr.getValue();

                if (c != null && (now > (timeToLive + c.lastAccessed))) {
                    deleteKey.add(key);
                }
            }
        }

        for (K key : deleteKey) {
            synchronized (CacheMap) {
                CacheMap.remove(key);
            }

            Thread.yield();
        }
    }
}
