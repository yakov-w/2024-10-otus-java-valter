package ru.otus.cachehw;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {

    // Надо реализовать эти методы
    private static int size = 10;
    private final WeakHashMap<K, V> cache = new WeakHashMap<>();

    @SuppressWarnings("rawtypes")
    private final List<HwListener> listeners = new ArrayList<>(); // TODO надо их где-то дергать

    @Override
    public void put(K key, V value) {
        cache.size();
        cache.put(key, value);
        event(key,value,"put");
    }

    @Override
    public void remove(K key) {
        V v = cache.remove(key);
        event(key, v,"remove");
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
    private void event(K k, V v, String data) {
        for (int i = 0; i < listeners.size(); ++i) {
            try {
                listeners.get(i).notify(k, v, data);
            } catch (Exception ex) {
                throw new RuntimeException();
            }
        }
    }
}
