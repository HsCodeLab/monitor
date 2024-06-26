package com.hs.monitor.communication;

import com.hs.monitor.communication.modbus.HsSerialPortWrapper;
import com.hs.monitor.communication.modbus.Modbus4j;
import com.hs.monitor.communication.modbus.ModbusEnum;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.ip.IpParameters;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ModbusTcp implements Comm {

    public byte[] read(byte[] bytes) {
        return new byte[0];
    }


    //初步规划key就是各个设备的IP地址
    public static Map<String, ModbusMaster> map = new HashMap<>();

    //初始化
    public static void init(Set<String> ips, String port) {
        Modbus4j.comm = ModbusEnum.TCP;
        ips.forEach(ip -> {
            IpParameters params = new IpParameters();
            params.setHost(ip);
            params.setPort(Integer.parseInt(port));
            ModbusTcp.addMaster(params);
        });
    }

    //销毁
    public static void destroy() {
        //暂时啥也不做
    }

    public static void addMaster(IpParameters params) {
        if (!map.containsKey(params.getHost())) {
            // 采用 Modbus RTU over TCP/IP，第二个参数为 true，即 modbusFactory.createTcpMaster(params, true)
            ModbusMaster master = Modbus4j.modbusFactory.createTcpMaster(params, false);// TCP 协议
            try {
                master.init();
            } catch (ModbusInitException e) {
                System.out.println("初始化异常");
            }
            map.put(params.getHost(), master);
        }
    }

    public static ModbusMaster getMaster(String key) {
        return map.get(key);
    }
}
