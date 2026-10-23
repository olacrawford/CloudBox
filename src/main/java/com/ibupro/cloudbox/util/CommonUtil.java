package com.ibupro.cloudbox.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 通用工具类
 * <p>提供向前端直接输出 JSON 响应、生成文件存储路径等通用方法。
 */
@Slf4j

public class CommonUtil {

    /**
     * 将对象序列化为 JSON 并写入 HTTP 响应，直接返回给前端
     * <p>适用于不走统一返回值流程的场景（如在拦截器中校验登录态失败时直接输出结果）。
     *
     * @param response HTTP 响应对象
     * @param obj      要输出的对象，会通过 {@link JsonUtil#obj2Json(Object)} 序列化
     */
    public static void sendJsonMessage(HttpServletResponse response, Object obj) {

        response.setContentType("application/json; charset=utf-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.print(JsonUtil.obj2Json(obj));
            response.flushBuffer();
        } catch (IOException e) {
            log.warn("响应json数据给前端异常:{}", e);
        }
    }

    /**
     * 根据文件名称获取文件后缀
     *
     * @param fileName 文件名称
     * @return 最后一个点号之后的内容；若文件名不含点号，则原样返回
     */
    public static String getFileSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 根据文件名称生成对象存储中的文件键，格式为：年/月/日/uuid.后缀
     * <p>按天分目录并使用随机 UUID 命名，避免同名文件互相覆盖。
     *
     * @param fileName 原始文件名称
     * @return 形如 2026/10/23/uuid.jpg 的存储路径
     */
    public static String getFilePath(String fileName) {
        String suffix = getFileSuffix(fileName);
        // 生成文件在存储桶中的唯一键
        return StrUtil.format("{}/{}/{}/{}.{}", DateUtil.thisYear(), DateUtil.thisMonth() + 1, DateUtil.thisDayOfMonth(), IdUtil.randomUUID(), suffix);
    }

}
