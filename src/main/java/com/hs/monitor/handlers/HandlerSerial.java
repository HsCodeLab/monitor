package com.hs.monitor.handlers;

import com.hs.monitor.annotations.ControlAnnotation;
import com.hs.monitor.communication.Comm;
import com.hs.monitor.communication.CommFactory;
import com.hs.monitor.communication.CommEnum;
import com.hs.monitor.communication.Serial;
import com.hs.monitor.enums.BaudRate;
import com.hs.monitor.enums.DataBit;
import com.hs.monitor.enums.Parity;
import com.hs.monitor.enums.StopBit;
import com.hs.monitor.message.MessageEntity;
import com.hs.monitor.protocol.ProtocolUtil;
import com.hs.monitor.utils.CrcUtil;
import com.hs.monitor.utils.TaskUtil;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import static com.hs.monitor.protocol.ProtocolUtil.*;
import static com.hs.monitor.utils.ByteUtil.intToBytes;
import static com.hs.monitor.utils.ByteUtil.mergeBytes;

/**
 * serial串口设置页面控件的处理器类
 */
public class HandlerSerial {

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

    //是否停止轮询线程
    public static boolean isStop = false;
    //    public static Thread thread = null;
//    private static final ReentrantLock lock = new ReentrantLock();

    @ControlAnnotation("open")
    public static void handleOpen(Button button) {
        button.setOnAction(e -> {
            openPort(button);
        });
    }

    public static void openPort(Button button) {
        if (!Serial.serialPort.isOpen()) {
            Serial.serialPort.setBaudRate(Serial.baudRate.getValue());
            Serial.serialPort.setNumDataBits(Serial.dataBit.getValue());
            Serial.serialPort.setNumStopBits(Serial.stopBit.getValue());
            Serial.serialPort.setParity(Serial.parity.getValue());

            if (Serial.serialPort.openPort()) {
                System.out.println(Serial.serialPort.getSystemPortName() + "打开成功");
                isStop = false;

                TaskUtil.getExecutor().execute(() -> {
                    while (!isStop) {
                        //上锁
//                            lock.lock();
                        Comm comm = CommFactory.getComm(CommEnum.SERIAL);
                        if (PROTOCOL_RESOLVE == RESOLVE_BYTE) {
                            byte[] vol_in = intToBytes(0x00);
                            byte[] vol_out = intToBytes(0x00);
                            byte[] fre_in = intToBytes(0x00);
                            byte[] fre_out = intToBytes(0x00);
                            byte[] bat_vol = intToBytes(0x00);
                            byte bat_level = 0x00;
                            byte ups_temp = 0x00;
                            byte out_load_percent = 0x00;
                            byte[] content = mergeBytes(vol_in, vol_out, fre_in, fre_out, bat_vol, bat_level, ups_temp, out_load_percent);
                            MessageEntity entity = MessageEntity.builder().head((byte) 0x10).protocolType((byte) 0x01).functionCode((byte) 0x01)
                                    .frameNumber(new byte[]{0x01, 0x02}).dataLength(new byte[]{0x10, 0x00}).content(content).build();
                            byte[] data = MessageEntity.getBytes(entity);
                            byte[] crc = CrcUtil.crc16(data);

                            byte[] bytes = mergeBytes(data, crc);

                            byte[] bytes_read = comm.read(bytes);
                            ProtocolUtil.resolveBytes(bytes_read);
                        } else if (PROTOCOL_RESOLVE == RESOLVE_STR) {
                            System.out.println("ProtocolUtil.cmd_read=" + ProtocolUtil.cmd_read);
                            if (ProtocolUtil.cmd_read != null) {
                                byte[] bytes_send = ProtocolUtil.cmd_read.getBytes();
                                byte[] bytes_read = comm.read(bytes_send);
                                ProtocolUtil.resolveBytes(bytes_read);
                            }
                        }

                        //1秒钟读一次
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }

                        //解锁
//                            lock.unlock();
                    }
                });
                button.setText("关闭串口");
            } else {
                System.out.println("COM3打开失败");
            }
        } else {
            System.out.println("已经打开了");
            if (Serial.serialPort.closePort()) {

                isStop = true;
                button.setText("打开串口");
            }
        }
    }
}
