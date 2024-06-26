package com.hs.monitor.message;

import com.fazecast.jSerialComm.SerialPort;
import com.hs.monitor.interfaces.HsConsumer;
import com.hs.monitor.protocol.ProtocolByte;
import com.hs.monitor.protocol.ProtocolStr;
import javafx.scene.control.TextArea;

import java.util.HashMap;
import java.util.function.BiConsumer;

import static com.hs.monitor.protocol.ProtocolUtil.*;

/**
 * 消息处理器
 */
public class MessageHandler {

    private static HashMap<Byte, BiConsumer<byte[], String>> map_consumer = new HashMap<>();

    public static SerialPort serialPort = null;
    public static TextArea textArea = null;


    static {
        map_consumer.put(RESOLVE_BYTE, MessageHandler::handleConsumer00);
        map_consumer.put(RESOLVE_STR, MessageHandler::handleConsumer01);
    }

    public static void handleMsg(byte[] bytes, String str) {
        BiConsumer<byte[], String> consumer = map_consumer.get(PROTOCOL_RESOLVE);
        consumer.accept(bytes, str);
    }

    private static void handleConsumer00(byte[] bytes, String str) {
        HsConsumer consumer = ProtocolByte.function_map.get(bytes[2]);
        accept(consumer);
    }

    private static void handleConsumer01(byte[] bytes, String str) {
        HsConsumer consumer = ProtocolStr.function_map.get(str);
        accept(consumer);
    }

    private static void accept(HsConsumer consumer){
        if (consumer != null) {
            consumer.accept();
        } else {
            textArea.appendText("无法发送,没有对应的指令" + "\n");
        }
    }

    public static void appendStr(byte[] bytes) {
        String str = getStr(bytes);
        textArea.appendText("发送:    " + str + "\n");
    }
}
