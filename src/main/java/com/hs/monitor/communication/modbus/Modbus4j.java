package com.hs.monitor.communication.modbus;

import com.fazecast.jSerialComm.SerialPort;
import com.hs.monitor.communication.ModbusRtu;
import com.hs.monitor.communication.ModbusTcp;
import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.serotonin.modbus4j.msg.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * modbus通讯工具类,采用modbus4j实现
 */
@Slf4j
public class Modbus4j {
    /**
     * 工厂。
     */
    public static ModbusFactory modbusFactory;


    static {
        if (modbusFactory == null) {
            modbusFactory = new ModbusFactory();
        }
    }

    public static ModbusEnum comm;

    //当前主机的key,也就是各个设备的IP地址
    public static String key;

    /**
     * 获取master
     *
     * @return
     * @throws ModbusInitException
     */
    public static ModbusMaster getMaster() {
        if (comm == ModbusEnum.RTU) {
            return ModbusRtu.getMaster();
        } else if (comm == ModbusEnum.TCP) {
            return ModbusTcp.getMaster(key);
        }

//        IpParameters params = new IpParameters();
//        params.setHost("localhost");
//        params.setPort(502);

        // modbusFactory.createUdpMaster(params);//UDP 协议
        // modbusFactory.createAsciiMaster(params);//ASCII 协议

        return null;
    }

