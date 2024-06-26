package com.hs.monitor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hs.monitor.utils.JsonUtil;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static com.hs.monitor.utils.JsonUtil.*;

@Slf4j
public class Tmp3 extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 创建数据列表
        ObservableList<String> items = FXCollections.observableArrayList(
                "Item 1", "Item 2", "Item 3", "Item 4", "Item 5");

        // 创建ListView并设置数据源
        ListView<String> listView = new ListView<>(items);

        // 设置ListView为可编辑状态，使用TextFieldListCell来编辑
        listView.setEditable(true);
        listView.setCellFactory(TextFieldListCell.forListView());

        // 设置根布局
        StackPane root = new StackPane();
        root.getChildren().add(listView);

        // 创建场景并设置根节点
        Scene scene = new Scene(root, 300, 250);

        // 设置舞台的标题和场景
        primaryStage.setTitle("Editable ListView Example");
        primaryStage.setScene(scene);

        // 显示舞台
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
