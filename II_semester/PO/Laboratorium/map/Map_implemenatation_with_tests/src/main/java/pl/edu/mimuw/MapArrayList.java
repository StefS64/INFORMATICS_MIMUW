package pl.edu.mimuw;

import java.util.*;

public class MapArrayList<K,V> implements Map<K,V> {
    private final List<Pair<K,V>> pairs;
    public MapArrayList() {
        pairs = new ArrayList<>();
    }

    @Override
    public int size() {
        return pairs.size();
    }

    @Override
    public boolean isEmpty() {
        return pairs.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        for(Pair<K,V> pair : pairs) {
            if(pair.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        for(Pair<K,V> pair : pairs) {
            if(pair.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        for(Pair<K,V> pair : pairs) {
            if(pair.getKey().equals(key)) {
                return pair.getValue();
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        V oldValue = null;
        for(Pair<K,V> pair : pairs) {
            if(pair.getKey().equals(key)) {
                oldValue = pair.getValue();
                pair.setValue(value);
            }
        }
        pairs.add(new Pair<K,V>(key, value));
        return oldValue;
    }

    @Override
    public V remove(Object key) {
        V oldValue = null;
        for(Pair<K,V> pair : pairs) {
            if(pair.getKey().equals(key)) {
                oldValue = pair.getValue();
                pairs.remove(pair);
            }
        }
        return oldValue;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for(Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        pairs.clear();
    }

    @Override
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();
        for(Pair<K,V> pair : pairs) {
            keySet.add(pair.getKey());
        }
        return keySet;
    }

    @Override
    public Collection<V> values() {
        Set<V> values = new HashSet<>();
        for(Pair<K,V> pair : pairs) {
            values.add(pair.getValue());
        }
        return values;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> set = new HashSet<>();
        for(Pair<K,V> pair : pairs) {
            Entry<K, V> entry = new AbstractMap.SimpleImmutableEntry<>(pair.getKey(), pair.getValue());
            set.add(entry);
        }
        return set;
    }
    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(!(obj instanceof MapArrayList)) {
            return false;
        }
        return this.entrySet().equals(((MapArrayList) obj).entrySet());
    }
    @Override
    public int hashCode() {
        return pairs.hashCode();
    }
}
