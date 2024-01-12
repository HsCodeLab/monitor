package com.watson.monitor;

import com.fazecast.jSerialComm.SerialPort;
import com.watson.monitor.enums.BaudRate;
import com.watson.monitor.enums.DataBit;
import com.watson.monitor.enums.Parity;
import com.watson.monitor.enums.StopBit;
import com.watson.monitor.protocol.ProtocolUtil;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.watson.monitor.protocol.ProtocolUtil.PROTOCOL_RESOLVE;

/**
 * 串口主机
 */
public class SerialMaster extends Application {

    private static SerialPort[] port_arr = SerialPort.getCommPorts();

    private static ObservableList<String> ports = FXCollections.observableArrayList(
            Arrays.stream(port_arr)
                    .map(SerialPort::getSystemPortName)
                    .collect(Collectors.toList())
    );

    private static ObservableList<String> baud_rates = BaudRate.getList();
    private static ObservableList<String> data_bits = DataBit.getList();
    private static ObservableList<String> stop_bits = StopBit.getList();
    private static ObservableList<String> parities = Parity.getList();

    private static SerialPort serialPort = port_arr[0];
    private static BaudRate baudRate = BaudRate.RATE_9600;
    private static DataBit dataBit = DataBit.BIT_8;
    private static StopBit stopBit = StopBit.BIT_1;
    private static Parity parity = Parity.NONE;

    private static void handlePorts(int index) {
        serialPort = port_arr[index];
    }

    private static void handleBaudRates(int index) {
        baudRate = BaudRate.fromIndex(index);
    }

    private static void handleDataBits(int index) {
        dataBit = DataBit.fromIndex(index);
    }

    private static void handleStopBits(int index) {
        stopBit = StopBit.fromIndex(index);
    }

    private static void handleParities(int index) {
        parity = Parity.fromIndex(index);
    }

    static List<Consumer<Integer>> functions = new ArrayList<>();

    static {
        functions.add(SerialMaster::handlePorts);
        functions.add(SerialMaster::handleBaudRates);
        functions.add(SerialMaster::handleDataBits);
        functions.add(SerialMaster::handleStopBits);
        functions.add(SerialMaster::handleParities);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();

        ToolBar toolBar = new ToolBar();
        Button btn_serial = new Button("串口");
        Button btn_usb = new Button("USB");
        Button btn_modbus = new Button("Modbus");
        Button btn_snmp = new Button("SNMP");

        toolBar.getItems().addAll(btn_serial, btn_usb, btn_modbus, btn_snmp);

        root.getChildren().addAll(toolBar, getPaneSerial());


        Scene scene = new Scene(root, 1000, 600);

        //应用名称
        primaryStage.setTitle("串口主机");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public HBox getPaneSerial() {
        HBox hBox = new HBox();
        hBox.setSpacing(20);
        hBox.setPadding(new Insets(10));

        VBox vBox1 = new VBox();
        vBox1.setSpacing(10);
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        String[] labels = {"串口", "波特率", "数据位", "停止位", "校验"};
        ComboBox<String>[] comboBoxes = new ComboBox[labels.length];


        // 初始化下拉框的选项
        ObservableList<String>[] comboBoxOptions = new ObservableList[]{
                ports,
                baud_rates,
                data_bits,
                stop_bits,
                parities
        };

        // 初始化默认选择的索引
        int[] defaultSelections = {0, 3, 3, 0, 0};

        for (int i = 0; i < labels.length; i++) {
            Label label = new Label(labels[i]);
            ComboBox<String> comboBox = new ComboBox<>();
            comboBox.setItems(comboBoxOptions[i]);
            comboBox.getSelectionModel().select(defaultSelections[i]);
            // 添加选项变化监听器
            Consumer<Integer> function = functions.get(i);
            comboBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
                if (function != null) {
                    function.accept(newValue.intValue());
                }
            });

            gridPane.add(label, 0, i);
            gridPane.add(comboBox, 1, i);

            comboBoxes[i] = comboBox;
        }

        Button btn_open = new Button("打开串口");
        vBox1.getChildren().addAll(gridPane, btn_open);
        btn_open.setOnAction(e -> {
            if (!serialPort.isOpen()) {
                serialPort.setBaudRate(baudRate.getValue());
                serialPort.setNumDataBits(dataBit.getValue());
                serialPort.setNumStopBits(stopBit.getValue());
                serialPort.setParity(parity.getValue());
                if (serialPort.openPort()) {
                    System.out.println("COM3打开成功");
                } else {
                    System.out.println("COM3打开失败");
                }
            }
        });


        VBox vBox2 = new VBox();
        vBox2.setSpacing(10);

        TextArea textArea_send = new TextArea();
        TextArea textArea_get = new TextArea();

        HBox hBox1 = new HBox();
        hBox1.setSpacing(10);

        Label label = new Label("协议格式");
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setItems(FXCollections.observableArrayList("字节流", "字符流"));
        comboBox.getSelectionModel().select(0);
        comboBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            PROTOCOL_RESOLVE = (byte) newValue.intValue();
            if (serialPort.isOpen()) {
                serialPort.writeBytes(new byte[]{0x30, PROTOCOL_RESOLVE}, 2);
            }
        });

        Button btn = new Button("读取");
        btn.setOnAction(e -> {
//            byte[] bytes = hexToBytes(textArea.getText());
//            byte[] bytes = textArea.getText().getBytes();
            if (serialPort.isOpen()) {
                byte[] bytes_send = ProtocolUtil.getBytes(textArea_send.getText());
                serialPort.writeBytes(bytes_send, bytes_send.length);
//                System.out.println("发送的数据=" + bytesToHex(bytes));
                String str_send = ProtocolUtil.getStr(bytes_send);
                textArea_get.appendText("发送:    " + str_send + "\n");

                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }

                byte[] bytes_get = new byte[serialPort.bytesAvailable()];
                int numRead = serialPort.readBytes(bytes_get, bytes_get.length);
//                System.out.println("发送的数据=" + bytesToHex(bytes));
                String str_get = ProtocolUtil.getStr(bytes_get);
                textArea_get.appendText("读取:    " + str_get + "\n");

            } else {
                System.out.println("串口未打开");
            }
        });

        hBox1.getChildren().addAll(label, comboBox, btn);

        vBox2.getChildren().addAll(textArea_send, textArea_get, hBox1);
        hBox.getChildren().addAll(vBox1, vBox2);

        return hBox;
    }

}
