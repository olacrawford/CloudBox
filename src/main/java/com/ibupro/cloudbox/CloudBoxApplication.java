package com.ibupro.cloudbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * CloudBox 启动类
 * <p>个人云盘服务，规划包含账号、文件（分片上传、存储配额）与分享等功能模块。
 */
@SpringBootApplication
public class CloudBoxApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudBoxApplication.class, args);
    }

}
