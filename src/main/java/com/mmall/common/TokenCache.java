package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/12/10.
 */
public class TokenCache {

    public static final String TOKEN_PREFIX = "token_";

    private static final Logger logger = LoggerFactory.getLogger(TokenCache.class);

    //方法initialCapacity()为设定缓存的初始化容量，maximumSize()方法设置缓存的最大容量，如果超出，则调用LRU算法（最小使用算法）移除缓存项
    private static LoadingCache<String,String> localCache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                //load方法的作用：默认的数据加载实现，当调用get取值时，如果key没有对应的值，则调用此方法进行加载
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });

    public static void setKey(String key,String value){
        localCache.put(key,value);
    }

    public static String getKey(String key){
        String value;
        try{
            value = localCache.get(key);
            if (value == null) {
                return null;
            }
            return value;

        }catch (Exception e){
            logger.error("localCache get error",e);
            return null;
        }

    }

}
