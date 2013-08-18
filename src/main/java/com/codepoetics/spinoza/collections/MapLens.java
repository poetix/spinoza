package com.codepoetics.spinoza.collections;

import java.util.Map;
import java.util.Map.Entry;

import com.codepoetics.spinoza.api.PathDescribingLens;
import com.codepoetics.spinoza.paths.api.PathDescriber;
import com.google.common.collect.ImmutableMap;

public class MapLens<K, V> implements  PathDescribingLens<Map<? extends K, ? extends V>, V> {

    public static <K, V> MapLens<K, V> atKey(K key) {
        return new MapLens<K, V>(key);
    }
    
    private final K key;
    
    private MapLens(K key) {
        this.key = key;
    }

    @Override
    public V get(Map<? extends K, ? extends V> target) {
        return target.get(key);
    }

    @Override
    public Map<? extends K, ? extends V> update(
            Map<? extends K, ? extends V> target, V newValue) {
        ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
        for (Entry<? extends K, ? extends V> original : target.entrySet()) {
            if (original.getKey().equals(key)) {
                builder.put(key, newValue);
            } else {
                builder.put(original);
            }
        }
        return builder.build();
    }

    @Override
    public void describeTo(PathDescriber describer) {
        describer.index(key);
    }
}
