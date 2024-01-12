package com.watson.monitor.message;

import com.watson.monitor.utils.ByteUtil;
import lombok.Builder;
import lombok.Data;

/**
 * 消息实体类
 * 假设一帧协议格式如下
 * 帧头   协议类型 功能码 帧码   数据长度 数据内容 crc校验
 * 0x10  1字节   1字节  2字节  2字节    n字节   2字节
 * 其中
 * 协议类型的含义是:假设这么一种情况,同一功能码对应的同一条命令下有单相数据和3相数据,也就是说单相机只回复单相数据,3相机回复3相数据,这里仅以这两种作为区分,
 * 就是会存在同一条命令不同机型回复的内容不相同,因此以此协议类型加以区分,如有机型A和机型B,机型A没有温度数据,机型B有温度数据
 * <p>
 * 功能码,假设0x01代表实时数据,0x03代表参数设置
 * 帧码: 同一帧数据第几次发送,每次累加
 * 数据长度: 数据内容的字节数
 * crc校验采用大端序,数据内容里面的可以选择
 */
@Builder
@Data
public class MessageEntity {
    private byte head;
    private byte protocolType;
    private byte functionCode;
    private byte[] frameNumber;
    private byte[] dataLength;
    private byte[] content;
    private byte[] crc;

    public static byte[] getBytes(MessageEntity entity) {
        byte[] bytes = ByteUtil.mergeBytes(entity.getHead(), entity.getProtocolType(),
                entity.getFunctionCode(), entity.getFrameNumber(), entity.getDataLength(),
                entity.getContent(), entity.getCrc());
        return bytes;
    }
}
