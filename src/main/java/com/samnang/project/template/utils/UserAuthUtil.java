package com.samnang.project.template.utils;

import com.samnang.project.template.model.CurrentUserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class UserAuthUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthUtil.class);

    protected String token;

    protected String deviceId;

    @Builder.Default
    protected String tokenRequestAttribute = "Authorization";

    public static UserAuthUtil loadHeaderData(HttpServletRequest request) {
        return builder()
                .deviceId(request.getHeader("DeviceId"))
                .tokenRequestAttribute("Authorization")
                .build();
    }

    public CurrentUserInfo getTokenUser() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        Object CurrentUserInfo = request.getAttribute(getTokenRequestAttribute());
        if (CurrentUserInfo == null) {
            throw new SecurityException("The current operation is not a logged-in user operation");
        }
        return (CurrentUserInfo) CurrentUserInfo;
    }

    public void setTokenUser(CurrentUserInfo jwtToken) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        request.setAttribute(getTokenRequestAttribute(), jwtToken);
    }


}