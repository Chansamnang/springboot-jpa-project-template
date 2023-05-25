package com.samnang.project.template.common.annotations;

import com.samnang.project.template.common.inspect.CheckRequestIpAspect;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.annotation.*;

/**
 * @see CheckRequestIpAspect#checkIp(ProceedingJoinPoint)
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckRequestIp {
}