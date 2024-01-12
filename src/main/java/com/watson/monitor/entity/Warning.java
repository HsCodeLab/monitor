package com.watson.monitor.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class Warning {
    private String sn;
    private Date time;
    private String code;
    private String info;
}
