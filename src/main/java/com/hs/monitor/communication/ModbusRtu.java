package com.hs.monitor.communication;

import com.hs.monitor.communication.modbus.HsSerialPortWrapper;
import com.hs.monitor.communication.modbus.Modbus4j;
import com.hs.monitor.communication.modbus.ModbusEnum;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;

public class ModbusRtu implements Comm {

    public byte[] read(byte[] bytes) {
        return new byte[0];
    }

    public static ModbusMaster master;

    //初始化
    public static void init() {
        HsSerialPortWrapper.serialPort = Serial.serialPort;
        Modbus4j.comm = ModbusEnum.RTU;
    }

    public static void destroy() {
        master = null;
    }

    public static ModbusMaster getMaster() {
        if (master == null) {
            master = Modbus4j.modbusFactory.createRtuMaster(HsSerialPortWrapper.getInstance());//RTU 协议
            try {
                master.init();
            } catch (ModbusInitException e) {
                System.out.println("初始化异常");
            }
        }
        return master;
    }
}
