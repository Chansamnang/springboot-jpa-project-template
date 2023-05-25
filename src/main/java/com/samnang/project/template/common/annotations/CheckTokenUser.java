package com.samnang.project.template.common.annotations;

import com.samnang.project.template.common.inspect.TokenUserAspect;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.annotation.*;

/**
 * @see TokenUserAspect#checkLogin(ProceedingJoinPoint)
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckTokenUser {

    String userType() default "";

    int[] role() default {};

}
