package com.wanchcoach.global.api;

import com.wanchcoach.global.error.AlreadyExistException;
import com.wanchcoach.global.error.InvalidJWTException;
import com.wanchcoach.global.error.InvalidAccessException;
import com.wanchcoach.global.error.NotFoundException;
import com.wanchcoach.global.error.ServiceRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.wanchcoach.global.api.ApiResult.ERROR;

@RestControllerAdvice
@Slf4j
public class GeneralExceptionHandler {
    private ResponseEntity<ApiResult<?>> newResponse(HttpStatus code, Throwable throwable) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(ERROR(code, throwable), headers, code);
    }

    @ExceptionHandler(ServiceRuntimeException.class)
    public ResponseEntity<?> handleServiceRuntimeException(ServiceRuntimeException e){
        if (e instanceof NotFoundException){
            return newResponse(HttpStatus.NOT_FOUND, e);
        }else if(e instanceof InvalidJWTException){
            return newResponse(HttpStatus.UNAUTHORIZED, e);
        }else if(e instanceof AlreadyExistException){
            return newResponse(HttpStatus.ALREADY_REPORTED, e);
        }else if (e instanceof InvalidAccessException) {
            return newResponse(HttpStatus.FORBIDDEN, e);
        }

        log.info("Unexpected service exception occurred: {}", e.getMessage(), e);
        return newResponse(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
}
