package com.watson.monitor;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Tmp2 extends Application {
    @Override
    public void start(Stage primaryStage) {
        ProgressBar progressBar = new ProgressBar(0);

        // 创建时间轴（Timeline）来控制动画
        Timeline timeline = new Timeline(
                // 使用 KeyFrame 定义动画的关键帧
                new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(3), new KeyValue(progressBar.progressProperty(), 1))
        );

        // 设置动画循环次数，CycleCount.INDEFINITE 表示无限循环
        timeline.setCycleCount(Timeline.INDEFINITE);

        // 播放动画
        timeline.play();

        StackPane root = new StackPane();
        root.getChildren().add(progressBar);

        Scene scene = new Scene(root, 300, 200);

        primaryStage.setTitle("ProgressBar Animation Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
