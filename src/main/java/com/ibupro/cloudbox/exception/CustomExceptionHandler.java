package com.ibupro.cloudbox.exception;

import com.ibupro.cloudbox.util.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理器
 * <p>兜底拦截所有未被业务代码捕获的异常，统一转换为 {@link JsonData} 格式返回，
 * 避免向前端暴露堆栈等敏感信息。
 */
@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    /**
     * 统一异常处理入口
     * <p>业务异常 {@link BizException} 按其携带的错误码和描述返回给前端；
     * 其余异常一律按系统异常处理，仅返回通用提示，详细信息只记录到日志。
     *
     * @param e 捕获到的异常
     * @return 统一格式的错误响应
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonData handler(Exception e){

        if(e instanceof BizException bizException){
            log.error("[业务异常]{}",e);
            return JsonData.buildCodeAndMsg(bizException.getCode(),bizException.getMsg());
        }else {
            log.error("[系统异常]{}",e);
            return JsonData.buildError("系统异常");
        }

    }

}