package com.watson.monitor.utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskUtil {

    private static ThreadPoolExecutor executor;

    static {
        Properties properties = loadConfig("task");

        int corePoolSize = Integer.parseInt(properties.getProperty("corePoolSize"));
        int maxPoolSize = Integer.parseInt(properties.getProperty("maxPoolSize"));
        long keepAliveTime = Long.parseLong(properties.getProperty("keepAliveTime"));
        TimeUnit timeUnit = TimeUnit.valueOf(properties.getProperty("timeUnit"));

        // 创建一个ThreadPoolExecutor
        executor = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveTime,
                timeUnit,
                new LinkedBlockingQueue<>() // 使用LinkedBlockingQueue作为工作队列
        );
    }
    public static Properties loadConfig(String file_name) {
        Properties properties = new Properties();
        try (InputStream input = TaskUtil.class.getClassLoader().getResourceAsStream(file_name + ".properties")) {
            if (input != null) {
                properties.load(new InputStreamReader(input, StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static ThreadPoolExecutor getExecutor() {
        return executor;
    }
}
