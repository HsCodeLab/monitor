package com.hs.monitor.handlers;

import com.hs.monitor.annotations.ControlAnnotation;
import com.hs.monitor.communication.*;
import com.hs.monitor.communication.modbus.Modbus4j;
import com.hs.monitor.communication.modbus.ModbusEnum;
import com.hs.monitor.enums.BaudRate;
import com.hs.monitor.enums.DataBit;
import com.hs.monitor.enums.Parity;
import com.hs.monitor.enums.StopBit;
import com.hs.monitor.message.MessageEntity;
import com.hs.monitor.protocol.ProtocolUtil;
import com.hs.monitor.utils.CrcUtil;
import com.hs.monitor.utils.TaskUtil;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.ip.IpParameters;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.hs.monitor.protocol.ProtocolUtil.*;
import static com.hs.monitor.utils.ByteUtil.intToBytes;
import static com.hs.monitor.utils.ByteUtil.mergeBytes;

/**
 * serial串口设置页面控件的处理器类
 */
public class HandlerModbusTcp {

    private static StringProperty net1 = new SimpleStringProperty();
    private static StringProperty net2 = new SimpleStringProperty();
    private static StringProperty net3 = new SimpleStringProperty();
    private static StringProperty net4 = new SimpleStringProperty();
    private static StringProperty port = new SimpleStringProperty();

    private static ListView<String> listView;

    @ControlAnnotation("net1")
    public static void getNet1(TextField textField) {
        net1.set(textField.getText());
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            // 处理文本变化事件
            net1.set(textField.getText());
        });
        // 设置焦点监听器
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // 当失去焦点时
                updateIps();
            }
        });
    }

    @ControlAnnotation("net2")
    public static void getNet2(TextField textField) {
//        textField.setOnAction(e-> net2.set(textField.getText()));
        net2.set(textField.getText());
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            // 处理文本变化事件
            net2.set(textField.getText());
        });
        // 设置焦点监听器
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // 当失去焦点时
                updateIps();
            }
        });
    }

    @ControlAnnotation("net3")
    public static void getNet3(TextField textField) {
        net3.set(textField.getText());
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            // 处理文本变化事件
            net3.set(textField.getText());
        });
        // 设置焦点监听器
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // 当失去焦点时
                updateIps();
            }
        });
    }

    @ControlAnnotation("net4")
    public static void getNet4(TextField textField) {
        net4.set(textField.getText());
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            // 处理文本变化事件
            net4.set(textField.getText());
        });
        // 设置焦点监听器
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // 当失去焦点时
                updateIps();
            }
        });
    }

    @ControlAnnotation("port")
    public static void getPort(TextField textField) {
        port.set(textField.getText());
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            // 处理文本变化事件
            port.set(textField.getText());
        });
    }

    @ControlAnnotation("ip_list")
    public static void showIps(ListView<String> listView) {
        HandlerModbusTcp.listView = listView;
        Set<String> ips = getIps();
        listView.getItems().addAll(ips);
    }


    @ControlAnnotation("read")
    public static void handleRead(Button button) {
        button.setOnAction(e -> {
            Set<String> ips = getIps();

            ModbusTcp.init(ips, port.get());

            Modbus4j.key = "127.0.0.1";
            Number number = Modbus4j.readHoldingRegister(1, 0, DataType.TWO_BYTE_INT_UNSIGNED);
            System.out.println("number=" + number);

        });
    }

    public static void updateIps() {
        Set<String> ips = getIps();
        listView.getItems().clear();
        listView.getItems().addAll(ips);
    }

    public static Set<String> getIps() {
        Set<String> ipSet1 = getIpSet(net1);
        Set<String> ipSet2 = getIpSet(net2);
        Set<String> ipSet3 = getIpSet(net3);
        Set<String> ipSet4 = getIpSet(net4);

        return generateIPCombinations(ipSet1, ipSet2, ipSet3, ipSet4, new StringBuilder(), 0);
    }

    // 递归生成IP组合
    // 递归生成IP组合并返回到Set中
    private static Set<String> generateIPCombinations(Set<String> ipSet1, Set<String> ipSet2, Set<String> ipSet3, Set<String> ipSet4, StringBuilder sb, int depth) {
        Set<String> result = new LinkedHashSet<>();

        if (depth == 4) {
            // 添加当前IP地址组合到结果集合中
            result.add(sb.toString());
            return result;
        }

        Set<String> currentSet = null;
        switch (depth) {
            case 0:
                currentSet = ipSet1;
                break;
            case 1:
                currentSet = ipSet2;
                break;
            case 2:
                currentSet = ipSet3;
                break;
            case 3:
                currentSet = ipSet4;
                break;
        }

        for (String ipPart : currentSet) {
            if (sb.length() > 0) {
                sb.append(".");
            }
            sb.append(ipPart);

            // 递归调用，将生成的IP地址组合添加到结果集中
            result.addAll(generateIPCombinations(ipSet1, ipSet2, ipSet3, ipSet4, sb, depth + 1));

            // 回溯，移除当前层级的IP部分
            sb.setLength(sb.lastIndexOf(".") > 0 ? sb.lastIndexOf(".") : 0);
        }

        return result;
    }

    public static Set<String> getIpSet(StringProperty property) {
        Set<String> set = new LinkedHashSet<>();
        if (!property.get().isEmpty()) {
            //默认三种情况,1.单值比如10, 2. (10-12), 3.(10,12)
            if (property.get().contains(",")) {
                String[] split = property.get().split(",");
                for (String s : split) {
                    set.add(s);
                }
            } else if (property.get().contains("-")) {
                String[] split = property.get().split("-");
                int start = Integer.parseInt(split[0]);
                int end = Integer.parseInt(split[1]);
                for (int i = start; i <= end; i++) {
                    set.add(String.valueOf(i));
                }
            } else {
                set.add(property.get());
            }
        }
        return set;
    }
}
