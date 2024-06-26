package com.hs.monitor.handlers;

import javafx.scene.control.Alert;

/**
 * 弹窗提示
 */
public class HsAlert {
    public static void alert(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        HandlerEmail.setIcon(alert);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    public static void alert(String title, String text, Alert.AlertType type) {
        Alert alert = new Alert(type);
        HandlerEmail.setIcon(alert);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
