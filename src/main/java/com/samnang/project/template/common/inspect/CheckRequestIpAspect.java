package com.samnang.project.template.common.inspect;

import com.samnang.project.template.common.IpUtils;
import com.samnang.project.template.common.exception.ApiException;
import com.samnang.project.template.db.entity.BindIpAddress;
import com.samnang.project.template.db.repository.BindIpRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CheckRequestIpAspect {

    @Autowired
    protected BindIpRepository bindIpRepository;

    @Around("@annotation(com.samnang.project.template.common.annotations.CheckRequestIp)")
    public Object checkIp(ProceedingJoinPoint point) throws Throwable {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase("ip");
        String ip = IpUtils.getIpAddress();
        BindIpAddress example = BindIpAddress.builder()
                .ip(ip)
                .build();
        Example<BindIpAddress> ex = Example.of(example, matcher);
        if (!bindIpRepository.exists(ex))
            throw new ApiException("Unauthorized IP: " + ip);
        return point.proceed();
    }
}