package com.samnang.project.template.common.annotations;

import com.samnang.project.template.common.enums.LimitType;
import com.samnang.project.template.common.inspect.LimitInterceptor;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.annotation.*;

/**
 * @see LimitInterceptor#interceptor(ProceedingJoinPoint)
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Limit {

    String name() default "";

    String key() default "";

    String prefix() default "";

    int period();

    int count();

    LimitType limitType() default LimitType.CUSTOMER;

}