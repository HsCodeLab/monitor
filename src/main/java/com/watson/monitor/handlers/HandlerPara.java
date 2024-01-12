package com.watson.monitor.handlers;

import com.watson.monitor.annotations.ControlAnnotation;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;

/**
 * serial串口设置页面控件的处理器类
 */
public class HandlerPara {

    @ControlAnnotation(values = {"vol_bypass", "vol_bypass_a", "vol_bypass_b", "vol_bypass_c", "fre_in", "bat_num"})
    public static void handleSpinner(Spinner<?> spinner, Button button) {
        button.setOnAction(e -> {
            Double value = (Double) spinner.getValueFactory().getValue();
            System.out.println("获取的value=" + value);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            HandlerEmail.setIcon(alert);
            alert.setTitle("参数设置");
            alert.setHeaderText(null);
            alert.setContentText("设置成功");
            alert.showAndWait();
        });
    }

    @ControlAnnotation(values = {"board_ctrl", "sound_alert", "timer_switch", "bat_check", "net_wake"})
    public static void handleCheckBox(CheckBox checkBox, Button button) {
        button.setOnAction(e -> {
            int value = checkBox.selectedProperty().get() ? 1 : 0;
            System.out.println("获取的value=" + value);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            HandlerEmail.setIcon(alert);
            alert.setTitle("参数设置");
            alert.setHeaderText(null);
            alert.setContentText("设置成功");
            alert.showAndWait();
        });
    }

}
