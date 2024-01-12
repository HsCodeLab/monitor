package com.watson.monitor.function;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

public class MenuFunction {
    public static void invokeFunction(String functionName, Object... paras) {
        try {
            // 假设所有的目标函数都在 FunctionDispatcher 类中
            Class<?> functionClass = MenuFunction.class;
            Class<?>[] parameterTypes = new Class<?>[paras.length];

            // 获取参数列表的类型
            for (int i = 0; i < paras.length; i++) {
                parameterTypes[i] = paras[i].getClass();
            }

            Method method = functionClass.getDeclaredMethod(functionName, parameterTypes);
            method.invoke(null, paras); // 如果目标函数是静态方法，可以传入 null 作为对象参数
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void click_theme(String key) {
        if (StringUtils.equals(key, "light")) {
            Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        } else {
            Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        }
    }

}
