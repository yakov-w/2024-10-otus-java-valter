package ru.otus.cachehw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.stream.IntStream;

public class MyCache<K, V> implements HwCache<K, V> {

    // Надо реализовать эти методы
    private final Map<K, V> cache = new WeakHashMap<>();

    @SuppressWarnings("rawtypes")
    private final List<HwListener> listeners = new ArrayList<>(); // TODO надо их где-то дергать

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        fireEvent(key, value, "put");
    }

    @Override
    public void remove(K key) {
        V v = cache.remove(key);
        fireEvent(key, v, "remove");
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    @SuppressWarnings("unchecked")
    private void fireEvent(K k, V v, String data) {
        IntStream.range(0, listeners.size()).forEach(i -> listeners.get(i).notify(k, v, data));
    }
}
