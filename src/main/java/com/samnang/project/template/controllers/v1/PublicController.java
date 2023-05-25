package com.samnang.project.template.controllers.v1;

import com.samnang.project.template.model.UserLoginRequest;
import com.samnang.project.template.model.request.LoginRequestBody;
import com.samnang.project.template.services.UserService;
import com.samnang.project.template.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping({"/public/"})
public class PublicController {

    @Autowired
    protected UserService userService;


    @PostMapping("login")
    public ApiResult login(HttpServletRequest request, @RequestBody LoginRequestBody requestBody) {
        UserLoginRequest userLoginRequest = UserLoginRequest
                .builder()
                .deviceId(request.getHeader("DeviceId"))
                .username(requestBody.getUsername())
                .password(requestBody.getPassword())
                .build();
        return userService.login(userLoginRequest);
    }

}