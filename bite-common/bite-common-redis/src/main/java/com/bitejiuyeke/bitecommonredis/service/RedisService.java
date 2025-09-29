package com.bitejiuyeke.bitecommonredis.service;


import com.bitejiuyeke.bitecommoncore.utils.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.lang.ref.Reference;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis 操作工具类
 */
@SuppressWarnings(value = {"unchecked", "rawtypes"})
@Component
public class RedisService {

    /**
     * redisTemplate
     */
    @Autowired
    public RedisTemplate redisTemplate;


    /**
     * 设置过期时间
     * @param key
     * @param timeout
     * @return
     */
    public boolean expire(final String key, final long timeout) {
        return expire(key, timeout,  TimeUnit.SECONDS);
    }

    /**
     * 设置过期时间
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    public boolean expire(final String key, final long timeout,
                          final TimeUnit  unit) {
        return redisTemplate.expire(key, timeout,  unit);
    }

    /**
     * 获取有效时间
     * @param key
     * @return
     */
    public long getExpire(final String key){
        return redisTemplate.getExpire(key);
    }

    /**
     * 判断key是否存在
     * @param key
     * @return 存在返回true, 不存在返回false
     */
    public boolean hasKey( String key){
        return redisTemplate.hasKey(key);
    }

    /**
     * 查找匹配的键
     * @param pattern
     * @return
     */
    public Collection<String> keys(final String pattern){
        return redisTemplate.keys(pattern);
    }

    /**
     * 重命名key
     * @param oldKey
     * @param newKey
     */
    public void renameKey(String oldKey, String newKey) {
    	redisTemplate.rename(oldKey, newKey);
    }

    /**
     * 删除key
     * @param key
     * @return
     */
    public boolean deleteObject(final String key){
        return redisTemplate.delete(key);
    }

    /**
     * 删除多个key
     * @param collection
     * @return
     */
    public boolean deleteObject(final Collection collection){
        return redisTemplate.delete(collection) > 0;
    }


