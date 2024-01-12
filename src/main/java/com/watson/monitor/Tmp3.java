package com.watson.monitor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.watson.monitor.utils.JsonUtil;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static com.watson.monitor.utils.JsonUtil.*;

@Slf4j
public class Tmp3 extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Internationalization Example");

        // 创建菜单
        MenuBar menuBar = createMenuBar();

        // 创建两个 Label
//        Label label1 = new Label(getText("home.vol"));
//        Label label2 = new Label(getText("home.cur"));

        Label label1 = (Label) createComponent("Label", "home.vol");
        Label label2 = (Label) createComponent("Label", "home.cur");

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(20));
        hBox.setSpacing(100);
        hBox.getChildren().addAll(label1, label2);
        // 布局
        VBox vBox = new VBox(menuBar, hBox);
        Scene scene = new Scene(vBox, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private MenuBar createMenuBar() {
        // 创建语言选择菜单
        Menu languageMenu = new Menu(getText("home.lan"));

        lan.addListener((observable, oldValue, newValue) -> languageMenu.setText(getText("home.lan")));


        LinkedHashMap<String, Object> map_lans = (LinkedHashMap<String, Object>) map_lan.get("lans");
        map_lans.forEach((key, value) -> {
            HashMap<String, String> map = (HashMap<String, String>) value;
            // 创建语言选择菜单项
            MenuItem menuItem = new MenuItem(map.get("name"));
            menuItem.setOnAction(event -> lan.set(map.get("key")));
            languageMenu.getItems().add(menuItem);
        });

//        MenuItem englishMenuItem = new MenuItem("English");
//        englishMenuItem.setOnAction(event -> lan.set("en"));
//
//
//        // 添加菜单项到语言选择菜单
//        languageMenu.getItems().addAll(chineseMenuItem, englishMenuItem);

        // 创建菜单栏
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(languageMenu);

        return menuBar;
    }

    private static String getText(String keyName) {
        String path = "/lan/" + lan.get();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = null;
        try {
            node = objectMapper.readTree(new File(JsonUtil.class.getResource(path + SUFFIX).getPath()));
        } catch (Exception e) {
//            throw new RuntimeException(e);
            String file = lan.get() + SUFFIX;
            log.info("找不到对应文件" + file + ",请确保lan目录下有" + file + "文件");
            return null;
        }

        String[] parts = keyName.split("\\.");

        for (String part : parts) {
            if (node.has(part)) {
                node = node.get(part);
            } else {
                // 路径无效或节点不存在
                log.info("找不到" + keyName + "对应的文本");
                return null;
            }
        }

        if (node.isTextual()) {
            return node.asText();
        } else {
            // 节点不是文本节点
            log.info(keyName + "对应的不是文本节点");
            return null;
        }
    }

    public static Control createComponent(String className, String key) {
        try {
            String full_name = CTRL_PREFIX + className;
            Class<?> componentClass = Class.forName(full_name);
            Control control = (Control) componentClass.getDeclaredConstructor().newInstance();

            handleComponent(className, key, control);

            return control;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static StringProperty lan = new SimpleStringProperty();
    private static Map<String, Object> map_lan = new HashMap<>();

    private static final Map<String, BiConsumer<String, Control>> handlerMap = new HashMap<>();

    static {
        map_lan = getMap("/lan/lan");

        lan.set((String) map_lan.get("default"));

        handlerMap.put("Label", (key, control) -> {
            Label label = (Label) control;
            label.setText(getText(key));
            lan.addListener((observable, oldValue, newValue) -> {
                label.setText(getText(key));
            });
        });
    }

    public static void handleComponent(String className, String key, Control control) {
        BiConsumer<String, Control> consumer = handlerMap.get(className);
        if (consumer != null) {
            consumer.accept(key, control);
        }
    }

}
