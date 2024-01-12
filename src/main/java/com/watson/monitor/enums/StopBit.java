package com.watson.monitor.enums;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StopBit {
    BIT_1(0, "1", 1),
    BIT_1_5(1, "1.5", 2),
    BIT_2(2, "2", 3);

    private int index;
    private String name;
    private int value;

    public static StopBit fromIndex(int index) {
        for (StopBit stopBit : StopBit.values()) {
            if (stopBit.index == index) {
                return stopBit;
            }
        }
        return null;
    }

    // 静态方法，根据整数值获取对应的枚举实例
    public static StopBit fromValue(int value) {
        for (StopBit stopBit : StopBit.values()) {
            if (stopBit.value == value) {
                return stopBit;
            }
        }
        return null;
    }

    // 静态方法，根据整数值获取对应的枚举实例
    public static StopBit fromName(String name) {
        for (StopBit stopBit : StopBit.values()) {
            if (stopBit.name == name) {
                return stopBit;
            }
        }
        return null;
    }

    private static ObservableList<String> list = FXCollections.observableArrayList();

    static {
        for (StopBit stopBit : StopBit.values()) {
            list.add(stopBit.getName());
        }
    }

    public static ObservableList<String> getList() {
        return list;
    }

}
