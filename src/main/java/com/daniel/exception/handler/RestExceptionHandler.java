package com.daniel.exception.handler;

import com.daniel.exception.BusinessException;
import com.daniel.exception.code.BaseResponseCode;
import com.daniel.utils.dataresult.DataResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Package: com.daniel.exception.handler
 * @ClassName: RestExceptionHandler
 * @Author: daniel
 * @CreateTime: 2021/1/30 19:00
 * @Description: 接管系统异常，自定义成我们想要的结果
 */
@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    //Exception异常的监控
    @ExceptionHandler(Exception.class)
    public DataResult handleException(Exception e) {
        log.error("handlerException...{}",e);
        return DataResult.getResult(BaseResponseCode.SYSTEM_ERROR);
    }

    //运行时异常的监控，此类是自定义的。
    @ExceptionHandler(value = BusinessException.class)//捕获BusinessException此异常
    public DataResult businessException(BusinessException e){
        log.error("businessException,{},{}",e.getLocalizedMessage(),e);
        return DataResult.getResult(e.getCode(),e.getMsg());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public DataResult unauthorizedException(UnauthorizedException e) {
        log.error("UnauthorizedException,{},{}",e.getLocalizedMessage(),e);
        return DataResult.getResult(BaseResponseCode.NOT_PERMISSION);
    }
}
