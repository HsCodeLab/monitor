package com.watson.monitor.layout;

import com.watson.monitor.maps.PageMap;
import com.watson.monitor.utils.JsonUtil;
import com.watson.monitor.wrappers.ControlWrapper;
import com.watson.monitor.wrappers.LayoutWrapper;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.watson.monitor.protocol.ProtocolUtil.protocol_type;
import static com.watson.monitor.utils.ControlUtil.createComponent;
import static com.watson.monitor.utils.LanUtil.addLanguageListen;
import static com.watson.monitor.utils.LanUtil.getText;

public class LayoutGrid_O extends VBox implements Layout {

    private ComboBox<String> comboBox = new ComboBox<>();
    //    private HBox hBox_header = new HBox();
    private HBox hBox = new HBox();

    private String group_name = "";

    //如果没有保存到Map里面就返回默认的list
    private List<HashMap<String, Object>> list = new ArrayList<>();

    public LayoutGrid_O(LayoutWrapper wrapper, Map<String, String> map) {
        this.group_name = wrapper.getGroupName();

        if (StringUtils.isNotEmpty(map.get("protocol_type"))) {
            protocol_type.set(Integer.parseInt(map.get("protocol_type")));
        }

        Map<String, Object> map_item = JsonUtil.getMap(map.get("path"));

        int column;
        if (StringUtils.isNotEmpty(map.get("column"))) {
            column = Integer.parseInt(map.get("column"));
        } else {
            column = 1;
        }

//        if (StringUtils.isNotEmpty(map.get("column"))) {
//            Label label = new Label(getText("home.dev_option"));
//            addLanguageListen("home.dev_option", label);
//
//            // 设置 ComboBox 的默认选中索引为 0
//            comboBox.setItems(FXCollections.observableArrayList(getText("home.phase1"), getText("home.phase3")));
//            comboBox.getSelectionModel().select(0);
//
//            LAN.addListener((observable, oldValue, newValue) -> {
//                comboBox.setItems(FXCollections.observableArrayList(getText("home.phase1"), getText("home.phase3")));
//                // 设置 ComboBox 的默认选中索引为 0
//                comboBox.getSelectionModel().select(0);
//            });
//
//
//            //监听 ComboBox 的选项变化事件
//            comboBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
//                int index = newValue.intValue();
//                //重新设置comboBox的items时选中的索引会获取为-1,此时默认选中第0项
//                if (index == -1) {
//                    index = 0;
//                }
//                ProtocolUtil.type_property.set((map.get("protocol_types")).split(",")[index]);
//            });
//
//            Button button = new Button(getText("home.change_val"));
//            addLanguageListen("home.change_val", button);
//            button.setOnMouseClicked(event -> {
//                changeValue();
//            });
//
//            Button button1 = new Button(getText("home.add"));
//            addLanguageListen("home.add", button1);
//            DialogTool dialogTool = new DialogTool();
//            button1.setOnMouseClicked(event -> {
//                dialogTool.show();
//            });
//
//
//            hBox_header.setPadding(new Insets(10));
//            hBox_header.setSpacing(10);
//            hBox_header.setAlignment(Pos.CENTER_LEFT);
//
//            hBox_header.getChildren().add(label);
//            hBox_header.getChildren().add(comboBox);
//            hBox_header.getChildren().add(button);
//            hBox_header.getChildren().add(button1);

//        BackgroundFill backgroundFill = new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY);
//        Background background = new Background(backgroundFill);
//        this.setBackground(background);

        hBox.setPadding(new Insets(10));
        hBox.setSpacing(10);

        HashMap<String, List<HashMap<String, Object>>> typeToListMap = new HashMap<>();

        if (StringUtils.isNotEmpty(group_name)) {
            PageMap.map_data_page.put(group_name, typeToListMap);
        }

        GridPane[] gridPane = new GridPane[column];
        for (int i = 0; i < column; i++) {
            gridPane[i] = new GridPane();
            gridPane[i].setHgap(10);
            gridPane[i].setVgap(10);
        }

        map_item.forEach((key, value) -> {
            LinkedHashMap<String, String> map_obj = (LinkedHashMap<String, String>) value;

            StringProperty value_property = new SimpleStringProperty();
            value_property.set(map_obj.get("value"));

            // 文本
//                Label name = new Label(getText(map_obj.get("name")));
            // 文本
            Label name = new Label("");
            if (StringUtils.isNotEmpty(map_obj.get("name"))) {
                name = new Label(getText(map_obj.get("name")));
                addLanguageListen(map_obj.get("name"), name);
            }

            addLanguageListen(map_obj.get("name"), name);

            Label unit = new Label(map_obj.get("unit"));

            ControlWrapper controlWrapper = ControlWrapper.builder().groupName(group_name).handlerClass(wrapper.getHandlerClass()).map(map_obj).valueProperty(value_property).build();
            Control component = createComponent(map_obj.get("type"), controlWrapper);

            String scale;
            if (StringUtils.isNotEmpty(map_obj.get("scale"))) {
                scale = map_obj.get("scale");
            } else {
                scale = "1";
            }

            HashMap<String, Object> map_prop = new HashMap<>();
            map_prop.put("name", name);
            map_prop.put("unit", unit);
            map_prop.put("component", component);
            map_prop.put("value_property", value_property);
            map_prop.put("scale", scale);

            if (StringUtils.isNotEmpty(map_obj.get("protocol_types"))) {
                String[] protocolTypes = (map_obj.get("protocol_types")).split(",");

                for (String type : protocolTypes) {
                    typeToListMap.computeIfAbsent(type, k -> new ArrayList<>()).add(map_prop);
                }
            } else {
                list.add(map_prop);
            }
        });

        handleView(getList(typeToListMap), gridPane);

        protocol_type.addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                String currentType = String.format("%02d",protocol_type.get());
                List<HashMap<String, Object>> dataList = typeToListMap.get(currentType);
                if (dataList != null) {
                    for (int i = 0; i < dataList.size(); i++) {
                        HashMap<String, Object> map_obj = dataList.get(i);
                        if (map_obj != null) {
                            StringProperty value_property = (StringProperty) map_obj.get("value_property");
                            if (value_property != null)
                                value_property.set("0");
                        }
                    }
                    handleView(getList(typeToListMap), gridPane);
                }
            });
        });

