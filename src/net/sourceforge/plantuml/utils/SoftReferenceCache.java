package net.sourceforge.plantuml.utils;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SoftReferenceCache<K,V> {
	
	private final Map<K, SoftReference<V>> map = new ConcurrentHashMap<>();

	public V get(K key) {
		final SoftReference<V> ref = map.get(key);
		return ref == null ? null : ref.get();
	}

	public void put(K key, V value) {
		map.put(key, new SoftReference<>(value));
	}
}
