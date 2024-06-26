package com.hs.monitor.entity;

import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Builder
@Data
public class History {
    private int id;
    private String sn;
    private Date date;
    private double voltageInput;
    private double voltageOutput;

    private double frequencyInput;
    private double frequencyOutput;

    private double batteryVoltage;
    private int batteryLevel;

    private int temperature;
    private int outputLoadPercent;
}
