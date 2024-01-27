package org.krzywanski.table.utils;

import java.util.LinkedHashMap;
import java.util.List;

public class ListSupportLinkedHashMap<K,V> extends LinkedHashMap<K, V> {

    final List<K> listToSupport;
    public ListSupportLinkedHashMap(List<K>listToSupport) {
        super();
        this.listToSupport = listToSupport;
    }

    @Override
    public V put(K key, V value) {
        listToSupport.add(key);
        return super.put(key, value);
    }
}
