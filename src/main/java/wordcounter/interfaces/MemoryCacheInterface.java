package wordcounter.interfaces;

public interface MemoryCacheInterface<K, T> {
    void put(K key, T value);
    T get(K key);
    void remove(K key);
    void cleanup();

}
