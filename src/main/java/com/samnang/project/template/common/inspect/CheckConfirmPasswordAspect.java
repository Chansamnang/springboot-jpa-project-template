package com.samnang.project.template.common.inspect;


import com.samnang.project.template.common.exception.ApiException;
import com.samnang.project.template.common.exception.TokenErrorException;
import com.samnang.project.template.db.entity.User;
import com.samnang.project.template.model.CurrentUserInfo;
import com.samnang.project.template.services.TokenValidateService;
import com.samnang.project.template.services.UserService;
import com.samnang.project.template.utils.ApiResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class CheckConfirmPasswordAspect {

    @Autowired
    protected UserService userService;
    @Autowired
    protected TokenValidateService tokenValidate;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Around("@annotation(com.samnang.project.template.common.annotations.CheckConfirmation)")
    public Object checkConfirmPassword(ProceedingJoinPoint point) throws Throwable {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String pwd = request.getHeader("ConfirmPassword");
        if (!StringUtils.hasLength(pwd)) {
            throw new TokenErrorException("ConfirmPassword most not empty!");
        }
        CurrentUserInfo currentUserInfo = this.tokenValidate.checkToken(request);
        if (currentUserInfo == null) {
            return ApiResult.error("token failed", 401);
        }
        ApiResult<User> resultUser = userService.getUserByUsername(currentUserInfo.getUsername());
        if (resultUser.isFail()) {
            throw new ApiException(resultUser.getMessage());
        }
        User user = resultUser.getData();
        if (!this.passwordEncoder.matches(pwd, user.getPassword())) {
            return ApiResult.error("Incorrect password");
        }
        return point.proceed();
    }
}