    /**
     * 读取[01 Coil Status 0x]类型 开关数据
     *
     * @param slaveId slaveId
     * @param offset  位置
     * @return 读取值
     * @throws ModbusTransportException 异常
     * @throws ErrorResponseException   异常
     * @throws ModbusInitException      异常
     */
    public static Boolean readCoilStatus(int slaveId, int offset) {
        // 01 Coil Status
        Boolean value = null;
        try {
            BaseLocator<Boolean> loc = BaseLocator.coilStatus(slaveId, offset);
            value = getMaster().getValue(loc);
        } catch (Exception e) {
            System.out.println("发生异常");
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 读取[02 Input Status 1x]类型 开关数据
     *
     * @param slaveId
     * @param offset
     * @return
     * @throws ModbusTransportException
     * @throws ErrorResponseException
     * @throws ModbusInitException
     */
    public static Boolean readInputStatus(int slaveId, int offset) {
        // 02 Input Status
        Boolean value = null;
        try {
            BaseLocator<Boolean> loc = BaseLocator.inputStatus(slaveId, offset);
            value = getMaster().getValue(loc);
        } catch (Exception e) {
            System.out.println("发生异常");
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 读取[03 Holding Register类型 2x]模拟量数据
     *
     * @param slaveId  slave Id
     * @param offset   位置
     * @param dataType 数据类型,来自com.serotonin.modbus4j.code.DataType
     * @return
     * @throws ModbusTransportException 异常
     * @throws ErrorResponseException   异常
     * @throws ModbusInitException      异常
     */
    public static Number readHoldingRegister(int slaveId, int offset, int dataType) {
        Number value = null;
        try {
            // 03 Holding Register类型数据读取
            BaseLocator<Number> loc = BaseLocator.holdingRegister(slaveId, offset, dataType);
            value = getMaster().getValue(loc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 读取[04 Input Registers 3x]类型 模拟量数据
     *
     * @param slaveId  slaveId
     * @param offset   位置
     * @param dataType 数据类型,来自com.serotonin.modbus4j.code.DataType
     * @return 返回结果
     * @throws ModbusTransportException 异常
     * @throws ErrorResponseException   异常
     * @throws ModbusInitException      异常
     */
    public static Number readInputRegisters(int slaveId, int offset, int dataType) {
        // 04 Input Registers类型数据读取
        Number value = null;
        try {
            BaseLocator<Number> loc = BaseLocator.inputRegister(slaveId, offset, dataType);
            value = getMaster().getValue(loc);
        } catch (Exception e) {
            System.out.println("发生异常");
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 批量读取使用方法
     *
     * @throws ModbusTransportException
     * @throws ErrorResponseException
     * @throws ModbusInitException
     */
    public static void batchRead() {

        try {
            BatchRead<Integer> batch = new BatchRead<>();

            batch.addLocator(0, BaseLocator.holdingRegister(1, 1, DataType.FOUR_BYTE_FLOAT));
            batch.addLocator(1, BaseLocator.inputStatus(1, 0));

            ModbusMaster master = getMaster();

            batch.setContiguousRequests(false);
            BatchResults<Integer> results = master.send(batch);
            System.out.println(results.getValue(0));
            System.out.println(results.getValue(1));
        } catch (Exception e) {
            System.out.println("发生异常");
            e.printStackTrace();
        }
    }

    /**
     * 写 [01 Coil Status(0x)]写一个 function ID = 5
     *
     * @param slaveId     slave的ID
     * @param writeOffset 位置
     * @param writeValue  值
     * @return 是否写入成功
     * @throws ModbusTransportException
     * @throws ModbusInitException
     */
    public static boolean writeCoil(int slaveId, int writeOffset, boolean writeValue) {
        try {
            // 获取master
            ModbusMaster tcpMaster = getMaster();
            // 创建请求
            WriteCoilRequest request = new WriteCoilRequest(slaveId, writeOffset, writeValue);
            // 发送请求并获取响应对象
            WriteCoilResponse response = (WriteCoilResponse) tcpMaster.send(request);
            if (response.isException()) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            System.out.println("发生异常");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 写[01 Coil Status(0x)] 写多个 function ID = 15
     *
     * @param slaveId     slaveId
     * @param startOffset 开始位置
     * @param bdata       写入的数据
     * @return 是否写入成功
     * @throws ModbusTransportException
     * @throws ModbusInitException
     */
    public static boolean writeCoils(int slaveId, int startOffset, boolean[] bdata) {
        try {
            // 获取master
            ModbusMaster tcpMaster = getMaster();
            // 创建请求
            WriteCoilsRequest request = new WriteCoilsRequest(slaveId, startOffset, bdata);
            // 发送请求并获取响应对象
            WriteCoilsResponse response = (WriteCoilsResponse) tcpMaster.send(request);
            if (response.isException()) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            System.out.println("发生异常");
            e.printStackTrace();
            return false;
        }
    }

    /***
     * 写[03 Holding Register(4x)] 写一个 function ID = 6
     *
     * @param slaveId
     * @param writeOffset
     * @param writeValue
     * @return
     * @throws ModbusTransportException
     * @throws ModbusInitException
     */
    public static boolean writeRegister(int slaveId, int writeOffset, short writeValue) {
        try {
            // 获取master
            ModbusMaster tcpMaster = getMaster();
            // 创建请求对象
            WriteRegisterRequest request = new WriteRegisterRequest(slaveId, writeOffset, writeValue);
            WriteRegisterResponse response = (WriteRegisterResponse) tcpMaster.send(request);
            if (response.isException()) {
                log.error(response.getExceptionMessage());
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            System.out.println("发生异常");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 写入[03 Holding Register(4x)]写多个 function ID=16
     *
     * @param slaveId     modbus的slaveID
     * @param startOffset 起始位置偏移量值
     * @param sdata       写入的数据
     * @return 返回是否写入成功
     * @throws ModbusTransportException
     * @throws ModbusInitException
     */
    public static boolean writeRegisters(int slaveId, int startOffset, short[] sdata) {
        try {
            // 获取master
            ModbusMaster tcpMaster = getMaster();
            // 创建请求对象
            WriteRegistersRequest request = new WriteRegistersRequest(slaveId, startOffset, sdata);
            // 发送请求并获取响应对象
            ModbusResponse response = tcpMaster.send(request);
            if (response.isException()) {
                log.error(response.getExceptionMessage());
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            System.out.println("发生异常");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 写入数字类型的模拟量（如:写入Float类型的模拟量、Double类型模拟量、整数类型Short、Integer、Long）
     *
     * @param slaveId
     * @param offset
     * @param value   写入值,Number的子类,例如写入Float浮点类型,Double双精度类型,以及整型short,int,long
     *                registerCount ,com.serotonin.modbus4j.code.DataType
     * @throws ModbusTransportException
     * @throws ErrorResponseException
     * @throws ModbusInitException
     */
    public static void writeHoldingRegister(int slaveId, int offset, Number value, int dataType) {
        try {
            // 获取master
            ModbusMaster tcpMaster = getMaster();
            // 类型
            BaseLocator<Number> locator = BaseLocator.holdingRegister(slaveId, offset, dataType);
            tcpMaster.setValue(locator, value);
        } catch (Exception e) {
            System.out.println("发生异常");
            e.printStackTrace();
        }
    }

    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
//            // 01测试
//            Boolean v011 = readCoilStatus(1, 0);
//            Boolean v012 = readCoilStatus(1, 1);
//            Boolean v013 = readCoilStatus(1, 6);
//            System.out.println("v011:" + v011);
//            System.out.println("v012:" + v012);
//            System.out.println("v013:" + v013);
//            // 02测试
//            Boolean v021 = readInputStatus(1, 0);
//            Boolean v022 = readInputStatus(1, 1);
//            Boolean v023 = readInputStatus(1, 2);
//            System.out.println("v021:" + v021);
//            System.out.println("v022:" + v022);
//            System.out.println("v023:" + v023);

            // 03测试
            Number num = readHoldingRegister(1, 0, DataType.TWO_BYTE_INT_UNSIGNED);
            System.out.println("读取单一寄存器地址:" + num);

//            // 04测试
            Number v041 = readInputRegisters(1, 0, DataType.FOUR_BYTE_FLOAT);//
//            Number v042 = readInputRegisters(1, 2, DataType.FOUR_BYTE_FLOAT);//
//            System.out.println("v041:" + v041);
//            System.out.println("v042:" + v042);
//            // 批量读取
//            batchRead();

            //@formatter:off
            // 测试01
//			boolean t01 = writeCoil(1, 0, true);
//			System.out.println("T01:" + t01);

            // 测试02
//			boolean t02 = writeCoils(1, 0, new boolean[] { true, false, true });
//			System.out.println("T02:" + t02);

            // 测试03
//			short v = -3;
//			boolean t03 = writeRegister(1, 0, v);
//			System.out.println("T03:" + t03);
            // 测试04
//			boolean t04 = writeRegisters(1, 0, new short[] { -3, 3, 9 });
//			System.out.println("t04:" + t04);
            //写模拟量
//            writeHoldingRegister(1,0, 10.1f, DataType.FOUR_BYTE_FLOAT);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
