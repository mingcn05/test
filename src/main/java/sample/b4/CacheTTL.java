package sample.b4;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CacheTTL<K, V> implements Map<K, V> {
    private final Map<K, V> cache;
    private final Map<K, Long> expirationTimes;

    public CacheTTL() {
        this.cache = new HashMap<>();
        this.expirationTimes = new HashMap<>();
    }

    public CacheTTL(int n, int m) {
        this();
        // Set TTL for each element to m seconds
        Thread cleanerThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(n * 1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                long currTime = System.currentTimeMillis();
                synchronized (this) {
                    expirationTimes.entrySet().removeIf(entry -> entry.getValue() < currTime);
                }
            }
        });
        cleanerThread.setDaemon(true);
        cleanerThread.start();
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public V get(Object key) {
        synchronized (this) {
            V value = cache.get(key);
            if (value != null) {
                expirationTimes.put((K) key, System.currentTimeMillis());
            }
            return value;
        }
    }

    @Override
    public V put(K key, V value) {
        synchronized (this) {
            expirationTimes.put(key, System.currentTimeMillis());
            return cache.put(key, value);
        }
    }

    @Override
    public V remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    // Implement other methods in the Map interface
    //...
}
