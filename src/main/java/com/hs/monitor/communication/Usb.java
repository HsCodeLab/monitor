package com.hs.monitor.communication;

import org.usb4java.*;

public class Usb implements Comm {
    private static final short YOUR_PRODUCT_ID = 0;
    private static final short YOUR_VENDOR_ID = 1;

    public static void main(String[] args) {
        // 初始化
        LibUsb.init(null);

        // 获取设备列表
        DeviceList list = new DeviceList();
        int result = LibUsb.getDeviceList(null, list);

        if (result < 0) {
            throw new LibUsbException("Unable to get device list", result);
        }

        try {
            // 遍历设备列表
            for (Device device : list) {
                DeviceDescriptor descriptor = new DeviceDescriptor();
                result = LibUsb.getDeviceDescriptor(device, descriptor);

                if (result != LibUsb.SUCCESS) {
                    throw new LibUsbException("Unable to read device descriptor", result);
                }

                // 根据设备描述符进行过滤（根据你的设备修改条件）
                if (descriptor.idVendor() == YOUR_VENDOR_ID && descriptor.idProduct() == YOUR_PRODUCT_ID) {
                    // 打开设备
                    DeviceHandle handle = new DeviceHandle();
                    result = LibUsb.open(device, handle);

                    if (result != LibUsb.SUCCESS) {
                        throw new LibUsbException("Unable to open USB device", result);
                    }

                    try {
                        // 进行USB通信
                        // TODO: 在这里添加你的USB通信代码
                    } finally {
                        // 关闭设备
                        LibUsb.close(handle);
                    }
                }
            }
        } finally {
            // 释放设备列表
            LibUsb.freeDeviceList(list, true);
            // 结束
            LibUsb.exit(null);
        }
    }

    @Override
    public byte[] read(byte[] bytes) {
        return new byte[0];
    }
}
