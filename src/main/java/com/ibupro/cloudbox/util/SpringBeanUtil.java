package com.ibupro.cloudbox.util;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Spring Bean 属性复制工具类
 * <p>基于 {@link org.springframework.beans.BeanUtils#copyProperties(Object, Object)} 封装，
 * 常用于实体与 DTO 之间的转换；要求目标类型提供无参构造器。
 */
public class SpringBeanUtil {

    /**
     * 复制属性，创建并返回目标类型的新对象
     *
     * @param <T>    目标类型
     * @param source 源对象
     * @param target 目标类型
     * @return 复制后的目标类型新对象
     */
    public static <T> T copyProperties(Object source, Class<T> target) {
        try {
            T t = target.getConstructor().newInstance();
            BeanUtils.copyProperties(source, t);
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 复制一份具有相同属性的列表，列表元素为目标类型的新对象
     *
     * @param sourceList 源对象列表
     * @param target     列表元素的目标类型
     * @param <T>        列表元素类型
     * @return 复制后的目标类型列表
     */
    public static <T> List<T> copyProperties(List<?> sourceList, Class<T> target) {
        ArrayList<T> targetList = new ArrayList<>();

        sourceList.forEach(source -> {
            T t = copyProperties(source, target);
            targetList.add(t);
        });
        return targetList;
    }


    /**
     * 将源对象的属性复制到已存在的目标对象上
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copyProperties(Object source, Object target){
        BeanUtils.copyProperties(source,target);
    }

}