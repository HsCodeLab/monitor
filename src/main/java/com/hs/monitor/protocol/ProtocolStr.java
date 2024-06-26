package com.hs.monitor.protocol;

import com.hs.monitor.message.MessageHandler;
import com.hs.monitor.interfaces.HsConsumer;
import com.hs.monitor.utils.ByteUtil;
import com.hs.monitor.utils.CrcUtil;

import java.util.HashMap;

import static com.hs.monitor.message.MessageHandler.appendStr;
import static com.hs.monitor.protocol.ProtocolUtil.getRandom;
import static com.hs.monitor.protocol.ProtocolUtil.protocol_type;
import static com.hs.monitor.utils.ByteUtil.bytesToHex;

/**
 * 传输字符流的协议
 */
public class ProtocolStr {
    private static final String MSG_RE = "RE";
    private static final String MSG_PE = "PE";

    /**
     * 存储处理函数的map
     */
    public static HashMap<String, HsConsumer> function_map = new HashMap<>();

    static {
        //处理字符流的map
        function_map.put(MSG_RE, ProtocolStr::handleRE);
        function_map.put(MSG_PE, ProtocolStr::handlePE);
    }

    private static String DELI = ",";

    private static void handleRE() {

        int vol_in = getRandom(2200, 20);
        int vol_in_a = getRandom(2200, 20);
        int vol_in_b = getRandom(2200, 20);
        int vol_in_c = getRandom(2200, 20);
        int vol_out = getRandom(2200, 20);
        int vol_out_a = getRandom(2200, 20);
        int vol_out_b = getRandom(2200, 20);
        int vol_out_c = getRandom(2200, 20);
        int fre_in = 500;
        int fre_out = 500;
        int bat_vol = getRandom(40, 10);
        int bat_level = getRandom(0, 100);
        int ups_temp = getRandom(40, 60);
        int out_load_percent = getRandom(80, 18);

        String str;
        if (protocol_type.get() == 0x01) {
            str = "SN-0001," + protocol_type.get() + DELI + vol_in + DELI + vol_out + DELI + fre_in + DELI + fre_out + DELI + bat_vol + DELI + bat_level + DELI + ups_temp + DELI + out_load_percent;
        } else {
            str = "SN-0001," + protocol_type.get() + DELI + vol_in_a + DELI + vol_in_b + DELI + vol_in_c + DELI + vol_out_a + DELI + vol_out_b + DELI + vol_out_c + DELI + fre_in + DELI + fre_out + DELI + bat_vol + DELI + bat_level + DELI + ups_temp + DELI + out_load_percent;
        }
        byte[] data = str.getBytes();


        byte[] crc16 = CrcUtil.crc16(data);

        System.out.println("发送的数据=" + bytesToHex(data));
        System.out.println("发送的crc=" + bytesToHex(crc16));

        byte[] bytes = ByteUtil.mergeBytes(data, crc16);
        MessageHandler.serialPort.writeBytes(bytes, bytes.length);

        appendStr(bytes);
    }

    private static void handlePE() {
        String str = "";
        int vol_bypass = getRandom(2200, 20);
        int vol_bypass_a = getRandom(2200, 20);
        int vol_bypass_b = getRandom(2200, 20);
        int vol_bypass_c = getRandom(2200, 20);
        int fre_in = 500;
        int bat_num = 48;
        int board_control = getRandom(0, 2);
        int sound_alarm = getRandom(0, 2);
        int timer_on_off = getRandom(0, 2);
        int bat_check = getRandom(0, 2);
        int net_awake = getRandom(0, 2);
        if (protocol_type.get() == 0x01) {
            str = "SN-0001," + protocol_type.get() + DELI + vol_bypass + DELI + fre_in + DELI + bat_num + DELI + board_control + DELI + sound_alarm
                    + DELI + timer_on_off + DELI + bat_check + DELI + net_awake;
        } else {
            str = "SN-0001," + protocol_type.get() + DELI + vol_bypass_a + DELI + vol_bypass_b + DELI + vol_bypass_c + DELI + fre_in + DELI + bat_num + DELI + board_control + DELI + sound_alarm
                    + DELI + timer_on_off + DELI + bat_check + DELI + net_awake;
        }

        byte[] data = str.getBytes();
        byte[] crc16 = CrcUtil.crc16(data);
        byte[] bytes = ByteUtil.mergeBytes(data, crc16);
        MessageHandler.serialPort.writeBytes(bytes, bytes.length);

        appendStr(bytes);
    }
}
