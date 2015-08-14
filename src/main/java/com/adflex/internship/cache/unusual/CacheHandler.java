package com.adflex.internship.cache.unusual;

import java.util.Collections;
import java.util.HashMap;

/**
 * Created by dangchienhsgs on 12/08/2015.
 */
public class CacheHandler<K, V> {
    private HashMap<K, MyPair<Integer, V>> cacheMap;

    public static final int MAX_ELEMENTS = 100000;

    public CacheHandler() {
        this.cacheMap = new HashMap<>();
    }

    public V get(K key) {
        if (cacheMap.containsKey(key)) {
            cacheMap.get(key).setLeft(cacheMap.get(key).getLeft() + 1);
            return cacheMap.get(key).getValue();
        } else {
            return null;
        }
    }

    public void put(K key, V value) {
        if (cacheMap.size() == MAX_ELEMENTS) {
            // get max key
            K maxKey = Collections.min(
                    cacheMap.entrySet(),
                    (entry1, entry2) -> entry1.getValue().getLeft() < entry2.getValue().getLeft() ? 1 : -1).getKey();

            remove(maxKey);

            cacheMap.put(key, new MyPair(1, value));
        } else {
            cacheMap.put(key, new MyPair(1, value));
        }
    }

    public void remove(K key) {
        if (cacheMap.containsKey(key)) {
            cacheMap.remove(key);
        }
    }
}
