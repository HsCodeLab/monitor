package com.watson.monitor.utils;

import java.util.Arrays;

public class ByteBuffer {

    private byte[] bytes;
    private int index;


    public static ByteBuffer wrap(byte[] bytes) {
        ByteBuffer buffer = new ByteBuffer();
        buffer.bytes = bytes;
        buffer.index = 0;
        return buffer;
    }

    public byte[] getBytes(int length) {
        int to = index + length;
        if (to > bytes.length){
            to =  bytes.length;
        }
        byte[] bytes1 = Arrays.copyOfRange(bytes, index, to);
        index = to;
        return bytes1;
    }
}
