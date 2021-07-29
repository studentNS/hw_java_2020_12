package ru.otus.cachehw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author sergey
 * created on 14.12.18.
 */
public class MyCache<K, V> implements HwCache<K, V> {
    //Надо реализовать эти методы
    private final Map<K, V> cache  = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        notifyListeners(key, value, "PUT");
    }

    @Override
    public void remove(K key) {
        var value = cache.remove(key);
        notifyListeners(key, value, "REMOVE");
    }

    @Override
    public V get(K key) {
        var value = cache.get(key);
        notifyListeners(key, value, "GET");
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(K key, V value, String action) {

        for (HwListener listener : listeners){
            try {
                listener.notify(key, value, action);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
