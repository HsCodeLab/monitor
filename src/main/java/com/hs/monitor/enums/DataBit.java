package com.hs.monitor.enums;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DataBit {
    BIT_5(0,5),
    BIT_6(1,6),
    BIT_7(2,7),
    BIT_8(3,8);

    private int index;
    private int value;

    public static DataBit fromIndex(int index) {
        for (DataBit dataBit : DataBit.values()) {
            if (dataBit.index == index) {
                return dataBit;
            }
        }
        return null;
    }

    // 静态方法，根据整数值获取对应的枚举实例
    public static DataBit fromValue(int value) {
        for (DataBit dataBit : DataBit.values()) {
            if (dataBit.value == value) {
                return dataBit;
            }
        }
        return null;
    }

    private static ObservableList<String> dataBits = FXCollections.observableArrayList();

    static {
        for (DataBit dataBit : DataBit.values()) {
            dataBits.add(String.valueOf(dataBit.getValue()));
        }
    }

    public static ObservableList<String> getList() {
        return dataBits;
    }

}
