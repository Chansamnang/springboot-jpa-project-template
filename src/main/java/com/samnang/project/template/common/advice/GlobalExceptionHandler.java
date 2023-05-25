package com.samnang.project.template.common.advice;


import com.samnang.project.template.common.exception.*;
import com.samnang.project.template.configs.ApplicationRunnerConfig;
import com.samnang.project.template.utils.ApiResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationRunnerConfig.class);

    private static final Integer CODE = 1000;

    @ExceptionHandler({TokenErrorException.class})
    public ApiResult handler(HttpServletRequest req, TokenErrorException e) {
        LOGGER.error("Request: " + req.getRequestURL() + " raised " + e);
        String msg = StringUtils.isBlank(e.getMessage()) ? "Abnormal user status" : e.getMessage();
        ApiResult backResult = ApiResult.error(msg, CODE);
        return backResult;
    }

    @ExceptionHandler({ApiException.class})
    public ApiResult handler(HttpServletRequest req, ApiException e) {
        LOGGER.error("Request: " + req.getRequestURL() + " raised " + e);
        ApiResult backResult = ApiResult.error(e.getMessage(), CODE);
        return backResult;
    }

    @ExceptionHandler({FormFieldErrorException.class})
    public ApiResult handler(HttpServletRequest req, FormFieldErrorException e) {
        LOGGER.error("Request: " + req.getRequestURL() + " raised " + e);
        ApiResult backResult = ApiResult.error(e.getMessage(), CODE);
        return backResult;
    }

    @ExceptionHandler({ImageVerifyCodeException.class})
    public ApiResult handler(HttpServletRequest req, ImageVerifyCodeException e) {
        LOGGER.error("Request: " + req.getRequestURL() + " raised " + e);
        ApiResult backResult = ApiResult.error(e.getMessage(), CODE);
        return backResult;
    }

    @ExceptionHandler({LimitException.class})
    public ApiResult handler(HttpServletRequest req, LimitException e) {
        LOGGER.error("Request: " + req.getRequestURL() + " raised " + e);
        ApiResult backResult = ApiResult.error(e.getMessage(), CODE);
        return backResult;
    }

    @ExceptionHandler({MobileVerifyCodeException.class})
    public ApiResult handler(HttpServletRequest req, MobileVerifyCodeException e) {
        LOGGER.error("Request: " + req.getRequestURL() + " raised " + e);
        ApiResult backResult = ApiResult.error(e.getMessage(), CODE);
        return backResult;
    }

    @ExceptionHandler(value = Exception.class)
    public ApiResult Erection(HttpServletRequest req, Exception e) {
        LOGGER.error("Request: " + req.getRequestURL() + " raised " + e);
        e.printStackTrace();
        return ApiResult.error("Exception", CODE);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ApiResult RuntimeException(HttpServletRequest req, RuntimeException e) {
        LOGGER.error("Request: " + req.getRequestURL() + " raised " + e);
        e.printStackTrace();
        return ApiResult.error("RuntimeException", CODE);
    }

    @ExceptionHandler({java.sql.SQLException.class})
    public ApiResult mysqlDataTruncationHandle(HttpServletRequest req, java.sql.SQLException ex) {
        LOGGER.error("Request: " + req.getRequestURL() + " raised " + ex);
        LOGGER.error("mysql error");
        return ApiResult.error("sql error", CODE);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ApiResult ExceptionHandler(HttpServletRequest req, DataIntegrityViolationException ex) {
        LOGGER.error("Request: " + req.getRequestURL() + " raised " + ex);
        LOGGER.error("SQL error");
        String message = ex.getCause().getCause().getLocalizedMessage();
        if (message.contains(" for key"))
            message = message.split(" for key")[0];
        return ApiResult.error(message, CODE);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ApiResult HandleMethodArgumentNotValidException(HttpServletRequest req, MethodArgumentNotValidException ex) {
        LOGGER.error("Request: " + req.getRequestURL() + " raised " + ex);
        List<ObjectError> errors = ex.getAllErrors();
        List<String> errorMessages = new ArrayList<>();
        for (ObjectError error : errors) {
            String message = error.getDefaultMessage();
            errorMessages.add(message);
        }
        LOGGER.error("Validation failed " + errorMessages);
        return ApiResult.error("Validation failed", CODE);
    }

    @ExceptionHandler({InvalidDataAccessApiUsageException.class})
    public ApiResult InvalidDataAccessApiUsageExceptionHandler(HttpServletRequest req, InvalidDataAccessApiUsageException ex) {
        LOGGER.error("Request: " + req.getRequestURL() + " raised " + ex);
        LOGGER.error("mysql error");
        return ApiResult.error(ex.getCause().getMessage(), 1);
    }

    @ExceptionHandler({EmptyResultDataAccessException.class})
    public ApiResult EmptyResultDataAccessExceptionHandler(HttpServletRequest req, EmptyResultDataAccessException ex) {
        LOGGER.error("Request: " + req.getRequestURL() + " raised " + ex);
        LOGGER.error("mysql error");
        return ApiResult.error("Id not exists", 1);
    }

    @ExceptionHandler({MissingRequestHeaderException.class})
    public ApiResult MissingRequestHeaderExceptionHandler(HttpServletRequest req, MissingRequestHeaderException ex) {
        LOGGER.error("Request: " + req.getRequestURL() + " raised " + ex);
        LOGGER.error("mysql error");
        return ApiResult.error("Missing required header parameter!", 1);
    }


}