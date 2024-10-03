package pl.edu.mimuw;

import java.util.*;

public class MapArrayList<K,V> implements Map<K,V> {
    private ArrayList<Pair<K,V>> pairs;

    public MapArrayList() {
        pairs = new ArrayList<>();
    }
    @Override
    public void add(K key, V value) {
        for(Pair<K,V> pair1 : pairs) {
            if(pair1.getKey().equals(key)) {
                pair1.setValue(value);
                return;
            }
        }
        pairs.add(new Pair<>(key, value));
    }
    @Override
    public V get(K key) {
        for(Pair<K,V> pair1 : pairs) {
            if(pair1.getKey().equals(key)) {
                return pair1.getValue();
            }
        }
        return null;
    }
    @Override
    public V remove(K key) {
        for(Pair<K,V> pair1 : pairs) {
            if(pair1.getKey().equals(key)) {
                V value = pair1.getValue();
                pair1.setValue(null);
                return value;
            }
        }
        return null;
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
    public boolean containsKey(Object o) {
        return false;
    }

    @Override
    public boolean containsValue(Object o) {
        return false;
    }

    @Override
    public V get(Object o) {
        return null;
    }

    @Override
    public V put(K k, V v) {
        return null;
    }

    @Override
    public V remove(Object o) {
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<K> keySet() {
        return Set.of();
    }

    @Override
    public Collection<V> values() {
        return List.of();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return Set.of();
    }
}
