package com.samnang.project.template.services;


import com.samnang.project.template.model.UserLoginRequest;
import com.samnang.project.template.utils.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class UserLoginLogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginLogService.class);


    @Value("${tsca.limit-daliy-login-max-error-nums:30}")
    protected int limitDaliyLoginMaxErrorNums = 30;


    public ApiResult checkErrorNums(UserLoginRequest request) {
        return ApiResult.success("Not yet implement: checkErrorNums");
    }

}