package com.hs.monitor.communication;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommEnum {
    SERIAL(getClassName(Serial.class)),
    USB(getClassName(Usb.class)),
    MODBUS_RTU(getClassName(ModbusRtu.class)),
    MODBUS_TCP(getClassName(ModbusTcp.class)),
    SNMP(getClassName(Snmp.class)),
    ;

    private String value;

    public static CommEnum fromValue(String value) {
        for (CommEnum comm : CommEnum.values()) {
            if (comm.value == value) {
                return comm;
            }
        }
        return null;
    }

    public static String getClassName(Class<?> clazz) {
        return clazz.getName();
    }
}
