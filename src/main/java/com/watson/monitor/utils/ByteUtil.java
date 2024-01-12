package com.watson.monitor.utils;

import com.watson.monitor.enums.Endian;
import com.watson.monitor.protocol.ProtocolUtil;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static com.watson.monitor.protocol.ProtocolUtil.DELIMITER;

public class ByteUtil {
    // 字节数组转换为16进制字符串
    public static String bytesToHex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            // 使用%02X表示以十六进制输出，2表示输出两位，不足两位前面补0
            stringBuilder.append(String.format("%02x", b));
            stringBuilder.append(" ");
        }
        return stringBuilder.toString().trim();
    }

    public static byte[] hexToBytes(String text) {
        // 去除空格并拆分为字符串数组
        String[] hexValues = text.split(DELIMITER);

        // 创建字节数组
        byte[] byteArray = new byte[hexValues.length];

        // 将每个子字符串解析为整数，然后转换为字节
        for (int i = 0; i < hexValues.length; i++) {
            int intValue = Integer.parseInt(hexValues[i], 16);
            byteArray[i] = (byte) intValue;
        }
        return byteArray;
    }

    public static String bytesToStr(byte[] bytes) {
//        StringBuilder stringBuilder = new StringBuilder();
//        for (byte b : bytes) {
//            // 使用%02X表示以十六进制输出，2表示输出两位，不足两位前面补0
//            stringBuilder.append(String.format("%04d", b));
//        }
//        return stringBuilder.toString().trim();
        return new String(bytes);
    }

    public static byte[] strToBytes(String text) {
        return text.getBytes();
    }

    /**
     * 可以传字节数组也可以传单个字节,组合返回一个新的字节数组
     *
     * @param args
     * @return
     */
    public static byte[] mergeBytes(Object... args) {
        if (args.length < 0 || args.length == 0) {
            return new byte[0];
        }
        ArrayList<Byte> list = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Byte) {
                list.add((byte) args[i]);
            } else if (args[i] instanceof byte[]) {
                byte[] arr = (byte[]) args[i];
                for (int j = 0; j < arr.length; j++) {
                    list.add(arr[j]);
                }
            }
        }
        Byte[] Byte_Arr = list.toArray(new Byte[0]);
        byte[] bytes = new byte[Byte_Arr.length];
        for (int i = 0; i < Byte_Arr.length; i++) {
            bytes[i] = Byte_Arr[i];
        }
        return bytes;
    }

    /**
     * 把字节数组转为int,大小端由ProtocolUtil.endian决定
     *
     * @param bytes
     * @return
     */
    public static int bytesToInt(byte[] bytes) {
        int result = 0;
        //小端序
        if (ProtocolUtil.endian == Endian.SmallEndian) {
            for (int i = 0; i < bytes.length; i++) {
                result = result | ((bytes[i] & 0xFF) << (8 * i));
            }
        } else if (ProtocolUtil.endian == Endian.BigEndian) {
            for (int i = 0; i < bytes.length; i++) {
                result = result | ((bytes[i] & 0xFF) << (8 * (bytes.length - i - 1)));
            }
        }
        return result;
    }

    /**
     * 第一个参数是要转换的int值,第二个参数表示返回byte数组的长度,如果不指定,默认为2
     *
     * @param args
     * @return
     */
    public static byte[] intToBytes(int... args) {
        int len = 2;
        if (args.length > 1) {
            //如果返回的数组长度没有指定,那么默认是2
            len = args[1];
        }
        byte[] bytes = new byte[len];
        //小端序
        if (ProtocolUtil.endian == Endian.SmallEndian) {
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) (args[0] >> (8 * i) & 0xFF);
            }
        } else if (ProtocolUtil.endian == Endian.BigEndian) {
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) (args[0] >> (8 * (bytes.length - i - 1)) & 0xFF);
            }
        }
        return bytes;
    }

    public static void main(String[] args) {
//        byte[] bytes = new byte[]{0x11, 0x22, 0x33, 0x44};
//        System.out.println("结果=" + bytesToHex(intToBytes(2200)));
        String path = JsonUtil.class.getResource("/").getPath();
        System.out.println("path=" + path);
    }

}
