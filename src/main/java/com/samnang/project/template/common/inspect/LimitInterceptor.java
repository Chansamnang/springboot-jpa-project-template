package com.samnang.project.template.common.inspect;


import com.google.common.collect.ImmutableList;
import com.samnang.project.template.common.annotations.Limit;
import com.samnang.project.template.common.enums.LimitType;
import com.samnang.project.template.common.exception.LimitException;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collections;

@Aspect
@Configuration
public class LimitInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LimitInterceptor.class);

    private final String REDIS_SCRIPT = buildLuaScript();

    @Autowired(required = false)
    protected RedisTemplate<String, Serializable> redisTemplate;

    @Around("execution(public * *(..)) && @annotation(com.samnang.project.template.common.annotations.Limit)")
    public Object interceptor(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Limit limitAnno = method.getAnnotation(Limit.class);
        LimitType limitType = limitAnno.limitType();
        String key = null;
        int limitPeriod = limitAnno.period();
        int limitCount = limitAnno.count();
        switch (limitType) {
            case IP:
                key = getIpAddress();
                break;
            case CUSTOMER:
                key = limitAnno.key();
                break;
        }

        ImmutableList<String> keys = ImmutableList.of(StringUtils.join(Collections.singleton(limitAnno.prefix()), key));
        try {
            DefaultRedisScript<Number> defaultRedisScript = new DefaultRedisScript<>(this.REDIS_SCRIPT, Number.class);
            Number count = this.redisTemplate.execute(defaultRedisScript, keys, limitCount, limitPeriod);
            if (count != null && count.intValue() <= limitCount) {
                return pjp.proceed();
            }
            // 访问信息: 当前-{},单个-{},访问限制是: {}次/{}秒 = Access information: current-{}, single-{}, access limit is: {} times/{} seconds
            LOGGER.warn("访问信息: 当前-{},单个-{},访问限制是: {}次/{}秒", key, limitType, limitCount, limitPeriod);
            // 您的访问频率过高,请稍后再访问 = Your visit frequency is too high, please visit later
            throw new LimitException("您的访问频率过高,请稍后再访问");
        } catch (Throwable e) {
            if (e instanceof LimitException) {
                throw new LimitException(e.getMessage());
            } else {
                e.printStackTrace();
                // 限流处理 其他异常 = Current limit processing Other exceptions
                throw new LimitException("限流处理 其他异常");
            }
        }
    }

    private String buildLuaScript() {
        String lua = "local c" +
                "\nc = redis.call('get', KEYS[1])" +
                "\nif c and tonumber(c) > tonumber(ARGV[1]) then" +
                "\nreturn c;" +
                "\nend" +
                "\nc = redis.call('incr', KEYS[1])" +
                "\nif tonumber(c) == 1 then" +
                "\nredis.call('expire', KEYS[1], ARGV[2])" +
                "\nend" +
                "\nreturn c;";
        return lua;
    }

    public String getIpAddress() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}