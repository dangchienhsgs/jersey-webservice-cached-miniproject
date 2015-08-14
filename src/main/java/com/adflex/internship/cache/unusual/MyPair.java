package com.adflex.internship.cache.unusual;

/**
 * Created by dangchienhsgs on 12/08/2015.
 */



import java.util.Map;

public class MyPair<K, V> implements Map.Entry<K, V> {
    private K left;
    private V right;

    public MyPair(K key, V value)
    {
        this.left = key;
        this.right = value;
    }

    public K getLeft()
    {
        return this.left;
    }

    public V getRight()
    {
        return this.right;
    }

    @Override
    public K getKey() {
        return getLeft();
    }

    @Override
    public V getValue() {
        return null;
    }

    @Override
    public V setValue(V v) {
        return null;
    }

    public K setLeft(K left)
    {
        return this.left = left;
    }

    public V setRight(V right)
    {
        return this.right = right;
    }
}


