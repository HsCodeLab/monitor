package com.hs.monitor.utils;

import com.hs.monitor.enums.Endian;

/**
 * 校验工具类
 * crc一般采用大端序
 */
public class CrcUtil {

    private static Endian endian = Endian.BigEndian;
    private static final int POLYNOMIAL = 0xA001;

    /**
     * 小端序
     *
     * @param data
     * @return
     */
    public static byte[] crc16(byte[] data) {
        int crc = 0xFFFF;

        for (byte b : data) {
            crc ^= (b & 0xFF);
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x0001) != 0) {
                    crc = (crc >> 1) ^ POLYNOMIAL;
                } else {
                    crc >>= 1;
                }
            }
        }
        byte low = (byte) (crc & 0xFF);
        byte high = (byte) ((crc >> 8) & 0xFF);
        byte[] crc_bytes = new byte[2];
        if (endian == Endian.SmallEndian) {
            crc_bytes[0] = low;
            crc_bytes[1] = high;
        } else if (endian == Endian.BigEndian) {
            crc_bytes[0] = high;
            crc_bytes[1] = low;
        }
        // 返回两个字节的CRC16校验值，低字节在前，高字节在后
        return crc_bytes;
    }

    public static boolean isCrcOk(byte[] crc_get,byte[] crc_calc){
        return crc_get[0] == crc_calc[0] && crc_get[1] == crc_calc[1];
    }
}
