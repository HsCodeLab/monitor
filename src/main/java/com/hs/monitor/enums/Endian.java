package com.hs.monitor.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 大小端字节序
 */
@Getter
@AllArgsConstructor
public enum Endian {
    SmallEndian(0),
    BigEndian(1);

    private int value;

    public static Endian fromValue(int value) {
        for (Endian endian : Endian.values()) {
            if (endian.value == value) {
                return endian;
            }
        }
        return null;
    }
}
