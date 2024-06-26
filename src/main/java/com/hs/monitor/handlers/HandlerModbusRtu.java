package com.hs.monitor.handlers;

import com.hs.monitor.annotations.ControlAnnotation;
import com.hs.monitor.communication.*;
import com.hs.monitor.communication.modbus.HsSerialPortWrapper;
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
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import org.controlsfx.control.CheckComboBox;

import java.util.List;

import static com.hs.monitor.protocol.ProtocolUtil.*;
import static com.hs.monitor.utils.ByteUtil.intToBytes;
import static com.hs.monitor.utils.ByteUtil.mergeBytes;
import static com.hs.monitor.utils.LanUtil.addLanguageListen;

/**
 * serial串口设置页面控件的处理器类
 */
public class HandlerModbusRtu {

    @ControlAnnotation("port")
    public static void handlePorts(ComboBox<String> comboBox) {
        comboBox.setItems(Serial.getPorts());
        comboBox.getSelectionModel().select(0);
        comboBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            Serial.handlePorts(newValue.intValue());
        });
    }

    @ControlAnnotation("baud_rate")
    public static void handleBaudRates(ComboBox<String> comboBox) {
        comboBox.setItems(BaudRate.getList());
        comboBox.getSelectionModel().select(3);
        comboBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            Serial.handleBaudRates(newValue.intValue());
        });
    }

    @ControlAnnotation("data_bit")
    public static void handleDataBits(ComboBox<String> comboBox) {
        comboBox.setItems(DataBit.getList());
        comboBox.getSelectionModel().select(3);
        comboBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            Serial.handleDataBits(newValue.intValue());
        });
    }

    @ControlAnnotation("stop_bit")
    public static void handleStopBits(ComboBox<String> comboBox) {
        comboBox.setItems(StopBit.getList());
        comboBox.getSelectionModel().select(0);
        comboBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            Serial.handleStopBits(newValue.intValue());
        });
    }

    @ControlAnnotation("parity")
    public static void handleParities(ComboBox<String> comboBox) {
        comboBox.setItems(Parity.getList());
        comboBox.getSelectionModel().select(0);
        comboBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            Serial.handleParities(newValue.intValue());
        });
    }

    @ControlAnnotation("slave_id")
    public static void handleSlaveId(CheckComboBox<String> checkComboBox) {
        slaveIds = checkComboBox.getCheckModel().getCheckedItems();
        handleCheckComboBox(checkComboBox);
    }

    private static ObservableList<String> slaveIds;

    @ControlAnnotation("open")
    public static void handleOpen(Button button) {
        button.setOnAction(e -> {
            openPort(button);
        });
    }

    public static void openPort(Button button) {
        if (slaveIds == null || slaveIds.size() == 0) {
            HsAlert.alert("提示", "从机ID不能为空");
            return;
        }

        if (!Serial.serialPort.isOpen()) {
            System.out.println("没有打开");
            Serial.serialPort.setBaudRate(Serial.baudRate.getValue());
            Serial.serialPort.setNumDataBits(Serial.dataBit.getValue());
            Serial.serialPort.setNumStopBits(Serial.stopBit.getValue());
            Serial.serialPort.setParity(Serial.parity.getValue());

            if (Serial.serialPort.openPort()) {
                System.out.println(Serial.serialPort.getSystemPortName() + "打开成功");
                ModbusRtu.init();

                Number number = Modbus4j.readHoldingRegister(Integer.parseInt(slaveIds.get(0)), 0, DataType.TWO_BYTE_INT_UNSIGNED);

                System.out.println("number=" + number);
                button.setText("关闭串口");
            } else {
                System.out.println(Serial.serialPort.getSystemPortName() + "打开失败");
            }
        } else {
            System.out.println("已经打开了");
            try {
////                // 关闭 Modbus 主站和串口
                Modbus4j.getMaster().destroy();
                ModbusRtu.destroy();
//////                HsSerialPortWrapper.getInstance().close();
                button.setText("打开串口");
            } catch (Exception e) {
                System.out.println("关闭串口异常");
                e.printStackTrace();
            }
//            if (Serial.serialPort.closePort()) {
////                isStop = true;
//                button.setText("打开串口");
//            }
        }
    }

    //处理显示项
    private static void handleValueShowed(CheckComboBox<String> checkComboBox) {
        List<Integer> checkedIndices = checkComboBox.getCheckModel().getCheckedIndices();
        int totalItems = checkComboBox.getItems().size() - 1;//把全选项扣掉
        int checkedCount = checkedIndices.size();
        checkComboBox.titleProperty().set(checkedCount + " / " + totalItems);
    }

    //处理全选/全不选,以及自定义显示项问题
    private static void handleCheckComboBox(CheckComboBox<String> checkComboBox) {
        checkComboBox.getCheckModel().check(1);
        handleValueShowed(checkComboBox);

        addLanguageListen("modbus.all", checkComboBox);

        // 监听第一项（索引0）的选中状态变化
        checkComboBox.getCheckModel().getCheckedItems().addListener((ListChangeListener<? super String>) change -> {
            while (change.next()) {
                int firstItemIndex = 0; // 第一项的索引，假设是0
                handleValueShowed(checkComboBox);
                slaveIds = checkComboBox.getCheckModel().getCheckedItems();
                if (change.getAddedSize() > 0 && change.getAddedSubList().contains(checkComboBox.getItems().get(firstItemIndex))) {
                    // 第一项被选中
                    for (int i = 1; i < checkComboBox.getItems().size(); i++) {
                        checkComboBox.getCheckModel().check(checkComboBox.getItems().get(i));
                    }
                } else if (change.getRemovedSize() > 0 && change.getRemoved().contains(checkComboBox.getItems().get(firstItemIndex))) {
                    // 第一项被取消选中
                    for (int i = 1; i < checkComboBox.getItems().size(); i++) {
                        checkComboBox.getCheckModel().clearCheck(checkComboBox.getItems().get(i));
                    }
                }
            }
        });
    }
}
