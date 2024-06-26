package com.hs.monitor.communication;

import com.fazecast.jSerialComm.SerialPort;
import com.hs.monitor.enums.BaudRate;
import com.hs.monitor.enums.DataBit;
import com.hs.monitor.enums.Parity;
import com.hs.monitor.enums.StopBit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

public class Serial implements Comm {

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
        SerialPort[] commPorts = getPortArr();
        ObservableList<String> ports = FXCollections.observableArrayList(
                Arrays.stream(commPorts)
                        .map(SerialPort::getSystemPortName)
                        .collect(Collectors.toList())
        );
        return ports;
    }

    public static SerialPort[] getPortArr() {
//        String[] arr = {"COM4", "COM3"};
        SerialPort[] ports = SerialPort.getCommPorts();
        // 定义比较器，按照数字部分进行排序
        Arrays.sort(ports, new Comparator<SerialPort>() {
            @Override
            public int compare(SerialPort p1, SerialPort p2) {
                // 提取数字部分，这里假设格式为字母开头，后跟一个或多个数字
                int num1 = extractNumber(p1.getSystemPortName());
                int num2 = extractNumber(p2.getSystemPortName());

                // 进行比较
                return Integer.compare(num1, num2);
            }

            // 提取字符串中的数字部分
            private int extractNumber(String s) {
                String numberStr = s.replaceAll("\\D", ""); // 移除非数字字符
                return Integer.parseInt(numberStr);
            }
        });
        return ports;
    }

    /**
     * 读取串口函数,写和读中间停顿200秒,让缓冲区有时间存足够的数据
     *
     * @param bytes
     * @return
     */
    public byte[] read(byte[] bytes) {
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
