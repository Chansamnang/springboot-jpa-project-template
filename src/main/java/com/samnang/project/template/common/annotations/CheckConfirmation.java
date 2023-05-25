package com.samnang.project.template.common.annotations;

import com.samnang.project.template.common.inspect.CheckConfirmPasswordAspect;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.annotation.*;

/**
 * @see CheckConfirmPasswordAspect#checkConfirmPassword(ProceedingJoinPoint)
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckConfirmation {
}