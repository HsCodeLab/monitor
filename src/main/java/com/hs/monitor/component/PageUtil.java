package com.hs.monitor.component;

import com.hs.monitor.layout.Layout;
import com.hs.monitor.layout.LayoutFactoryPage;
import com.hs.monitor.layout.LayoutFactory;
import com.hs.monitor.wrappers.LayoutWrapper;
import com.hs.monitor.wrappers.PageWrapper;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PageUtil {
    public static Map<Class<? extends Pane>, Consumer<PageWrapper>> paneHandlers = new HashMap<>();

    static {
        paneHandlers.put(GridPane.class, PageUtil::addGridPane);
        paneHandlers.put(HBox.class, PageUtil::addHBox);
        paneHandlers.put(VBox.class, PageUtil::addVBox);
    }

    /**
     * 生成页面的方法
     */
    public static void handlePage(Map<String, Object> map, Pane container) {
//        HBox hBox = new HBox();
//        ComboBox<String> comboBox = new ComboBox<>();
//        ArrayList<String> options = new ArrayList<>(Arrays.asList("横排","竖排","复制一页","复制一列","复制一行","可编辑"));
//        comboBox.setItems(FXCollections.observableList(options));
//        comboBox.setValue("横排");
        String group_name = (String) map.get("group_name");
        String handler_class = (String) map.get("handler_class");
        Pane pane = LayoutFactoryPage.createLayout((String) map.get("layout"));
        Map<String, Object> map_items = (Map<String, Object>) map.get("items");

        if (map_items == null) {
            HashMap<String, Object> map_items_1 = new HashMap<>();
            map_items_1.put("key1", map);
            map_items = map_items_1;
        }

        PageWrapper wrapper = PageWrapper.builder().groupName(group_name).handlerClass(handler_class).map(map_items).container(container).build();
        Consumer<PageWrapper> consumer = paneHandlers.get(pane.getClass());
        if (consumer != null) {
            consumer.accept(wrapper);
        } else {
            // Handle unknown pane type, or provide a default behavior
        }
    }


    public static void addItemToPane(PageWrapper wrapper, Pane pane, BiConsumer<Node, Integer> consumer) {

        pane.setPadding(new Insets(10));

        AtomicInteger index = new AtomicInteger();
        String groupName = wrapper.getGroupName();
        String handlerClass = wrapper.getHandlerClass();
        Pane container = wrapper.getContainer();
        Map<String, Object> map = wrapper.getMap();

        pane.prefWidthProperty().bind(wrapper.getContainer().widthProperty());

        map.forEach((key, value) -> {
            Map<String, String> mapItem = (Map<String, String>) value;
            LayoutWrapper layoutWrapper = LayoutWrapper.builder().groupName(groupName).handlerClass(handlerClass).build();
            Layout layout = LayoutFactory.createLayout(layoutWrapper, mapItem);

            ((Pane)layout).prefWidthProperty().bind(pane.widthProperty());

            consumer.accept((Node) layout, index.get());

            index.getAndIncrement();
        });


        if (container instanceof Dialog) {
            pane.setMinWidth(400);
            ((Dialog) container).getDialog().getDialogPane().setContent(pane);
        } else {
            container.getChildren().add(pane);
        }
    }

    public static void addGridPane(PageWrapper wrapper) {
        GridPane gridPane = new GridPane();
//        gridPane.prefWidthProperty().bind(wrapper.getContainer().widthProperty());
//        gridPane.prefHeightProperty().bind(wrapper.getContainer().heightProperty());
        addItemToPane(wrapper, gridPane, (node, index) -> gridPane.add(node, index, 0));
    }

    public static void addHBox(PageWrapper wrapper) {
        HBox hBox = new HBox();
//        hBox.prefWidthProperty().bind(wrapper.getContainer().widthProperty());
//        hBox.prefHeightProperty().bind(wrapper.getContainer().heightProperty());
        hBox.setSpacing(10);

        addItemToPane(wrapper, hBox, (node, index) -> hBox.getChildren().add(node));

    }

    public static void addVBox(PageWrapper wrapper) {
        VBox vBox = new VBox();
//        vBox.prefWidthProperty().bind(wrapper.getContainer().widthProperty());
//        vBox.prefHeightProperty().bind(wrapper.getContainer().heightProperty());
        vBox.setSpacing(10);
        addItemToPane(wrapper, vBox, (node, index) -> vBox.getChildren().add(node));
    }
}

