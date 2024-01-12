package com.watson.monitor.layout;

import com.watson.monitor.annotations.ControlAnnotation;
import com.watson.monitor.factory.ComponentHandlerFactory;
import com.watson.monitor.maps.PageMap;
import com.watson.monitor.utils.JsonUtil;
import com.watson.monitor.wrappers.ControlWrapper;
import com.watson.monitor.wrappers.GridWrapper;
import com.watson.monitor.wrappers.LayoutWrapper;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.watson.monitor.handlers.HandlerCommon.temp_format;
import static com.watson.monitor.handlers.HandlerCommon.tmp_map;
import static com.watson.monitor.protocol.ProtocolUtil.protocol_type;
import static com.watson.monitor.utils.ControlUtil.createComponent;
import static com.watson.monitor.utils.LanUtil.*;

public class LayoutGrid extends VBox implements Layout {

    private ComboBox<String> comboBox = new ComboBox<>();
    //    private HBox hBox_header = new HBox();
    private HBox hBox = new HBox();

    private String group_name;

    //如果没有保存到Map里面就返回默认的list
    private List<HashMap<String, Object>> list = new ArrayList<>();

    public LayoutGrid(LayoutWrapper wrapper, Map<String, String> map) {
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

            BooleanProperty checked_property = new SimpleBooleanProperty();

//            BooleanProperty is_show_property = new SimpleBooleanProperty();

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

            if (Boolean.parseBoolean(map_obj.get("is_listen_tmp"))) {
                temp_format.addListener((observable, oldValue, newValue) -> unit.setText(tmp_map.get(temp_format.get())));
            }

            ControlWrapper controlWrapper = ControlWrapper.builder().groupName(group_name).handlerClass(wrapper.getHandlerClass()).map(map_obj).valueProperty(value_property).checkedProperty(checked_property).build();
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
            map_prop.put("checked_property", checked_property);
            map_prop.put("scale", scale);
            map_prop.put("type", map_obj.get("type"));
            map_prop.put("bytes", map_obj.get("bytes"));//取几个字节

            //需不需要加应用按钮,有的只是纯显示项
            if (StringUtils.isNotEmpty(map_obj.get("button"))) {
                Button button = new Button(getText(map_obj.get("button")));
                map_prop.put("button", button);//取几个字节

                if (StringUtils.isNotEmpty(map_obj.get("item_name"))) {
                    GridWrapper gridWrapper = GridWrapper.builder().groupName(wrapper.getGroupName())
                            .handlerClass(wrapper.getHandlerClass()).map(map_obj).control(component)
                            .button(button).build();
                    bindHandler(gridWrapper);
                }
            }

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
                String currentType = String.format("%02d", protocol_type.get());
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


        for (int i = 0; i < gridPane.length; i++) {
            if (gridPane.length > 1) {
                gridPane[i].prefWidthProperty().bind(hBox.widthProperty().divide(gridPane.length));
                gridPane[i].prefHeightProperty().bind(hBox.heightProperty());
            }
            hBox.getChildren().add(gridPane[i]);
        }
        this.getChildren().add(hBox);
    }

    private List<HashMap<String, Object>> getList(HashMap<String, List<HashMap<String, Object>>> typeToListMap) {
        String currentType = String.format("%02d", protocol_type.get());
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
                //如果是按钮则不显示名称和单位
                if (map1.get("type").equals("Button")) {
                    gridPane[currentColumn.get()].add((Node) map1.get("component"), 1, currentRow);
                } else {
                    Label name = (Label) map1.get("name");
                    Control control = (Control) map1.get("component");
                    Label unit = (Label) map1.get("unit");

                    gridPane[currentColumn.get()].add(name, 0, currentRow);
                    gridPane[currentColumn.get()].add(control, 1, currentRow);
                    gridPane[currentColumn.get()].add(unit, 2, currentRow);
                    if (map1.get("button") != null) {
                        Button button = (Button) map1.get("button");
                        gridPane[currentColumn.get()].add(button, 3, currentRow);
                    }
                }

                if (currentRow > 1 && currentRow % rows == 0) {
                    index.set(0);
                    currentColumn.getAndIncrement();
                }
            }
        }
    }

    public static void bindHandler(GridWrapper wrapper) {
        if (StringUtils.isNotEmpty(wrapper.getGroupName()) && StringUtils.isNotEmpty(wrapper.getHandlerClass())) {
            Map<String, String> map = wrapper.getMap();
            String item_name = map.get("item_name");
//            System.out.println("item_name=" + item_name);
            if (StringUtils.isNotEmpty(item_name)) {
                try {
                    Class<?> handlerClass = Class.forName(ComponentHandlerFactory.package_prefix + wrapper.getHandlerClass());
                    Method[] methods = handlerClass.getDeclaredMethods();

                    OUTER:
                    for (Method method : methods) {
//                        System.out.println("item_name=" + item_name);
//                        System.out.println("method="+method.getName());
                        if (method.isAnnotationPresent(ControlAnnotation.class)) {
                            ControlAnnotation annotation = method.getAnnotation(ControlAnnotation.class);
                            //如果注解是以数组的形式提供
                            String[] values = annotation.values();

                            if (values.length > 0) {
                                for (int i = 0; i < values.length; i++) {
                                    if (values[i].equals(item_name)) {
                                        System.out.println("数组中调用时的item_name=" + item_name);
                                        method.invoke(null, wrapper.getControl(), wrapper.getButton());
                                        break OUTER;
                                    }
                                }
                            } else {//注解以单个的形式提供
                                if (annotation.value().equals(item_name)) {
                                    // 匹配到了对应的方法，可以调用
                                    System.out.println("单值中调用时的item_name=" + item_name);
                                    method.invoke(null, wrapper.getControl(), wrapper.getButton());
                                    break OUTER;
                                }
                            }
                        } else {
//                            System.out.println("isAnnotationPresent 等于false");
                        }
                    }
//                    System.out.println("for循环遍历完成");
                } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException |
                         InvocationTargetException e) {
                    System.out.println("反射异常");
                    e.printStackTrace(); // 处理异常
                }
            }
        }
    }
}