    /**
     * 缓存String类等
     * @param key
     * @param value
     */
    public <T> void setCacheObject(final String key, final T value){
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存String类等,并设置有效时间
     * @param key
     * @param value
     * @param timeout
     * @param unit
     * @param <T>
     */
    public <T> void setCacheObject(final String key, final T value,
                                   final Long timeout, final TimeUnit unit){
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 缓存String类等,并设置有效时间,若不存在则缓存
     * @param key
     * @param value
     * @param timeout
     * @param unit
     * @param <T>
     * @return
     */
    public <T> boolean setCacheObjectIfAbsent(final String key, final T value,
                                   final Long timeout, final TimeUnit unit){
        return  redisTemplate.opsForValue().setIfAbsent(key, value, timeout, unit);
    }

    /**
     * 获取缓存String类等(将缓存的数据类型反序列化成指定的数据类型返回)
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getCacheObject(final String key, Class<T> clazz){
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        T t = operation.get(key);
        if(t == null){
            return null;
        }
        return JsonUtil.string2Obj(JsonUtil.obj2String(t), clazz);
    }

    /**
     * 获取缓存String类等(将缓存的数据类型反序列化成指定的数据类型返回,支持复杂泛型)
     * @param key
     * @param reference
     * @param <T>
     * @return
     */
    public <T> T getCacheObject(final String key, TypeReference<T> reference){
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        T t = operation.get(key);
        if(t == null){
            return null;
        }
        return JsonUtil.string2Obj(JsonUtil.obj2String(t), reference);
    }

    /**
     * 缓存List类等
     * @param key
     * @param dataList
     * @param <T>
     * @return
     */
    public <T> long setCacheList(final String key, final List<T> dataList){
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null ? 0 : count;
    }

    /**
     * 从List结构左侧插⼊数据（头插、⼊队）
     * @param key key
     * @param value 缓存的对象
     * @param <T> 值类型
     */
    public <T> void leftPushForList(String key, final T value){
        redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 从List结构右侧插⼊数据（尾插、⼊队）
     * @param key key
     * @param value 缓存的对象
     * @param <T> 值类型
     */
    public <T> void rightPushForList(String key, final T value){
        redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 从List结构左侧弹出数据（头删、出队）
     * @param key
     */
    public void leftPopForList(String key){
    	redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 删除右侧第一个数据
     * @param key
     */
    public void rightPopForList(String key){
    	redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 删除List第一个匹配的元素
     * @param key
     * @param value
     */
    public <T> void removeForList(final String key, T value){
    	redisTemplate.opsForList().remove(key, 1L, value);
    }

    /**
     * 删除List中所有匹配的元素
     * @param key
     * @param value
     * @param <T>
     */
    public <T> void removeAllForList(final String key, T value){
        redisTemplate.opsForList().remove(key, 0, value);
    }

    /**
     * 删除Key中所有元素
     * @param key
     * @param <T>
     */
    public <T> void removeAllForList(final String key){
        redisTemplate.opsForList().trim(key, -1, 0);
    }

    /**
     * 修改List中指定索引位置的元素
     * @param key
     * @param index
     * @param value
     * @param <T>
     */
    public <T> void setElementAtIndex(final String key, int index, T value){
        redisTemplate.opsForList().set(key, index, value);
    }

    /**
     * 获取List类等
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> getCacheList(final String key, Class<T> clazz){
        List list = redisTemplate.opsForList().range(key, 0, -1);
        return JsonUtil.string2List(JsonUtil.obj2String(list), clazz);
    }

    /**
     * 获取List类等
     * @param key
     * @param reference
     * @param <T>
     * @return
     */
    public <T> List<T> getCacheList(final String key, TypeReference<List<T>>  reference){
        List list = redisTemplate.opsForList().range(key, 0, -1);
        List<T> res = JsonUtil.string2Obj(JsonUtil.obj2String(list), reference);
        return res;
    }

    /**
     * 根据范围获取List类等
     * @param key
     * @param start
     * @param end
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> getCacheListByRange(final String key, long start,
                                           long end, Class<T> clazz){
        List range = redisTemplate.opsForList().range(key, start, end);
        return JsonUtil.string2List(JsonUtil.obj2String(range), clazz);
    }

    /**
     * 获取List类等长度
     * @param key
     * @return
     */
    public long getCacheListSize(final String key){
        Long size = redisTemplate.opsForList().size(key);
        return size == null ? 0L : size;
    }


    /**
     * 缓存set类
     * @param key
     * @param member
     */
    public void addMember(final String key, Object... member){
        redisTemplate.opsForSet().add(key, member);
    }

    /**
     * 删除set类
     * @param key
     * @param member
     */
    public void deleteMember(final String key, Object... member){
        redisTemplate.opsForSet().remove(key, member);
    }

    /**
     * 获取缓存set类
     * @param key
     * @param reference
     * @param <T>
     * @return
     */
    public <T> Set<T> getCacheSet(final String key, TypeReference<Set<T>> reference){
        Set data = redisTemplate.opsForSet().members(key);
        return JsonUtil.string2Obj(JsonUtil.obj2String(data), reference);
    }

    /**
     * 添加元素
     * @param key
     * @param value
     * @param seqNo
     */
    public void addMemberZSet(String key, Object value, double seqNo){
        redisTemplate.opsForZSet().add(key, value, seqNo);
    }

    /**
     * 删除元素
     * @param key
     * @param value
     */
    public void deleteMemberZSet(String key, Object value){
        redisTemplate.opsForZSet().remove(key, value);
    }

    /**
     * 删除指定区域分值
     * @param key
     * @param minScore
     * @param maxScore
     */
    public void removeZSetByScore(final String key, double minScore,
                                  double maxScore){
        redisTemplate.opsForZSet().removeRangeByScore(key, minScore, maxScore);
    }

    /**
     * 获取有序数据集合(支持复杂嵌套)
     * @param key
     * @param reference
     * @param <T>
     * @return
     */
    public <T> Set<T> getCacheZSet(final String key, TypeReference<LinkedHashSet<T>> reference){
        Set data = redisTemplate.opsForZSet().range(key, 0, -1);
        return JsonUtil.string2Obj(JsonUtil.obj2String(data), reference);
    }

    /**
     * 获取有序数据集合(倒序)
     * @param key
     * @param reference
     * @param <T>
     * @return
     */
    public <T> Set<T> getCacheZSetDesc(final String key, TypeReference<LinkedHashSet<T>> reference){
        Set data = redisTemplate.opsForZSet().reverseRange(key, 0, -1);
        return JsonUtil.string2Obj(JsonUtil.obj2String(data), reference);
    }


    /**
     * 缓存Map类
     * @param key
     * @param map
     * @param <T>
     */
    public <T> void setCacheMap(final String key, final Map<String, T> map){
        if(map != null){
            redisTemplate.opsForHash().putAll(key, map);
        }
    }

    /**
     * 往Hash中缓存单个数据
     * @param key
     * @param hashKey
     * @param value
     * @param <T>
     */
    public <T> void setCacheMapValue(final String key,
                                     final String hashKey,
                                     final T value){
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 删除Hash中数据
     * @param key
     * @param hashKey
     * @return
     */
    public boolean deleteCacheMapValue(final String key, final String hashKey){
        return redisTemplate.opsForHash().delete(key, hashKey) > 0;
    }

    /**
     * 获取Hash类(支持复杂嵌套)
     * @param key
     * @param reference
     * @param <T>
     * @return
     */
    public <T> Map<String, T> getCacheMap(final String key, TypeReference<Map<String, T>>  reference){
        Map data = redisTemplate.opsForHash().entries(key);
        return JsonUtil.string2Obj(JsonUtil.obj2String(data), reference);
    }

    /**
     * 获取Hash类中数据
     * @param key
     * @param hashKey
     * @param <T>
     * @return
     */
    public <T> T getCacheMapValue(final String key, final String hashKey){
        HashOperations<String, String, T> opsForHash =
                redisTemplate.opsForHash();
                return opsForHash.get(key, hashKey);
    }


    /**
     * 获取多个Hash类中数据
     * @param key
     * @param hKeys
     * @param <T>
     * @return
     */
    public <T> List<T> getMultiCacheMapValue(final String key, final
    Collection<Object> hKeys,TypeReference<List<T>> typeReference) {
        List data = redisTemplate.opsForHash().multiGet(key, hKeys);
        return JsonUtil.string2Obj(JsonUtil.obj2String(data), typeReference);
    }
}
