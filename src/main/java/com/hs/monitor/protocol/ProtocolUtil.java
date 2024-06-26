package com.hs.monitor.protocol;


import com.hs.monitor.enums.Endian;
import com.hs.monitor.utils.ByteBuffer;
import com.hs.monitor.utils.ByteUtil;
import com.hs.monitor.utils.CrcUtil;
import javafx.application.Platform;
import javafx.beans.property.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;

import static com.hs.monitor.maps.PageMap.map_data_page;
import static com.hs.monitor.utils.ByteUtil.bytesToInt;

/**
 * 协议工具类
 * 直接读取字节还是字符,字符以什么分隔
 * 比如
 * 字节01 02 03 0A 68读取字节
 * 字符
 * 2200 1000 500,读取字符
 */
public class ProtocolUtil {
    public static String DELIMITER_COMMA = ",";

    public static Endian endian = Endian.SmallEndian;//默认采用小端序
    public static String DELIMITER_SPACE = " ";
    public static String DELIMITER = DELIMITER_SPACE;

    public static final byte RESOLVE_BYTE = 0x00;//按字节解析,也就是说设备传输的是0x10 0x0x 0x68等等这样子的一个个字节
    public static final byte RESOLVE_STR = 0x01;//按字符解析,也就是说设备传输的是类似2200,2200,500类似这样的一个个字符

    public static byte PROTOCOL_RESOLVE = RESOLVE_STR;//协议解析方式

    public static IntegerProperty protocol_type = new SimpleIntegerProperty(0x01);//协议类型,不同机型对应不同协议

    public static List<String> group_list = new ArrayList<>();
    public static List<String> cmd_list = new ArrayList<>();

    /**
     * key是group_name,value是cmd,待发送的命令
     */
    public static Map<String, String> group_cmd = new HashMap<>();

    public static String group_name;
    public static String cmd_read;


    /**
     * 用于解析数据的map
     * 从串口获取到字节流解析为String
     */
    private static HashMap<Byte, Function<byte[], String>> MAP_RESOLVE = new HashMap<>();

    /**
     * 用于打包封装的map
     * 获取到String封装为byte数组发送到串口
     */
    private static HashMap<Byte, Function<String, byte[]>> MAP_PACK = new HashMap<>();

    static {
        MAP_RESOLVE.put(RESOLVE_BYTE, ByteUtil::bytesToHex);
        MAP_RESOLVE.put(RESOLVE_STR, ByteUtil::bytesToStr);

        MAP_PACK.put(RESOLVE_BYTE, ByteUtil::hexToBytes);
        MAP_PACK.put(RESOLVE_STR, ByteUtil::strToBytes);
    }

    public static String getStr(byte[] bytes) {
        Function<byte[], String> function = MAP_RESOLVE.get(PROTOCOL_RESOLVE);
        return function.apply(bytes);
    }

    public static byte[] getBytes(String str) {
        Function<String, byte[]> function = MAP_PACK.get(PROTOCOL_RESOLVE);
        return function.apply(str);
    }

    /**
     * HashMap<String, Object> map_prop = new HashMap<>();
     * map_prop.put("name", name);
     * map_prop.put("unit", unit);
     * map_prop.put("component", component);
     * map_prop.put("value_property", value_property);
     *
     * @param bytes
     */
    public static void resolveBytes(byte[] bytes) {
        //去除crc,保留数据
        byte[] data = Arrays.copyOfRange(bytes, 0, bytes.length - 2);
        byte[] crc = Arrays.copyOfRange(bytes, bytes.length - 2, bytes.length);

        byte[] crc_calc = CrcUtil.crc16(data);

        if (!CrcUtil.isCrcOk(crc, crc_calc)) {
            System.out.println("crc校验不过");
            return;
        }

        if (PROTOCOL_RESOLVE == RESOLVE_BYTE) {
            resolveByByte(data);
        } else if (PROTOCOL_RESOLVE == RESOLVE_STR) {
            resolveByChar(data);
        }
    }

    /**
     * 按字符解析(也就是ASC码),如2200,2200,500...
     *
     * @param data
     */
    public static void resolveByChar(byte[] data) {
        String str = new String(data);
        System.out.println("读取的字符串=" + str);
        String[] str_arr = str.split(",");
        HashMap<String, Object> map = (HashMap<String, Object>) map_data_page.get(group_name);
        String cmd = ProtocolUtil.group_cmd.get(group_name);
        System.out.println("cmd=" + cmd);
        //页面和发送的命令不对应就return
        //比如发送了设置命令,此时点击到了首页,拿设置页的数据去填充首页就有问题
        if (cmd == null || !cmd.equals(cmd_read)) {
            System.out.println("cmd null ");
            return;
        }
        if (str_arr.length < 2) {
            System.out.println("str length return");
            return;
        }
        if (map == null) {
            System.out.println("map return");
            return;
        }
        protocol_type.set(Integer.parseInt(str_arr[1]));
        String type = String.format("%02d", Integer.parseInt(str_arr[1]));
        List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) map.get(type);

        if (list == null) {
            System.out.println("list is null return");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            StringProperty value_property = (StringProperty) list.get(i).get("value_property");
            BooleanProperty checked_property = (BooleanProperty) list.get(i).get("checked_property");
            BigDecimal value = new BigDecimal(str_arr[i + 2]);
            BigDecimal scale = new BigDecimal(String.valueOf(list.get(i).get("scale")));
            String value1 = String.valueOf(value.multiply(scale));
            if (value_property != null) {
                Platform.runLater(() -> value_property.set(value1));
            }

            if (checked_property != null) {
                Platform.runLater(() -> checked_property.set(value1.equals("1")));
            }

        }
    }

    /**
     * 按字节解析,如10 68 01 04 06...
     *
     * @param data
     */
    public static void resolveByByte(byte[] data) {
        HashMap<String, Object> map = (HashMap<String, Object>) map_data_page.get(group_name);
        protocol_type.set(data[1]);
        List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) map.get(String.format("%02d", protocol_type.get()));

        if (list == null) {
            return;
        }
        ByteBuffer buffer = ByteBuffer.wrap(data);
        byte[] prefix = buffer.getBytes(7);//协议头部的7字节,剩下的才是消息内容
        for (int i = 0; i < list.size(); i++) {
            StringProperty value_property = (StringProperty) list.get(i).get("value_property");
            BooleanProperty checked_property = (BooleanProperty) list.get(i).get("checked_property");
            BigDecimal scale = new BigDecimal(String.valueOf(list.get(i).get("scale")));
            //取几个字节
            int byte_num = Integer.parseInt(String.valueOf(list.get(i).get("bytes")));


            byte[] bytes1 = buffer.getBytes(byte_num);
            int value_int = bytesToInt(bytes1);
            BigDecimal value = new BigDecimal(value_int);


            String value1 = String.valueOf(value.multiply(scale));
            if (value_property != null) {
                Platform.runLater(() -> value_property.set(value1));
            }

            if (checked_property != null) {
                Platform.runLater(() -> checked_property.set(value1.equals("1")));
            }

        }
    }

    /**
     * y = kx + b
     *
     * @param slope     -> k:    斜率
     * @param intercept -> b 截距
     * @return
     */
    public static int getRandom(int intercept, int slope) {
        return (int) (intercept + Math.floor(Math.random() * slope));
    }

    public static void main(String[] args) {
        String format0 = String.format("%02d", 0);
        String format1 = String.format("%02d", 1);
        String format3 = String.format("%02d", 3);
        System.out.println("format=" + format0);
        System.out.println("format=" + format1);
        System.out.println("format=" + format3);
    }


}
