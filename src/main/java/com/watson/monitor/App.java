package com.watson.monitor;

import atlantafx.base.theme.PrimerLight;
import com.watson.monitor.component.IPage;
import com.watson.monitor.factory.PageHandlerFactory;
import com.watson.monitor.handlers.HandlerSerial;
import com.watson.monitor.protocol.ProtocolUtil;
import com.watson.monitor.utils.TaskUtil;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static com.watson.monitor.function.MenuFunction.invokeFunction;
import static com.watson.monitor.utils.JsonUtil.*;
import static com.watson.monitor.utils.LanUtil.*;

/**
 * Hello world!
 */
public class App extends Application {

    public static Node right_node;//右侧显示的项

    // 创建工具栏
    private static ToolBar toolBar = new ToolBar();
    // 创建菜单栏
    private static MenuBar menuBar = new MenuBar();

    public static BorderPane root = new BorderPane();

    @Override
    public void start(Stage primaryStage) {
//        history = new History();

        createMenuBar();

        createToolBar();

        // 将菜单栏和工具栏放在一个 VBox 中
        VBox topBox = new VBox(menuBar, toolBar);
        topBox.setSpacing(10);
        topBox.setPadding(new Insets(10));

        // 创建左侧导航栏（树形节点）
        TreeItem<String> rootNode = new TreeItem<>("Navigation");
        TreeItem<String> bigNode1 = new TreeItem<>("实验室");
        addLanguageListen("home.lab", bigNode1);

        TreeItem<String> bigNode2 = new TreeItem<>("工厂");
        addLanguageListen("home.factory", bigNode2);


        TreeItem<String> smallNode1_1 = new TreeItem<>("UPS-1");
        TreeItem<String> smallNode1_2 = new TreeItem<>("UPS-2");
        TreeItem<String> smallNode2_1 = new TreeItem<>("UPS-1");
        TreeItem<String> smallNode2_2 = new TreeItem<>("UPS-2");
        bigNode1.getChildren().addAll(smallNode1_1, smallNode1_2);
        bigNode2.getChildren().addAll(smallNode2_1, smallNode2_2);
        rootNode.getChildren().addAll(bigNode1, bigNode2);


        TreeView<String> navTreeView = new TreeView<>(rootNode);
        navTreeView.setShowRoot(false); // 不显示根节点

        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem1 = new MenuItem("选项 1");
        MenuItem menuItem2 = new MenuItem("选项 2");
        contextMenu.getItems().addAll(menuItem1, menuItem2);

        navTreeView.setContextMenu(contextMenu);

        // 添加鼠标点击事件监听器
        navTreeView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                TreeItem<String> selectedItem = navTreeView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    System.out.println("双击的值为: " + selectedItem.getValue());
//                    right_node = details;
//                    root.setCenter(right_node);
                }
            }
        });


        VBox navBar = new VBox(navTreeView);
        navBar.setPadding(new Insets(10));
        navBar.setSpacing(10);

        right_node = (Node) map_control_toolbar.get("home");
        // 设置布局
        root.setTop(topBox);
        root.setLeft(navBar);
        root.setCenter(right_node);


        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        // 设置主场景
        Scene scene = new Scene(root, 1080, 600);

        root.getStyleClass().add("background");


        //设置背景图
//        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        //应用名称
        primaryStage.setTitle("代码工厂");
        // 设置应用图标
        primaryStage.getIcons().add(new Image("logo.png"));

        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            // 停止后台线程的逻辑
            HandlerSerial.isStop = true;
            //关闭线程池
            TaskUtil.getExecutor().shutdown();
            // 关闭窗口
            primaryStage.close();
        });
    }

//    public static History history;

    private void createToolBar() {
        map_toolbar.forEach((key, value) -> {
            LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) value;
            Button btn = new Button(getText(map.get("name")));

            addLanguageListen(map.get("name"), btn);

            toolBar.getItems().addAll(btn);
            btn.setOnAction(e -> {
                // 从内存中获取页面对象并在界面中显示
                IPage selectedPage = map_control_toolbar.get(key);

                if (map.get("cmd_read") != null) {
                    ProtocolUtil.cmd_read = map.get("cmd_read");
                    ProtocolUtil.group_name = map.get("group_name");
                }

//                if (StringUtils.equals(key, "history")) {
//                    root.setCenter(history);
//                } else {
                if (selectedPage != null) {
                    PageHandlerFactory.handlePage(selectedPage);
                }
//                }
            });
        });
    }

    private void createMenuBar() {

        map_menubar.forEach((key, value) -> {
            LinkedHashMap<String, Object> map_menu1 = (LinkedHashMap<String, Object>) value;


            Menu menu = new Menu(getText((String) map_menu1.get("name")));

            addLanguageListen((String) map_menu1.get("name"), menu);


//            LAN.addListener((observable, oldValue, newValue) -> menu.setText(getText((String) map_menu1.get("name"))));
            if ("language".equals(key)) {
                LinkedHashMap<String, Object> map_lans = (LinkedHashMap<String, Object>) map_lan.get("lans");
                map_lans.forEach((key_lan, value_lan) -> {
                    HashMap<String, String> map = (HashMap<String, String>) value_lan;
                    // 创建语言选择菜单项
                    MenuItem menuItem = new MenuItem(map.get("name"));
                    menuItem.setOnAction(event -> LAN.set(map.get("key")));
                    menu.getItems().add(menuItem);
                });
            } else {
                LinkedHashMap<String, Object> map_menu2 = (LinkedHashMap<String, Object>) map_menu1.get("items");
                map_menu2.forEach((key1, value1) -> {

                    LinkedHashMap<String, String> map_menu3 = (LinkedHashMap<String, String>) value1;

                    MenuItem menuItem = new MenuItem(getText(map_menu3.get("name")));
//                    LAN.addListener((observable, oldValue, newValue) -> menuItem.setText(getText(map_menu3.get("name"))));
                    addLanguageListen(map_menu3.get("name"), menuItem);

                    menu.getItems().add(menuItem);
                    menu.getItems().add(new SeparatorMenuItem());

                    //路径不为空,说明是要跳转页面
                    String path = map_menu3.get("path");

                    menuItem.setOnAction(e -> {
                        if (StringUtils.isNotEmpty(path)) {
                            // 从内存中获取页面对象并在界面中显示
                            IPage selectedPage = map_control_menubar.get(key1);
                            if (selectedPage != null) {
                                PageHandlerFactory.handlePage(selectedPage);
                            }
                        } else {
                            if (StringUtils.isNotEmpty(map_menu3.get("function"))) {
                                invokeFunction(map_menu3.get("function"), key1);
                            }
                        }
                    });
                });
            }

            menuBar.getMenus().add(menu);
        });

//        menuItem_data1.setOnAction(e -> {
//            // 在这里编写处理实时数据菜单项点击的代码
//            System.out.println("实时数据 菜单项被点击了！");
//            right_node = home;
//            root.setCenter(right_node);
//        });

    }

    public static void main(String[] args) {
        launch(args);
    }

}
