package com.hs.monitor;

import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import static com.hs.monitor.utils.ByteUtil.bytesToHex;

public class Tmp1 extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        SerialPort[] ports = SerialPort.getCommPorts();

        if (ports.length == 0) {
            System.out.println("No serial ports found.");
            return;
        }

        // Select the first serial port (you may modify this based on your setup)
        SerialPort serialPort = ports[0];

        // 遍历串口数组
        for (SerialPort port : ports) {
            String portName = port.getSystemPortName();

            // 检查当前串口是否为COM3
            if (portName.equals("COM4")) {
                serialPort = port;
                serialPort.setBaudRate(9600);
                serialPort.setNumDataBits(8);
                serialPort.setNumStopBits(1);
                serialPort.setParity(SerialPort.NO_PARITY);
                break; // 找到COM3后退出循环
            }
        }

        // Open the serial port


        primaryStage.setTitle("JavaFX Simple App");

        Button btn = new Button("打开串口");
        Button btn1 = new Button("读数据");

        SerialPort finalSerialPort2 = serialPort;
        btn.setOnAction(e -> {
            if (finalSerialPort2.openPort()) {
                System.out.println("打开串口成功");
            } else {
                System.out.println("打开失败");
            }
        });

        btn1.setOnAction(e -> {
            if (finalSerialPort2.isOpen()) {
                // Create a thread for reading data from the serial port
                // Check if there is data available
                if (finalSerialPort2.bytesAvailable() > 0) {
                    // Read data from the serial port
                    byte[] buffer = new byte[finalSerialPort2.bytesAvailable()];
                    int bytesRead = finalSerialPort2.readBytes(buffer, buffer.length);
                    System.out.println("读取的数据为=" + bytesToHex(buffer));
                } else {
                    System.out.println("没有数据");
                }
            }
        });

        HBox root = new HBox();
        root.getChildren().addAll(btn, btn1);

        Scene scene = new Scene(root, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
