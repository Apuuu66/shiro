package com.kevin.cache;


import com.kevin.util.JedisUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: kevin
 * Date: 2018/6/21
 */
@Component
public class RedisCache<K, V> implements Cache<K, V> {
    @Autowired
    private JedisUtils jedisUtils;

    private final String SHIRO_CACHE_PREFIX = "shiro-cache:";

    private byte[] getKey(K k) {
        if (k instanceof String) {
            return (SHIRO_CACHE_PREFIX + k).getBytes();
        }
        return SerializationUtils.serialize(k);
    }

    @Override
    public V get(K k) throws CacheException {
        System.out.println("从redis缓存中获取");
        byte[] value = jedisUtils.get(getKey(k));
        if (value != null) {
            return (V) SerializationUtils.deserialize(value);
        }
        return null;
    }

    @Override
    public V put(K k, V v) throws CacheException {
        byte[] key = getKey(k);
        byte[] value = SerializationUtils.serialize(v);
        jedisUtils.set(key, value);
        jedisUtils.expire(key, 600);
        return v;
    }

    @Override
    public V remove(K k) throws CacheException {
        byte[] key = getKey(k);
        byte[] value = jedisUtils.get(key);
        jedisUtils.del(key);
        if (value != null) {
            return (V) value;
        }
        return null;
    }

    @Override
    public void clear() throws CacheException {
        Set<byte[]> keys = jedisUtils.keys(SHIRO_CACHE_PREFIX);
        Iterator<byte[]> iterator = keys.iterator();
        if (iterator.hasNext()) {
            jedisUtils.del(iterator.next());
        }
    }

    @Override
    public int size() {
        return jedisUtils.keys(SHIRO_CACHE_PREFIX).size();
    }

    @Override
    public Set<K> keys() {
        return (Set<K>) jedisUtils.keys(SHIRO_CACHE_PREFIX);
    }

    @Override
    public Collection<V> values() {
        Set<byte[]> keys = jedisUtils.keys(SHIRO_CACHE_PREFIX);
        HashSet<V> values = new HashSet<>();
        if (CollectionUtils.isEmpty(keys)) {
            return values;
        }
        for (byte[] key : keys) {
            V value = (V) SerializationUtils.deserialize(jedisUtils.get(key));
            values.add(value);
        }
        return values;
    }
}
