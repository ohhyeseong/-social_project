package com.example.StudyPost.global.response;

import ch.qos.logback.core.spi.ErrorCodes;
import com.example.StudyPost.global.exception.ErrorCode;

import java.util.Map;

public record ApiResponse<T>(
        boolean success,
        String code,
        String message,
        T data,
        Map<String,String> errors
) {
    public static <T> ApiResponse<T> ok(T data){
        return new ApiResponse<>(true,"OK","요청이 성공했습니다.", data, null);
    }
    public static ApiResponse<Void> ok(){
        return new ApiResponse<>(true,"OK","요청이 성공했습니다.", null, null);
    }
    public static ApiResponse<Void> error(ErrorCode errorCode){
        return new ApiResponse<>(false,errorCode.name(),errorCode.getMessage(), null, null);
    }
    public static ApiResponse<Void> error(ErrorCode errorCode, Map<String, String> errors){
        return new ApiResponse<>(false,errorCode.name(),errorCode.getMessage(), null, errors);
    }
}
