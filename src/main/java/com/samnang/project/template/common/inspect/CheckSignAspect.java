package com.samnang.project.template.common.inspect;

import com.samnang.project.template.common.exception.TokenErrorException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class CheckSignAspect {

    @Around("@annotation(com.samnang.project.template.common.annotations.CheckSign)")
    public Object checkLogin(ProceedingJoinPoint point) throws Throwable {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        if (!StringUtils.hasLength(request.getHeader("DeviceId"))) {
            throw new TokenErrorException("DeviceId not find!");
        }
        return point.proceed();
    }
}