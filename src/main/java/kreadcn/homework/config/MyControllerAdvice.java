package kreadcn.homework.config;

import com.alibaba.fastjson2.JSON;
import kreadcn.homework.model.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.LinkedHashMap;

import static kreadcn.homework.utils.ThreadContextHolder.SESSION_TIMEOUT_ERROR;

/**
 * @author 李洪文
 * @description
 * @date 2019/11/18 18:11
 */

@RestControllerAdvice
@Slf4j
public class MyControllerAdvice implements ResponseBodyAdvice<Object> {
    /**
     * @param ex 异常信息
     * @return json格式出错信息
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseData myExceptionHandler(Exception ex) {
        Throwable cause = ex;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }

        ResponseData responseData = new ResponseData();
        if (ex instanceof HttpRequestMethodNotSupportedException) {
            throw new RuntimeException(ex);
        } else if (!SESSION_TIMEOUT_ERROR.equals(ex.getMessage())) {
            responseData.setErrorCode(1000);
            log.debug("Error:", ex);
        }

        responseData.setSuccess(false);
        if (!StringUtils.hasText(cause.getMessage())) {
            responseData.setErrorMessage(cause.toString());
        } else {
            responseData.setErrorMessage(cause.getMessage());
        }

        return responseData;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest request, ServerHttpResponse serverHttpResponse) {
        String path = request.getURI().getPath();
        if (body instanceof ResponseData) {
            return body;
        } else if (path.contains("/swagger") || path.contains("/v3/api-docs")) {
            return body;
        } else if ("/error".equals(path) && body instanceof LinkedHashMap map) {
            ResponseData<Object> resp = new ResponseData();
            resp.setErrorCode(200);
            resp.setErrorMessage((String) map.get("error"));
            resp.setSuccess(false);
            return resp;
        } else if (methodParameter.getParameterType() == String.class) {
            ResponseData<Object> resp = new ResponseData();
            resp.setData(body);
            resp.setSuccess(true);
            return JSON.toJSONString(resp);
        } else {
            ResponseData<Object> resp = new ResponseData();
            resp.setData(body);
            resp.setSuccess(true);
            return resp;
        }
    }
}
