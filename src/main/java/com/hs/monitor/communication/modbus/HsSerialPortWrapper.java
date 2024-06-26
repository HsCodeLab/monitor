package com.hs.monitor.communication.modbus;

import com.fazecast.jSerialComm.SerialPort;
import com.serotonin.modbus4j.serial.SerialPortWrapper;

import java.io.InputStream;
import java.io.OutputStream;

public class HsSerialPortWrapper implements SerialPortWrapper {
    public static SerialPort serialPort;

    private static HsSerialPortWrapper serialPortWrapper;

    public static HsSerialPortWrapper getInstance() {
        if (serialPortWrapper == null) {
            synchronized (HsSerialPortWrapper.class) {
                if (serialPortWrapper == null) {
                    serialPortWrapper = new HsSerialPortWrapper();
                }
            }
        }
        return serialPortWrapper;
    }

    private HsSerialPortWrapper() {
    }


    @Override
    public void close() throws Exception {
        if (serialPort != null && serialPort.isOpen())
            serialPort.closePort();
    }

    @Override
    public void open() throws Exception {
        if (serialPort != null && serialPort.isOpen())
            serialPort.openPort();
    }

    @Override
    public InputStream getInputStream() {
        if (serialPort != null && serialPort.isOpen())
            return serialPort.getInputStream();
        return null;
    }

    @Override
    public OutputStream getOutputStream() {
        if (serialPort != null && serialPort.isOpen())
            return serialPort.getOutputStream();
        return null;
    }

    @Override
    public int getBaudRate() {
        if (serialPort != null && serialPort.isOpen())
            return serialPort.getBaudRate();
        return 0;
    }

    @Override
    public int getDataBits() {
        if (serialPort != null && serialPort.isOpen())
            return serialPort.getNumDataBits();
        return 0;
    }

    @Override
    public int getStopBits() {
        if (serialPort != null && serialPort.isOpen())
            return serialPort.getNumStopBits();
        return 0;
    }

    @Override
    public int getParity() {
        if (serialPort != null && serialPort.isOpen())
            return serialPort.getParity();
        return 0;
    }
}
