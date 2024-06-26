module com.hs.monitor {
    requires javafx.controls;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires org.apache.commons.lang3;
    requires org.controlsfx.controls;
    requires atlantafx.base;
    requires java.desktop;
    requires com.jfoenix;
    requires com.h2database;
    requires org.mybatis;
//    requires mybatis.generator.core;
    requires pagehelper;
    requires java.sql;
    requires static lombok;
    requires org.slf4j;
    requires com.fazecast.jSerialComm;
    requires usb4java;
    requires modbus4j;
    requires org.snmp4j;
    requires java.mail;
    requires activation;
    requires java.datatransfer;
    opens com.hs.monitor.mapper;
    opens com.hs.monitor.entity;
    exports com.hs.monitor;
}