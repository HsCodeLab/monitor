package com.watson.monitor.handlers;

import com.watson.monitor.annotations.ControlAnnotation;
import com.watson.monitor.utils.EmailUtil;
import com.watson.monitor.utils.TaskUtil;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 发送邮件页面控件的处理器类
 */
public class HandlerEmail {

    private static String recipient;
    private static String subject = "设备告警";
    private static String content = "UPS 电压异常";

    @ControlAnnotation("recipient")
    public static void handleRecipient(TextField textField) {
        recipient = textField.getText();
        System.out.println("handleRecipient函数中recipient=" + recipient);
    }

    @ControlAnnotation("send")
    public static void handleSend(Button button) {
        button.setOnAction(event -> {

            //发送过程中如果网络有问题,会卡顿,因此发邮件不能放在UI线程中
            TaskUtil.getExecutor().execute(() -> {
                try {
                    EmailUtil.send(subject, content, recipient);
                } catch (Exception e) {
                    e.printStackTrace();
                    //弹窗又放回到UI线程中
                    Platform.runLater(() -> showAlert(e));
                }

            });


        });
    }

    public static void setIcon(Alert alert){
        //设置左上角小图标
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("logo.png"));
    }

    public static void showAlert(Exception e) {
        // 创建一个信息类型的 Alert
        Alert alert = new Alert(Alert.AlertType.ERROR);
        setIcon(alert);
        alert.setTitle("提示");
        alert.setHeaderText("邮件发送失败"); // 可以设置标题，也可以设置为 null

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exceptionText = sw.toString();
        Label label = new Label("失败原因:");
        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);
        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }
}
