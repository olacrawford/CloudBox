package com.ibupro.cloudbox.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * JSON 工具类（基于 Jackson）
 * <p>内部持有全局共享的 {@link ObjectMapper}，统一项目中 Jackson 的序列化/反序列化行为：
 * 日期输出为 {@code yyyy-MM-dd HH:mm:ss} 格式、反序列化忽略未知字段、允许空对象序列化等。
 */
@Slf4j
public class JsonUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    static {

        //设置可用单引号
        MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

        //序列化的时候序列对象的所有属性
        MAPPER.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        //反序列化的时候如果多了其他属性,不抛出异常
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //下划线和驼峰互转
        //mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

        //如果是空对象的时候,不抛异常
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        //取消时间的转化格式,默认是时间戳,可以取消,同时需要设置要表现的时间格式
        MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 获取全局共享的 ObjectMapper，供需要自定义读写行为的场景复用
     *
     * @return 预配置的 ObjectMapper 实例
     */
    public static ObjectMapper get() {
        return MAPPER;
    }

    /**
     * 对象转为 JSON 字符串
     *
     * @param obj 待序列化的对象
     * @return JSON 字符串，序列化失败时返回 null（失败信息记录日志）
     */
    public static String obj2Json(Object obj) {
        String jsonStr = null;
        try {
            jsonStr = MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("json格式化异常:{}",e);
        }
        return jsonStr;
    }
    /**
     * JSON 字符串转为对象
     *
     * @param jsonStr  JSON 字符串
     * @param beanType 目标类型的 Class 对象
     * @param <T>      目标类型
     * @return 目标类型对象，解析失败时返回 null（失败信息记录日志）
     */
    public static <T> T json2Obj(String jsonStr, Class<T> beanType) {
        T obj = null;
        try {
            obj = MAPPER.readValue(jsonStr, beanType);
        } catch (Exception e){
            log.error("json格式化异常:{}",e);
        }
        return obj;
    }

    /**
     * JSON 字符串转为对象列表
     *
     * @param jsonData JSON 字符串
     * @param beanType 列表元素类型的 Class 对象
     * @param <T>      列表元素类型
     * @return 目标类型列表，解析失败时返回空列表（失败信息记录日志）
     */
    public static <T> List<T> json2List(String jsonData, Class<T> beanType) {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            return MAPPER.readValue(jsonData, javaType);
        } catch (Exception e) {
            log.error("json格式化异常:{}",e);
        }
        return new ArrayList<>(0);
    }

}