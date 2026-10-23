package com.ibupro.cloudbox.util;

import com.alibaba.fastjson.JSON;
import com.ibupro.cloudbox.enums.BizCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 统一响应结果封装
 * <p>所有接口的返回值统一使用该类包装：code 为 0 表示成功，非 0 表示失败。
 * 业务错误码统一定义在 {@link BizCodeEnum} 中，配合 BizException + 全局异常处理器使用。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonData {

    /**
     * 状态码：0 表示成功，-1 表示通用失败，其余为 BizCodeEnum 中定义的业务错误码
     */
    private Integer code;
    /**
     * 业务数据
     */
    private Object data;
    /**
     * 提示信息，失败时为错误描述
     */
    private String msg;

    /**
     * 将 data 字段转换为指定类型，用于调用方把 Object 类型的 data 还原成具体业务对象
     *
     * @param typeReference 目标类型
     * @param <T>           目标类型
     * @return 转换后的目标类型对象
     */
    public <T> T getData(Class<T> typeReference){
        return JSON.parseObject(JSON.toJSONString(data),typeReference);
    }

    /**
     * 构建成功响应，不携带数据
     *
     * @return code 为 0 的成功响应
     */
    public static JsonData buildSuccess() {
        return new JsonData(0, null, null);
    }

    /**
     * 构建成功响应并携带业务数据
     *
     * @param data 业务数据
     * @return code 为 0 的成功响应
     */
    public static JsonData buildSuccess(Object data) {
        return new JsonData(0, data, null);
    }

    /**
     * 构建通用失败响应（code 为 -1）
     *
     * @param msg 错误描述信息
     * @return code 为 -1 的失败响应
     */
    public static JsonData buildError(String msg) {
        return new JsonData(-1, null, msg);
    }

    /**
     * 构建自定义状态码的响应
     *
     * @param code 自定义状态码
     * @param msg  提示信息
     * @return 指定状态码的响应
     */
    public static JsonData buildCodeAndMsg(int code, String msg) {
        return new JsonData(code, null, msg);
    }

    /**
     * 根据业务错误码枚举构建响应
     *
     * @param codeEnum 业务错误码枚举
     * @return 携带枚举对应 code 与 message 的响应
     */
    public static JsonData buildResult(BizCodeEnum codeEnum){
        return JsonData.buildCodeAndMsg(codeEnum.getCode(),codeEnum.getMessage());
    }

    /**
     * 判断本次响应是否成功
     *
     * @return code 为 0 时返回 true
     */
    public  boolean isSuccess(){
        return code == 0;
    }
}
