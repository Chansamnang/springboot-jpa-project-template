package com.samnang.project.template.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ApiResult<T> {

    private String message;

    private int code;

    private T data;

    private Object extendInfo;

    private long timeStamp;

    private long execTime;

    public ApiResult() {
        this.message = "Success";
        this.code = 0;
        this.timeStamp = 0L;
        this.execTime = 0L;
    }

    @Transient
    public boolean isSuccess() {
        return code == 0;
    }

    @Transient
    public boolean isFail() {
        return !isSuccess();
    }

    public static <T> ApiResult<T> success() {
        return new ApiResult<T>();
    }

    public static <T> ApiResult<T> success(T data) {
        ApiResult<T> apiResult = new ApiResult<T>();
        apiResult.data = data;
        return apiResult;
    }

    public static ApiResult successPage(List list, Integer count) {
        return successPage(list, count, null);
    }

    public static ApiResult successPage(List list, Integer count, HashMap<String, Object> sumMap) {
        ApiResult apiResult = new ApiResult();
        PageData pageData = PageData.data(list, count);
        pageData.setSumMap(sumMap);
        apiResult.data = pageData;
        return apiResult;
    }

    public static ApiResult successPage(List list, Long count) {
        return successPage(list, count, null);
    }

    public static ApiResult successPage(List list, Long count, HashMap<String, Object> sumMap) {
        ApiResult apiResult = new ApiResult();
        PageData pageData = PageData.data(list, count);
        pageData.setSumMap(sumMap);
        apiResult.data = pageData;
        return apiResult;
    }

    public static <T> ApiResult<T> error(String message) {
        ApiResult<T> apiResult = new ApiResult<>();
        apiResult.code = 1;
        apiResult.message = message;
        return apiResult;
    }

    public static <T> ApiResult<T> error(String message, int code) {
        ApiResult<T> apiResult = new ApiResult<>();
        apiResult.code = code;
        apiResult.message = message;
        return apiResult;
    }

    public static ApiResult page(List list, Long total) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("list", list);
        data.put("total", total);
        return success(data);
    }

    public static <T> ApiResult<T> todo(String message) {
        ApiResult<T> apiResult = new ApiResult();
        apiResult.code = 1;
        apiResult.message = "TODO: " + message;
        return apiResult;
    }

}