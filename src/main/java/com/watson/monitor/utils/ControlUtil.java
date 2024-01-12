package com.watson.monitor.utils;

import com.watson.monitor.wrappers.ControlWrapper;
import com.watson.monitor.factory.ComponentHandlerFactory;
import javafx.scene.control.Control;
import org.apache.commons.lang3.StringUtils;

import static com.watson.monitor.utils.JsonUtil.CTRL_PREFIX;
import static com.watson.monitor.utils.JsonUtil.CTRL_PREFIX1;

/**
 * 控件工具类
 */
public class ControlUtil {
    /**
     * 创建控件的同时,反射调用传入的method给控件设置值value
     * 暂时就这一个参数,后面可能还要加其他参数,
     * 还要其他方法,因为不同控件的函数不一样
     *
     * @param className
     * @param wrapper
     * @return
     */
    public static Control createComponent(String className, ControlWrapper wrapper) {
        try {
            String full_name = "";
            if (StringUtils.equals(className, "CheckComboBox")) {
                full_name = CTRL_PREFIX1 + className;
            } else {
                full_name = CTRL_PREFIX + className;
            }
            Class<?> componentClass = Class.forName(full_name);
            Control control = (Control) componentClass.getDeclaredConstructor().newInstance();
            wrapper.setControl(control);

            ComponentHandlerFactory.handleComponent(className, wrapper);

            return control;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
