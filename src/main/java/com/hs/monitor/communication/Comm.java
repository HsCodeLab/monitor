package com.hs.monitor.communication;

/**
 * 通信方式接口
 * 初步包含Serial,USB,Modbus-RTU,Modbus-TCP,SNMP
 */
public interface Comm {
    byte[] read(byte[] bytes);
}
