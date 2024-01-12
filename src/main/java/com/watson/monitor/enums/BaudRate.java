package com.watson.monitor.enums;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum 主要包含3个值
 * index: 对应下拉框的索引
 * name: 对应下拉框的显示项
 * value: 对应要给串口设置的值
 */
@Getter
@AllArgsConstructor
public enum BaudRate {
    RATE_1200(0, 1200),
    RATE_2400(1, 2400),
    RATE_4800(2, 4800),
    RATE_9600(3, 9600),
    RATE_19200(4, 19200),
    RATE_38400(5, 38400),
    RATE_115200(6, 115200);

    private int index;
    private int value;

    public static BaudRate fromIndex(int index) {
        for (BaudRate baudRate : BaudRate.values()) {
            if (baudRate.index == index) {
                return baudRate;
            }
        }
        return null;
    }

    // 静态方法，根据整数值获取对应的枚举实例
    public static BaudRate fromValue(int value) {
        for (BaudRate baudRate : BaudRate.values()) {
            if (baudRate.value == value) {
                return baudRate;
            }
        }
        return null;
    }

    private static ObservableList<String> list = FXCollections.observableArrayList();

    static {
        for (BaudRate baudRate : BaudRate.values()) {
            list.add(String.valueOf(baudRate.getValue()));
        }
    }

    public static ObservableList<String> getList() {
        return list;
    }
}
