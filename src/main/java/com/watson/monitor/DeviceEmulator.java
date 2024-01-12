package com.watson.monitor;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.watson.monitor.enums.BaudRate;
import com.watson.monitor.enums.DataBit;
import com.watson.monitor.enums.Parity;
import com.watson.monitor.enums.StopBit;
import com.watson.monitor.message.MessageHandler;
import com.watson.monitor.protocol.ProtocolUtil;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Arrays;

import static com.watson.monitor.message.MessageHandler.handleMsg;
import static com.watson.monitor.protocol.ProtocolUtil.PROTOCOL_RESOLVE;
import static com.watson.monitor.protocol.ProtocolUtil.protocol_type;

/**
 * 设备模拟器
 */
public class DeviceEmulator extends Application {

    private static SerialPort[] ports = SerialPort.getCommPorts();
    private static SerialPort serialPort = Arrays.stream(ports)
            .filter(port -> port.getSystemPortName().equals("COM4"))
            .findFirst()
            .orElse(null);

    private static BaudRate baudRate = BaudRate.RATE_9600;
    private static DataBit dataBit = DataBit.BIT_8;
    private static StopBit stopBit = StopBit.BIT_1;
    private static Parity parity = Parity.NONE;

    static {
        serialPort.setBaudRate(baudRate.getValue());
        serialPort.setNumDataBits(dataBit.getValue());
        serialPort.setNumStopBits(stopBit.getValue());
        serialPort.setParity(parity.getValue());

        MessageHandler.serialPort = serialPort;

        if (serialPort.openPort()) {
            System.out.println("COM4打开成功");
            serialPort.addDataListener(new SerialPortDataListener() {
                @Override
                public int getListeningEvents() {
                    return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
                }

                @Override
                public void serialEvent(SerialPortEvent event) {
                    if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                        //加100毫秒延时,让大多数数据能够完整读取
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        byte[] bytes = new byte[event.getSerialPort().bytesAvailable()];
                        int numRead = event.getSerialPort().readBytes(bytes, bytes.length);

                        System.out.println("接收之前");
                        System.out.println("TYPE_PROTOCOL=" + PROTOCOL_RESOLVE);
                        String str = ProtocolUtil.getStr(bytes);
                        textArea.appendText("接收:    " + str + "\n");

                        handleMsg(bytes, str);
//                        System.out.println("bytes.length=" + bytes.length);
//                        String hex = bytesToHex(bytes);
//                        String str = bytesToStr(bytes);

//                        // 处理接收到的数据
//                        if (numRead > 0) {
//                            System.out.println("Received data: " + new String(newData));
//                            // 在这里执行相关函数，处理接收到的数据
//                        }
                    }
                }
            });
        } else {
            System.out.println("串口打开失败");
        }

    }

    private static TextArea textArea;

    @Override
    public void start(Stage primaryStage) {


        HBox root = new HBox();
        textArea = new TextArea();
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setItems(FXCollections.observableArrayList("协议1-单相", "协议2-三相"));
        comboBox.getSelectionModel().select(0);
        comboBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() == 0) {
                protocol_type.set(0x01);
            } else {
                protocol_type.set(0x03);
            }
        });

        MessageHandler.textArea = textArea;


        root.getChildren().addAll(textArea, comboBox);

        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("串口设备模拟器");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}

