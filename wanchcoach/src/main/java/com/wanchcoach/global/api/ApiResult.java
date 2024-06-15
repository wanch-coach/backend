package com.wanchcoach.global.api;

import org.springframework.http.HttpStatus;

public class ApiResult<T> {

    private final boolean success;
    private final T data;
    private final ApiError error;

    public ApiResult(boolean success, T data, ApiError error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResult<T> OK(T data){
        return new ApiResult<>(true, data, null);
    }

    public static ApiResult<?> ERROR(HttpStatus code, Throwable throwable){
        return new ApiResult<>(false, null, new ApiError(code, throwable));
    }

    public static ApiResult<?> ERROR(HttpStatus code, String errorMessage){
        return new ApiResult<>(false, null, new ApiError(code, errorMessage));
    }

    public boolean isSuccess(){return success;}

    public T getData() {
        return data;
    }

    public ApiError getError() {
        return error;
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "success=" + success +
                ", data=" + data +
                ", error=" + error +
                '}';
    }
}
