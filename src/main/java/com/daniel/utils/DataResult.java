package com.daniel.utils;

import com.daniel.exception.code.BaseResponseCode;
import com.daniel.exception.code.ResponseCodeInterface;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Package: com.daniel.utils
 * @ClassName: DataResult
 * @Author: daniel
 * @CreateTime: 2021/1/30 14:35
 * @Description:
 */
@Data
public class DataResult<T> {
    
    @ApiModelProperty(value = "请求响应code，0为成功，其他为失败")
    private int code;
    
    @ApiModelProperty(value = "响应异常码的详细信息")
    private String msg;
    
    @ApiModelProperty(value = "客户端响应信息")
    private T data;
    
    //不带响应异常码的构造方法
    public DataResult(int code, T data) {
        this.code = code;
        this.data = data;
        this.msg=null;
    }
    
    //完整的构造方法
    public DataResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    
    //不带客户端响应信息的构造方法
    public DataResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data=null;
    }
    
    //默认构造方法，默认是成功响应，所以调用成功响应枚举类型的code和msg
    public DataResult() {
        this.code=BaseResponseCode.SUCCESS.getCode();
        this.msg=BaseResponseCode.SUCCESS.getMsg();
        this.data=null;
    }
    
    //配置了客户端响应信息的成功响应
    public DataResult(T data) {
        this.data = data;
        this.code=BaseResponseCode.SUCCESS.getCode();
        this.msg=BaseResponseCode.SUCCESS.getMsg();
    }

    public DataResult(ResponseCodeInterface responseCodeInterface) {
        this.data = null;
        this.code = responseCodeInterface.getCode();
        this.msg = responseCodeInterface.getMsg();
    }

    public DataResult(ResponseCodeInterface responseCodeInterface, T data) {
        this.data = data;
        this.code = responseCodeInterface.getCode();
        this.msg = responseCodeInterface.getMsg();
    }
    /**
     * 操作成功 data为null
     * @Author: daniel
     * @UpdateUser:
     * @Version: 0.01
     * @param
     * @return       
     * @throws
     */
    public static DataResult success(){
        return new DataResult();
    }
    /**
     * 操作成功 data 不为null
     * @Author: daniel
     * @UpdateUser:
     * @Version: 0.01
     * @param data
     * @return       
     * @throws
     */
    public static <T>DataResult success(T data){
        return new DataResult(data);
    }
    /**
     * 自定义 返回操作 data 可控
     * @Author: daniel
     * @UpdateUser:
     * @Version: 0.01
     * @param code
     * @param msg
     * @param data
     * @return       
     * @throws
     */
    public static <T>DataResult getResult(int code,String msg,T data){
        return new DataResult(code,msg,data);
    }
    /**
     *  自定义返回  data为null
     * @Author: daniel
     * @UpdateUser:
     * @Version: 0.01
     * @param code
     * @param msg
     * @return       
     * @throws
     */
    public static DataResult getResult(int code,String msg){
        return new DataResult(code,msg);
    }
    /**
     * 自定义返回 入参一般是异常code枚举 data为空
     * @Author: daniel
     * @UpdateUser:
     * @Version: 0.01
     * @param responseCode
     * @return       
     * @throws
     */
    public static DataResult getResult(BaseResponseCode responseCode){
        return new DataResult(responseCode);
    }
    /**
     * 自定义返回 入参一般是异常code枚举 data 可控
     * @Author: daniel
     * @UpdateUser:
     * @Version: 0.01
     * @param responseCode
     * @param data
     * @return       
     * @throws
     */
    public static <T>DataResult getResult(BaseResponseCode responseCode, T data){

        return new DataResult(responseCode,data);
    }
}
