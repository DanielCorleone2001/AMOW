package com.daniel.shiro;

import com.alibaba.fastjson.JSON;
import com.daniel.contains.Constant;
import com.daniel.exception.BusinessException;
import com.daniel.exception.code.BaseResponseCode;
import com.daniel.utils.DataResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @Package: com.daniel.shiro
 * @ClassName: MyAccessControllerFilter
 * @Author: daniel
 * @CreateTime: 2021/1/31 20:27
 * @Description: 自定义拦截类。
 *              功能：拦截需求认证的请求。
 *              逻辑：首先验证客户端的header是否携带了token。如果没有的话直接响应客户端，让客户端转换到登录界面
 *                  如果有token的话就放行，进入shiro SecurityManager来验证
 */
@Slf4j
public class MyAccessControlFilter extends AccessControlFilter {
    /**
     * 是否允许访问
     * @param servletRequest
     * @param servletResponse
     * @param o
     * @return  true：允许，交下一个Filter处理     false：回往下执行onAccessDenied
     * @throws Exception
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    /**
     *
     * @param servletRequest
     * @param servletResponse
     * @return 返回true 跳转到下一个链式调用
     *          返回false 不会跳转
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;//强转成Http请求，方便调用其封装起来的函数
        log.info(request.getMethod());//查看请求的方法
        log.info(request.getRequestURI().toString());//查看请求的url
        //判断客户端头部是否携带accessToken

        try {
            String accessToken = request.getHeader(Constant.ACCESS_TOKEN);
            if ( StringUtils.isEmpty(accessToken)) {//token为空，返回token不能为空的错误码
                throw new BusinessException(BaseResponseCode.TOKEN_NOT_NULL);
            }
            MyPasswordToken passwordToken = new MyPasswordToken(accessToken);
            getSubject(servletRequest,servletResponse).login(passwordToken);
        } catch (BusinessException e) {
            MyResponse(servletResponse,e.getCode(),e.getMsg());
            return false;
        } catch (AuthenticationException e) {
            if ( e.getCause() instanceof BusinessException ) {
                BusinessException exception = (BusinessException)e.getCause();
                MyResponse(servletResponse,exception.getCode(),exception.getMsg());
            } else {
                MyResponse(servletResponse,BaseResponseCode.SHIRO_AUTHENTICATION_ERROR.getCode(),BaseResponseCode.SHIRO_AUTHENTICATION_ERROR.getMsg());
            }
            return false;
        }
        return true;
    }

    // 自定义异常的类，用户返回给客户端相应的JSON格式的信息
    private void MyResponse(ServletResponse response, int code, String msg ) {
        try {
            DataResult result = DataResult.getResult(code,msg);
            response.setContentType("application/json; charset=utf-8");
            response.setCharacterEncoding("utf-8");

            String userJson = JSON.toJSONString(result);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(userJson.getBytes("UTF-8"));
            outputStream.flush();
        } catch (IOException e) {
            log.error("error={}",e);
        }
    }
}
