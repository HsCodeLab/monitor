package com.watson.monitor.pages;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class Home extends Group {
    public Home(){
//        Group root = new Group();

        // 创建电源、UPS、电池和负载图形节点
        Rectangle ac_ac = createNode(200, 100, Color.GREEN);

        Rectangle ac = createNode(50, 200, Color.LIGHTGREY);
        Rectangle ac_dc = createNode(150, 200, Color.LIGHTGREY);
        Rectangle dc_ac = createNode(250, 200, Color.LIGHTGREY);
        Rectangle load = createNode(350, 200, Color.LIGHTGREY);

        Rectangle battery = createNode(200, 300, Color.BLUE);

//        // 创建十字形电线路径
//        Path path = new Path();
//        path.getElements().addAll(
//                new MoveTo(200, 50),
//                new LineTo(200, 350),
//                new MoveTo(50, 200),
//                new LineTo(350, 200));, path

        this.getChildren().addAll(ac_ac, ac, ac_dc, dc_ac, load, battery);

        // 创建电线边框
        Path lineBorder = new Path();
        lineBorder.getElements().addAll(
                new MoveTo(200, 100),
                new LineTo(200, 300),

                new MoveTo(100, 200),
                new LineTo(100, 100),

                new MoveTo(100, 100),
                new LineTo(300, 100),

                new MoveTo(300, 100),
                new LineTo(300, 200),


                new MoveTo(50, 200),
                new LineTo(350, 200),
                new ClosePath());
        lineBorder.setStrokeWidth(2);
        lineBorder.setStroke(Color.LIGHTGREY);

//        // 创建电线发光效果
//        Glow glow = new Glow(2.0); // 将 level 调整为 1.0，使发光效果更明显
//        Rectangle glowRect = new Rectangle(400, 400);
//        glowRect.setFill(null);
//        glowRect.setStroke(null);
//        glowRect.setEffect(glow);
//
//        // 创建电流图形节点
//        Rectangle current = createNode(50, 200, Color.LIGHTBLUE);
//        current.setWidth(5);
//        current.setHeight(5);
//
//        // 创建路径动画
//        PathTransition pathTransition = new PathTransition();
//        pathTransition.setDuration(Duration.seconds(10));
//        pathTransition.setNode(current);
//        pathTransition.setPath(lineBorder);
//        pathTransition.setCycleCount(PathTransition.INDEFINITE);
//        pathTransition.setAutoReverse(true);
//
//        // 开始动画
//        pathTransition.play();, glowRect, current

        this.getChildren().addAll(lineBorder);

//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Electricity Flow Animation (JavaFX)");
//        primaryStage.show();
    }

    private Rectangle createNode(double x, double y, Color color) {
        Rectangle node = new Rectangle(20, 20);
        node.setX(x - 10);
        node.setY(y - 10);
        node.setFill(color);
        return node;
    }
}
