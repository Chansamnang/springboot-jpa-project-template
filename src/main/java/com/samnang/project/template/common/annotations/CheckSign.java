package com.samnang.project.template.common.annotations;

import com.samnang.project.template.common.inspect.CheckSignAspect;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.annotation.*;

/**
 * @see CheckSignAspect#checkLogin(ProceedingJoinPoint)
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckSign {
}