package com.wanchcoach.app.global.api;

import com.wanchcoach.app.global.error.NotFoundException;
import com.wanchcoach.app.global.error.ServiceRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.wanchcoach.app.global.api.ApiResult.ERROR;

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
        if(e instanceof NotFoundException){
            return newResponse(HttpStatus.NOT_FOUND, e);
        }

        log.info("Unexpected service exception occurred: {}", e.getMessage(), e);
        return newResponse(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
}
