package com.watson.monitor.enums;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Parity {
    NONE(0,"NONE", 0),
    ODD(1,"ODD", 1),
    EVEN(2,"EVEN", 2),
    MARK(3,"MARK", 3),
    SPACE(4,"SPACE", 4);

    private int index;
    private String name;
    private int value;

    public static Parity fromIndex(int index) {
        for (Parity parity : Parity.values()) {
            if (parity.index == index) {
                return parity;
            }
        }
        return null;
    }

    // 静态方法，根据整数值获取对应的枚举实例
    public static Parity fromValue(int value) {
        for (Parity parity : Parity.values()) {
            if (parity.value == value) {
                return parity;
            }
        }
        return null;
    }

    // 静态方法，根据整数值获取对应的枚举实例
    public static Parity fromName(String name) {
        for (Parity parity : Parity.values()) {
            if (parity.name == name) {
                return parity;
            }
        }
        return null;
    }

    private static ObservableList<String> list = FXCollections.observableArrayList();

    static {
        for (Parity parity : Parity.values()) {
            list.add(parity.getName());
        }
    }

    public static ObservableList<String> getList() {
        return list;
    }

}
