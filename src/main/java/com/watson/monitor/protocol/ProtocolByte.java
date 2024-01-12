package com.watson.monitor.protocol;

import com.watson.monitor.message.MessageEntity;
import com.watson.monitor.message.MessageHandler;
import com.watson.monitor.interfaces.WConsumer;
import com.watson.monitor.utils.CrcUtil;

import java.util.HashMap;

import static com.watson.monitor.message.MessageHandler.appendStr;
import static com.watson.monitor.protocol.ProtocolUtil.*;
import static com.watson.monitor.utils.ByteUtil.intToBytes;
import static com.watson.monitor.utils.ByteUtil.mergeBytes;

/**
 * 传输字节流的协议
 */
public class ProtocolByte {
    private static final byte MSG_01 = 0x01;
    private static final byte MSG_03 = 0x03;
    /**
     * 存储处理函数的map
     */
    public static HashMap<Byte, WConsumer> function_map = new HashMap<>();

    static {
        //处理字节流的map
        function_map.put(MSG_01, ProtocolByte::handle01);
        function_map.put(MSG_03, ProtocolByte::handle03);
    }

    private static void handle01() {
//        String str = "10 01 01 02 03 04 05 06 07 08 09";
//        byte[] bytes = getBytes(str);
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


        byte[] vol_in_bytes = intToBytes(vol_in);
        byte[] vol_in_bytes_a = intToBytes(vol_in_a);
        byte[] vol_in_bytes_b = intToBytes(vol_in_b);
        byte[] vol_in_bytes_c = intToBytes(vol_in_c);
        byte[] vol_out_bytes = intToBytes(vol_out);
        byte[] vol_out_bytes_a = intToBytes(vol_out_a);
        byte[] vol_out_bytes_b = intToBytes(vol_out_b);
        byte[] vol_out_bytes_c = intToBytes(vol_out_c);
        byte[] fre_in_bytes = intToBytes(fre_in);
        byte[] fre_out_bytes = intToBytes(fre_out);
        byte[] bat_vol_bytes = intToBytes(bat_vol);
        byte bat_level_bytes = (byte) bat_level;
        byte ups_temp_bytes = (byte) ups_temp;
        byte out_load_percent_bytes = (byte) out_load_percent;

        byte[] content = new byte[0];
        if (protocol_type.get() == 0x01) {
            content = mergeBytes(vol_in_bytes, vol_out_bytes, fre_in_bytes, fre_out_bytes,
                    bat_vol_bytes, bat_level_bytes, ups_temp_bytes, out_load_percent_bytes);
        } else if (protocol_type.get() == 0x03) {
            content = mergeBytes(vol_in_bytes_a,vol_in_bytes_b,vol_in_bytes_c, vol_out_bytes_a,vol_out_bytes_b,vol_out_bytes_c,
                    fre_in_bytes, fre_out_bytes,bat_vol_bytes, bat_level_bytes, ups_temp_bytes, out_load_percent_bytes);
        }


        MessageEntity entity = MessageEntity.builder().head((byte) 0x10).protocolType((byte) protocol_type.get()).functionCode((byte) 0x01)
                .frameNumber(new byte[]{0x01, 0x02}).dataLength(new byte[]{0x10, 0x00}).content(content).build();
        byte[] data = MessageEntity.getBytes(entity);

        byte[] crc = CrcUtil.crc16(data);
        byte[] bytes = mergeBytes(data, crc);


        MessageHandler.serialPort.writeBytes(bytes, bytes.length);

        appendStr(bytes);
    }

    private static void handle03() {
        String str = "10 03 01 02 03 04 05 06 07 08 09";
        byte[] bytes = getBytes(str);
        MessageHandler.serialPort.writeBytes(bytes, bytes.length);

        appendStr(bytes);
    }

}
