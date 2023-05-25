package com.samnang.project.template.utils;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisUtil.class);

    @Resource
    protected StringRedisTemplate stringRedisTemplate;

    public void setValue(String key, Object value) {
        if (value == null) {
            Object currValue = getValue(key, Object.class);
            if (currValue == null) {
                return;
            }
            try {
                Set<String> keys = this.stringRedisTemplate.keys("*");
                this.stringRedisTemplate.delete(keys);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String valueStr = "";
            if (value instanceof String || value instanceof Integer || value instanceof Boolean) {
                valueStr = String.valueOf(value);
            } else {
                valueStr = JSON.toJSONString(value);
            }
            this.stringRedisTemplate.opsForValue().set(key, valueStr);
        }
    }

    public void setValue(String key, Object value, int expireSeconds) {
        String valueStr = "";
        if (value instanceof String || value instanceof Integer || value instanceof Boolean) {
            valueStr = String.valueOf(value);
        } else {
            valueStr = JSON.toJSONString(value);
        }
        this.stringRedisTemplate.opsForValue().set(key, valueStr, expireSeconds, TimeUnit.SECONDS);
    }

    public long setValueIncrement(String key, long increment) {
        return this.stringRedisTemplate.opsForValue().increment("requests:" + key, increment);
    }

    public <T> T getValue(String key, Class<T> clazz) {
        String valueStr;

        try {
            valueStr = this.stringRedisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            valueStr = this.stringRedisTemplate.opsForValue().get(key);
        }


        if (valueStr == null || valueStr.equals("null")) {
            return null;
        }
        if (clazz.getName().equals("java.lang.String")) {
            return (T) valueStr;
        }
        T t = null;
        if (!StringUtils.startsWithIgnoreCase(valueStr, "{") && !StringUtils.startsWithIgnoreCase(valueStr, "[")) {
            return (T) valueStr;
        }
        try {
            t = JSON.parseObject(valueStr).toJavaObject(clazz);
        } catch (Exception e) {
            LOGGER.debug("Error getting cache", e);
        }
        return t;
    }

    public void del(final String key) {
        Assert.hasText(key, "Key is not empty.");
        this.stringRedisTemplate.execute((RedisCallback<Long>) conn -> {
            RedisSerializer<String> serializer = RedisUtil.this.stringRedisTemplate.getStringSerializer();
            return conn.del(new byte[][]{serializer.serialize(key)});
        });
    }

}