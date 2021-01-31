package com.daniel.exception;

import com.daniel.exception.code.ResponseCodeInterface;

/**
 * @Package: com.daniel.exception
 * @ClassName: BusinessException
 * @Author: daniel
 * @CreateTime: 2021/1/30 13:12
 * @Description:
 */
public class BusinessException extends RuntimeException{
    /**
     * 提示编码
     */
    private final  int code;

    /**
     * 后端提示语
     */
    private final String msg;

    /**
     * 构造方法
     * @param code
     * @param msg
     */
    public BusinessException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(ResponseCodeInterface responseCodeInterface) {
        this(responseCodeInterface.getCode(),responseCodeInterface.getMsg());
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
