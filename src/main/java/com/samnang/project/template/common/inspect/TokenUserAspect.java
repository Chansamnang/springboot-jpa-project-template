package com.samnang.project.template.common.inspect;

import com.samnang.project.template.common.annotations.CheckTokenUser;
import com.samnang.project.template.model.CurrentUserInfo;
import com.samnang.project.template.services.TokenValidateService;
import com.samnang.project.template.utils.ApiResult;
import com.samnang.project.template.utils.UserAuthUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class TokenUserAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenUserAspect.class);

    @Autowired
    protected TokenValidateService tokenValidate;

    @Autowired
    protected UserAuthUtil userAuthUtil;

    @Around("@annotation(com.samnang.project.template.common.annotations.CheckTokenUser)")
    public Object checkLogin(ProceedingJoinPoint point) throws Throwable {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        CurrentUserInfo currentUserInfo = this.tokenValidate.checkToken(request);
        if (currentUserInfo == null) {
            LOGGER.debug("Failed to obtain user token");
            return ApiResult.error("token failed", 401);
        }
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        CheckTokenUser checkTokenUser = method.getAnnotation(CheckTokenUser.class);
        this.userAuthUtil.setTokenUser(currentUserInfo);
        Long roleId = currentUserInfo.getRoleId();
        if (checkTokenUser.role().length > 0 && !Arrays.asList(checkTokenUser.role()).contains(roleId.toString())) {
            return ApiResult.error("Role mismatch", 403);
        }
        return point.proceed();
    }

}
