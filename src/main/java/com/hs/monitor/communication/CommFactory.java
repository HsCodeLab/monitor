package com.hs.monitor.communication;

import java.lang.reflect.Constructor;

/**
 * 简单工厂+反射模式
 */
public class CommFactory {
    public static Comm getComm(CommEnum commEnum) {
        Comm comm = null;
        try {
            // 使用 Class.forName() 方法根据类名加载类对象
            Class<?> clazz = Class.forName(commEnum.getValue());

            // 获取默认的构造方法（无参数）
            Constructor<?> constructor = clazz.getDeclaredConstructor();

            // 如果构造方法是私有的，需要设置可访问性为 true
            constructor.setAccessible(true);

            // 使用构造方法创建类的实例
            comm = (Comm) constructor.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
            System.out.println("反射读取通信类发生异常");
            e.printStackTrace();
        }
        return comm;
    }
}
