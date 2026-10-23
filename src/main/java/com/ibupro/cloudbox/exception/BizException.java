package com.ibupro.cloudbox.exception;

import lombok.Data;
import com.ibupro.cloudbox.enums.BizCodeEnum;

/**
 * 业务异常
 * <p>业务校验失败时抛出，由全局异常处理器（CustomExceptionHandler）统一捕获，
 * 并按携带的错误码和描述信息转换为 {@link com.ibupro.cloudbox.util.JsonData} 返回。
 * 错误码优先使用 {@link BizCodeEnum} 中定义的枚举。
 */
@Data
public class BizException extends RuntimeException {

    /** 业务错误码，取值见 {@link BizCodeEnum} */
    private int code;
    /** 错误描述信息 */
    private String msg;
    /** 原始异常详情，仅用于日志排查，不返回给前端 */
    private String detail;
    /**
     * 使用自定义错误码和描述信息构建业务异常
     *
     * @param code    业务错误码
     * @param message 错误描述信息
     */
    public BizException(Integer code, String message) {
        super(message);
        this.code = code;
        this.msg = message;
    }

    /**
     * 使用业务错误码枚举构建业务异常
     *
     * @param bizCodeEnum 业务错误码枚举
     */
    public BizException(BizCodeEnum bizCodeEnum){
        super(bizCodeEnum.getMessage());
        this.code = bizCodeEnum.getCode();
        this.msg = bizCodeEnum.getMessage();
    }

    /**
     * 使用业务错误码枚举构建业务异常，并记录触发它的原始异常
     *
     * @param bizCodeEnum 业务错误码枚举
     * @param e           触发业务异常的原始异常，其 toString 会记录到 detail 字段
     */
    public BizException(BizCodeEnum bizCodeEnum, Exception e){
        super(bizCodeEnum.getMessage());
        this.code = bizCodeEnum.getCode();
        this.msg = bizCodeEnum.getMessage();
        this.detail = e.toString();
    }
}