//        hBox_items.prefWidthProperty().bind(this.widthProperty());
//        hBox_items.prefHeightProperty().bind(this.heightProperty());

        for (int i = 0; i < gridPane.length; i++) {
//            gridPane[i].prefWidthProperty().bind(hBox.widthProperty().divide(gridPane.length));
//            gridPane[i].prefHeightProperty().bind(hBox.heightProperty());
            hBox.getChildren().add(gridPane[i]);
        }
//        } else {
//            GridPane gridPane = new GridPane();
//            gridPane.setHgap(10);
//            gridPane.setVgap(10);
//
//            AtomicInteger i = new AtomicInteger();
//
//            map_item.forEach((key, value) -> {
//                //vo bl_in,vol_out等对象
//                LinkedHashMap<String, String> map_obj = (LinkedHashMap<String, String>) value;
//
//                // 文本
//                Label name = new Label("");
//                if (StringUtils.isNotEmpty(map_obj.get("name"))) {
//                    name = new Label(getText(map_obj.get("name")));
//                    addLanguageListen(map_obj.get("name"), name);
//                }
//
//                Label unit = new Label(map_obj.get("unit"));
//
//                gridPane.add(name, 0, i.get());
//                ControlWrapper controlWrapper = ControlWrapper.builder().groupName(group_name).handlerClass(wrapper.getHandlerClass()).map(map_obj).mapValue(null).build();
//                gridPane.add(createComponent(map_obj.get("type"), controlWrapper), 1, i.get());
//                gridPane.add(unit, 2, i.get());
//                i.getAndIncrement();
//            });
////            gridPane.prefWidthProperty().bind(this.widthProperty());
////            gridPane.prefHeightProperty().bind(this.heightProperty());
//            hBox_items.getChildren().add(gridPane);
//        }

//        this.getChildren().add(hBox_header);
//        hBox_items.widthProperty().
        this.getChildren().add(hBox);
    }

    private void changeValue() {
        String currentType = String.format("%02d",protocol_type.get());
        HashMap<String, List<HashMap<String, Object>>> typeToListMap = (HashMap<String, List<HashMap<String, Object>>>) PageMap.map_data_page.get(group_name);
        if (typeToListMap != null) {
            List<HashMap<String, Object>> dataList = typeToListMap.get(currentType);
            if (dataList != null) {
                for (int i = 0; i < dataList.size(); i++) {
                    HashMap<String, Object> map_obj = dataList.get(i);
                    StringProperty value_property = (StringProperty) map_obj.get("value_property");
                    value_property.set(Math.floor(Math.random() * 1000 + 1) + "");
                }
            } else {
//                System.out.println("dataList等于null");
            }
        } else {
//            System.out.println("typeToListMap等于null");
        }
    }

    private List<HashMap<String, Object>> getList(HashMap<String, List<HashMap<String, Object>>> typeToListMap) {
        String currentType = String.format("%02d",protocol_type.get());
        List<HashMap<String, Object>> dataList = typeToListMap.get(currentType);
        if (dataList != null) {
            return dataList;
        } else {
            return list;
        }
    }

    /**
     * 处理视图
     */
    private void handleView(List<HashMap<String, Object>> dataList, GridPane[] gridPane) {
        if (dataList != null) {
            for (int i = 0; i < gridPane.length; i++) {
                gridPane[i].getChildren().clear();
            }
            int size = dataList.size();
            int rows = (int) Math.floor((double) size / gridPane.length);

            if (size % gridPane.length == 0) {
                rows--;
            }

            AtomicInteger index = new AtomicInteger();
            AtomicInteger currentColumn = new AtomicInteger();

            for (int i = 0; i < size; i++) {
                // 渲染视图
                int currentRow = index.getAndIncrement();
                HashMap<String, Object> map1 = dataList.get(i);
                gridPane[currentColumn.get()].add((Node) map1.get("name"), 0, currentRow);
                gridPane[currentColumn.get()].add((Node) map1.get("component"), 1, currentRow);
                gridPane[currentColumn.get()].add((Node) map1.get("unit"), 2, currentRow);

                if (currentRow > 1 && currentRow % rows == 0) {
                    index.set(0);
                    currentColumn.getAndIncrement();
                }
            }
        }
    }
}
