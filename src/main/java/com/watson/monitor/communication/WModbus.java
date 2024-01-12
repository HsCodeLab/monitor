package com.watson.monitor.communication;

import com.fazecast.jSerialComm.SerialPort;
import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.io.ModbusSerialTransaction;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.net.SerialConnection;
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.util.SerialParameters;

public class WModbus {
    public static void read() {
        try {
            // 设置Modbus库的参数
            ModbusCoupler.getReference().setUnitID(1); // Set the default unit ID if needed

            // 设置串口参数
            String portName = "COM1"; // 修改为你的串口名称
            int baudRate = 9600;
            int dataBits = 8;
            int stopBits = 1;
            int parity = SerialPort.NO_PARITY;

            SerialParameters serialParameters = new SerialParameters();
            serialParameters.setBaudRate(baudRate);
            serialParameters.setDatabits(dataBits);
            serialParameters.setStopbits(stopBits);
            serialParameters.setParity(parity);

            // 创建串口连接
            SerialConnection connection = new SerialConnection(serialParameters);
            connection.open();

            // 创建Modbus RTU事务
            ModbusSerialTransaction transaction = new ModbusSerialTransaction(connection);
            ReadMultipleRegistersRequest request = new ReadMultipleRegistersRequest(0, 10);
            transaction.setRequest(request);

            // Execute transaction
            transaction.execute();

            // 执行事务
            transaction.execute();

            // 获取响应
            ReadMultipleRegistersResponse response = (ReadMultipleRegistersResponse) transaction.getResponse();

            // 处理响应数据
            if (response != null) {
                Register bv = response.getRegister(0); // 获取寄存器数据
                System.out.println("Read data: " + bv.toString());
            } else {
                System.out.println("No response");
            }

            // 关闭连接
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
