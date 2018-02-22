package wordcounter.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import wordcounter.interfaces.MemoryCacheInterface;
import wordcounter.models.CommonValues;

public class HashMapMemoryCache<K, T> implements MemoryCacheInterface<K, T> {
    private long timeToLive;
    private Map CacheMap;

    protected class CacheObject {
        public long lastAccessed = System.currentTimeMillis();
        public T value;

        protected CacheObject(T value) {
            this.value = value;
        }
    }

    public HashMapMemoryCache(long TimeToLive, final long TimerInterval, int maxItems) {
        this.timeToLive = TimeToLive * CommonValues.MILLISECONDS_TO_SECONDS;

        CacheMap = new HashMap(maxItems);

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

    @Override
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



            deleteKey = new ArrayList<K>((CacheMap.size() / 2) + 1);
            K key = null;
            CacheObject c = null;

            Iterator itr = CacheMap.keySet().iterator();
            while (itr.hasNext()) {
                key = (K) itr.next();

                c = (CacheObject) CacheMap.get(key);

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
