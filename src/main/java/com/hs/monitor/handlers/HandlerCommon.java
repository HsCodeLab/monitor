package com.hs.monitor.handlers;

import com.hs.monitor.annotations.ControlAnnotation;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.ComboBox;

import java.util.HashMap;

/**
 * serial串口设置页面控件的处理器类
 */
public class HandlerCommon {

    public static IntegerProperty temp_format = new SimpleIntegerProperty(0x00);//摄氏度
    public static HashMap<Integer,String> tmp_map = new HashMap();

    static {
        tmp_map.put(0x00,"℃");
        tmp_map.put(0x01,"℉");
    }

    @ControlAnnotation("temp_format")
    public static void handle01(ComboBox<String> comboBox) {
//        comboBox.setItems(WSerial.getPorts());
        comboBox.getSelectionModel().select(0);
        comboBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            temp_format.set(newValue.intValue());
        });
    }
}
