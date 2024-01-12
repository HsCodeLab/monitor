package com.watson.monitor.communication;

import com.fazecast.jSerialComm.SerialPort;
import com.watson.monitor.enums.BaudRate;
import com.watson.monitor.enums.DataBit;
import com.watson.monitor.enums.Parity;
import com.watson.monitor.enums.StopBit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;
import java.util.stream.Collectors;

public class WSerial {

    public static SerialPort[] port_arr = getPortArr();

    public static SerialPort serialPort = port_arr[0];
    public static BaudRate baudRate = BaudRate.RATE_9600;
    public static DataBit dataBit = DataBit.BIT_8;
    public static StopBit stopBit = StopBit.BIT_1;
    public static Parity parity = Parity.NONE;

    public static void handlePorts(int index) {
        serialPort = port_arr[index];
    }

    public static void handleBaudRates(int index) {
        baudRate = BaudRate.fromIndex(index);
    }

    public static void handleDataBits(int index) {
        dataBit = DataBit.fromIndex(index);
    }

    public static void handleStopBits(int index) {
        stopBit = StopBit.fromIndex(index);
    }

    public static void handleParities(int index) {
        parity = Parity.fromIndex(index);
    }

    public static ObservableList<String> getPorts() {
        SerialPort[] commPorts = SerialPort.getCommPorts();
        ObservableList<String> ports = FXCollections.observableArrayList(
                Arrays.stream(commPorts)
                        .map(SerialPort::getSystemPortName)
                        .collect(Collectors.toList())
        );
        return ports;
    }

    public static SerialPort[] getPortArr() {
        return SerialPort.getCommPorts();
    }

    /**
     * 读取串口函数,写和读中间停顿200秒,让缓冲区有时间存足够的数据
     *
     * @param bytes
     * @return
     */
    public static byte[] read(byte[] bytes) {
        if (isOpen()) {
            serialPort.writeBytes(bytes, bytes.length);

            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

            byte[] bytes_get = new byte[serialPort.bytesAvailable()];
            serialPort.readBytes(bytes_get, bytes_get.length);

            return bytes_get;
        } else {
            System.out.println("串口为空或者未打开");
            return new byte[0];
        }
    }

    public static boolean isOpen() {
        return serialPort != null && serialPort.isOpen();
    }
}
