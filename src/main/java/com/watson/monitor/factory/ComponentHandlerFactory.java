package com.watson.monitor.factory;

import com.watson.monitor.annotations.ControlAnnotation;
import com.watson.monitor.maps.PageMap;
import com.watson.monitor.wrappers.ControlWrapper;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.CheckComboBox;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static com.watson.monitor.service.HistoryService.getTableTotal;
import static com.watson.monitor.utils.LanUtil.*;
import static com.watson.monitor.utils.TableUtil.createPage;

public class ComponentHandlerFactory {
    private static final Map<String, Consumer<ControlWrapper>> handlerMap = new HashMap<>();

    static {
        handlerMap.put("Label", (wrapper) -> {
            Label label = (Label) wrapper.getControl();
            label.setText(getText(wrapper.getMap().get("value")));
            addLanguageListen(wrapper.getMap().get("value"), label);

            showControl(wrapper, wrapper.getControl());

            handleCycle(wrapper);
        });

        handlerMap.put("Button", (wrapper) -> {
            Button button = (Button) wrapper.getControl();
            button.setText(getText(wrapper.getMap().get("value")));

            addLanguageListen(wrapper.getMap().get("value"), button);

            showControl(wrapper, wrapper.getControl());

            handleCycle(wrapper);

            bindHandler(wrapper);
        });

        handlerMap.put("Hyperlink", (wrapper) -> {
            Hyperlink hyperlink = (Hyperlink) wrapper.getControl();
            hyperlink.setText(wrapper.getMap().get("value"));
            hyperlink.setOnAction(e -> openLink(wrapper.getMap().get("value")));
            showControl(wrapper, wrapper.getControl());
        });

        handlerMap.put("TextField", (wrapper) -> {
            TextField textField = (TextField) wrapper.getControl();
            if (wrapper.getValueProperty() != null) {
                textField.textProperty().bindBidirectional(wrapper.getValueProperty());
            } else {
                textField.setText(wrapper.getMap().get("value"));
            }

//            textField.setDisable(false);
            textField.setEditable(Boolean.parseBoolean(wrapper.getMap().get("editable")));
//            BooleanProperty is_show_property = new SimpleBooleanProperty();
//            Boolean isShow = Boolean.parseBoolean(map.get("isShow"));
//            is_show_property.set(isShow);
//            textField.visibleProperty().bind(is_show_property);
//            textField.managedProperty().bind(is_show_property);
            showControl(wrapper, wrapper.getControl());

            handleCycle(wrapper);

            bindHandler(wrapper);
        });

        handlerMap.put("CheckBox", (wrapper) -> {
            CheckBox checkBox = (CheckBox) wrapper.getControl();
//            checkBox.setSelected(wrapper.getMap().get("value").equals("1"));

            if (wrapper.getCheckedProperty() != null) {
                BooleanProperty checked_property = wrapper.getCheckedProperty();
                checkBox.selectedProperty().bindBidirectional(checked_property);

            } else {
                checkBox.setSelected(wrapper.getMap().get("value").equals("1"));
            }

            showControl(wrapper, wrapper.getControl());

            handleCycle(wrapper);
        });

        handlerMap.put("ComboBox", (wrapper) -> {
            ComboBox<String> comboBox = (ComboBox<String>) wrapper.getControl();
            String items = wrapper.getMap().get("items");

            if (StringUtils.isNotEmpty(wrapper.getMap().get("is_need_translate"))) {
                String[] arr = items.split(",");
                String[] arr1 = new String[arr.length];
                for (int i = 0; i < arr1.length; i++) {
                    arr1[i] = getText(arr[i]);
                }

                LAN.addListener((observable, oldValue, newValue) -> {
                    String[] arr2 = new String[arr.length];
                    for (int i = 0; i < arr1.length; i++) {
                        arr2[i] = getText(arr[i]);
                    }
                    comboBox.setItems(FXCollections.observableArrayList(arr2));
                    // 设置 ComboBox 的默认选中索引为 0
                    comboBox.getSelectionModel().select(0);
                });
                comboBox.setValue(getText(wrapper.getMap().get("value")));
                comboBox.setItems(FXCollections.observableArrayList(arr1));
            } else {
                String[] arr = items.split(",");
                comboBox.setValue(wrapper.getMap().get("value"));
                comboBox.setItems(FXCollections.observableArrayList(arr));
            }

            showControl(wrapper, wrapper.getControl());

            handleCombo(wrapper);

            handleCycle(wrapper);

            bindHandler(wrapper);
        });

        handlerMap.put("CheckComboBox", (wrapper) -> {
            CheckComboBox<String> checkComboBox = (CheckComboBox<String>) wrapper.getControl();
            String items = wrapper.getMap().get("items");
            String[] arr = items.split(",");
            checkComboBox.getItems().addAll(FXCollections.observableArrayList(arr));
            checkComboBox.getCheckModel().checkIndices(0, 2);
            showControl(wrapper, wrapper.getControl());

            handleCycle(wrapper);
        });

        handlerMap.put("Spinner", (wrapper) -> {
            Spinner<Double> spinner = (Spinner<Double>) wrapper.getControl();

            if (wrapper.getValueProperty() != null) {
                SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(
                        Double.parseDouble(wrapper.getMap().get("min")),
                        Double.parseDouble(wrapper.getMap().get("max")),
                        Double.parseDouble(wrapper.getMap().get("value")),
                        Double.parseDouble(wrapper.getMap().get("step"))
                );
                spinner.setValueFactory(valueFactory);
                Optional.ofNullable(wrapper.getValueProperty())
                        .ifPresent(valueProperty -> valueProperty.addListener((observable, oldValue, newValue) -> {
                            Double value;
                            try {
                                value = Double.parseDouble(valueProperty.get());
                            } catch (Exception e) {
                                value = 0.0;
                            }
                            spinner.getValueFactory().valueProperty().set(value);
                        }));

                // 将 StringProperty 转换为 ObjectProperty<Double>
//                ObjectProperty<Double> doubleProperty = new SimpleObjectProperty<>();
//                doubleProperty.bind(Bindings.createObjectBinding(
//                        () -> {
//                            try {
//                                // 尝试将字符串转换为 Double
//                                return Double.parseDouble(map_value.get("value_property").get());
//                            } catch (NumberFormatException e) {
//                                // 转换失败返回 null
//                                return null;
//                            }
//                        },
//                        map_value.get("value_property")
//                ));
            } else {
                SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(
                        Double.parseDouble(wrapper.getMap().get("min")),
                        Double.parseDouble(wrapper.getMap().get("max")),
                        Double.parseDouble(wrapper.getMap().get("value")),
                        Double.parseDouble(wrapper.getMap().get("step"))
                );
                spinner.setValueFactory(valueFactory);
            }

            spinner.setEditable(Boolean.parseBoolean(wrapper.getMap().get("editable")));

            showControl(wrapper, wrapper.getControl());

            handleCycle(wrapper);
        });

        handlerMap.put("DatePicker", (wrapper) -> {
            DatePicker datePicker = (DatePicker) wrapper.getControl();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate initialDate = LocalDate.parse(wrapper.getMap().get("value"), formatter);
            datePicker.setValue(initialDate);
            // 自定义日期格式
            StringConverter<LocalDate> converter = new StringConverter<>() {
                final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                @Override
                public String toString(LocalDate date) {
                    if (date != null) {
                        return dateFormatter.format(date);
                    } else {
                        return "";
                    }
                }

                @Override
                public LocalDate fromString(String string) {
                    if (string != null && !string.isEmpty()) {
                        return LocalDate.parse(string, dateFormatter);
                    } else {
                        return null;
                    }
                }
            };

            datePicker.setConverter(converter);
            showControl(wrapper, wrapper.getControl());

            handleCycle(wrapper);
        });

        //表格分页组件
        handlerMap.put("Pagination", (wrapper) -> {
            IntegerProperty totalItems = new SimpleIntegerProperty(getTableTotal());
            IntegerProperty itemsPerPage = new SimpleIntegerProperty(10);

            totalItems.set(Integer.parseInt(wrapper.getMap().get("total")));
            itemsPerPage.set(Integer.parseInt(wrapper.getMap().get("per_page")));

            Pagination pagination = (Pagination) wrapper.getControl();
            pagination.pageCountProperty().bind(totalItems.add(itemsPerPage).subtract(1).divide(itemsPerPage));
            pagination.setCurrentPageIndex(0);
            showControl(wrapper, wrapper.getControl());

            handlePagination(wrapper, totalItems, itemsPerPage);
        });
    }

    public static void handleComponent(String className, ControlWrapper wrapper) {
        Consumer<ControlWrapper> handler = handlerMap.get(className);
        if (handler != null) {
            handler.accept(wrapper);
        }
    }

    /**
     * 显示控件
     *
     * @param wrapper
     * @param node
     */
    public static void showControl(ControlWrapper wrapper, Node node) {
        BooleanProperty is_show_property = new SimpleBooleanProperty();
        Boolean isShow;
        if (StringUtils.isEmpty(wrapper.getMap().get("isShow"))) {
            isShow = true;
        } else {
            isShow = Boolean.parseBoolean(wrapper.getMap().get("isShow"));
        }

        is_show_property.set(isShow);
        node.visibleProperty().bind(is_show_property);
        node.managedProperty().bind(is_show_property);
    }

    public static String package_prefix = "com.watson.monitor.handlers.";

    /**
     * 绑定处理器函数
     * 为什么需要这个?
     * 所有组件都是根据JSON配置动态反射生成,需要绑定如按钮的点击函数之类的
     * JSON页面层级的配置中有一个handler_class处理器类,
     * 这个就是页面对应的处理器类名,各个组件需要绑定到类中的各个函数
     * group_name:组名,每个页面有一个唯一的group_name,页面的唯一标识
     * item_name:页面中各个组件的唯一标识
     */
    public static void bindHandler(ControlWrapper wrapper) {
        if (StringUtils.isNotEmpty(wrapper.getGroupName()) && StringUtils.isNotEmpty(wrapper.getHandlerClass())) {
            Map<String, String> map = wrapper.getMap();
            String item_name = map.get("item_name");
//            System.out.println("item_name=" + item_name);
            if (StringUtils.isNotEmpty(item_name)) {
                try {
                    Class<?> handlerClass = Class.forName(package_prefix + wrapper.getHandlerClass());
                    Method[] methods = handlerClass.getDeclaredMethods();

                    OUTER:
                    for (Method method : methods) {
//                        System.out.println("method名=" + method.getName());
                        if (method.isAnnotationPresent(ControlAnnotation.class)) {
                            ControlAnnotation annotation = method.getAnnotation(ControlAnnotation.class);
                            //如果注解是以数组的形式提供
                            String[] values = annotation.values();

                            if (values.length > 0) {
                                for (int i = 0; i < values.length; i++) {
                                    if (values[i].equals(item_name)) {
                                        System.out.println("数组中调用时的item_name=" + item_name);
                                        method.invoke(null, wrapper.getControl());
                                        break OUTER;
                                    }
                                }
                            } else {//注解以单个的形式提供
                                if (annotation.value().equals(item_name)) {
                                    // 匹配到了对应的方法，可以调用
                                    System.out.println("单值中调用时的item_name=" + item_name);
                                    method.invoke(null, wrapper.getControl());
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
//                    System.out.println("反射异常");
                    e.printStackTrace(); // 处理异常
                }
            }
        }
    }

    /**
     * 处理下拉框相关逻辑
     *
     * @param wrapper
     */
    private static void handleCombo(ControlWrapper wrapper) {
        ComboBox<String> comboBox = (ComboBox<String>) wrapper.getControl();
        String group_name = wrapper.getGroupName();
        if (StringUtils.isNotEmpty(wrapper.getMap().get("group_name")) && StringUtils.isEmpty(wrapper.getMap().get("member_name"))) {
            if (PageMap.map_history.containsKey(group_name)) {
                HashMap<String, Object> map = PageMap.map_history.get(group_name);
                map.put(wrapper.getMap().get("group_name"), comboBox);

                if (wrapper.getMap().get("group_name").equals("page_num")) {
                    // 监听值的变化
                    HashMap<String, Object> map_pagination = (HashMap<String, Object>) map.get("pagination");

                    Pagination pagination = (Pagination) map_pagination.get("obj_pagination");
                    IntegerProperty total = (IntegerProperty) map_pagination.get("obj_total");
                    IntegerProperty perPage = (IntegerProperty) map_pagination.get("obj_per_page");
                    comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                        total.set(getTableTotal());
                        perPage.set(Integer.parseInt(newValue));
                    });
                } else if (wrapper.getMap().get("group_name").equals("chart_type")) {
                    IntegerProperty chart_option = (IntegerProperty) map.get("chart_option");
                    // 监听索引的变化
                    comboBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
                        chart_option.set(newValue.intValue());
                    });
                } else if (wrapper.getMap().get("group_name").equals("cycle")) {
                    // 监听索引的变化
                    comboBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
                        changeOptions(group_name, newValue.intValue());
                    });
                }
            }
        }
    }

    /**
     * 根据所选周期改变所选选项
     */
    private static void changeOptions(String group_name, int index) {
        HashMap<String, Object> map_year = (HashMap<String, Object>) PageMap.map_history.get(group_name).get("cycle_year");
        HashMap<String, Object> map_month = (HashMap<String, Object>) PageMap.map_history.get(group_name).get("cycle_month");
        HashMap<String, Object> map_day = (HashMap<String, Object>) PageMap.map_history.get(group_name).get("cycle_day");
        HashMap<String, Object> map_hour = (HashMap<String, Object>) PageMap.map_history.get(group_name).get("cycle_hour");

        if (index == 0) {
            setVisibility(map_year, true);
            setVisibility(map_month, false);
            setVisibility(map_day, false);
            setVisibility(map_hour, false);
        } else if (index == 1) {
            setVisibility(map_year, false);
            setVisibility(map_month, true);
            setVisibility(map_day, false);
            setVisibility(map_hour, false);
        } else if (index == 2) {
            setVisibility(map_year, false);
            setVisibility(map_month, false);
            setVisibility(map_day, true);
            setVisibility(map_hour, false);
        } else if (index == 3) {
            setVisibility(map_year, false);
            setVisibility(map_month, false);
            setVisibility(map_day, false);
            setVisibility(map_hour, true);
        }
    }

    private static void setVisibility(HashMap<String, Object> map, boolean visible) {
        map.forEach((key, value) -> {
            HashMap<String, Object> map_item = (HashMap<String, Object>) value;
            Control control = (Control) map_item.get("control");
            BooleanProperty is_show_property = (BooleanProperty) map_item.get("is_show_property");
            is_show_property.set(visible);
            control.visibleProperty().bind(is_show_property);
            control.managedProperty().bind(is_show_property);
        });
    }

    /**
     * 处理分页组件
     */
    private static void handlePagination(ControlWrapper wrapper, IntegerProperty totalItems, IntegerProperty itemsPerPage) {
        //group_name 整个页面的标识
        //wrapper.getMap().get("group_name")) 组件的标识
        Pagination pagination = (Pagination) wrapper.getControl();
        String group_name = wrapper.getGroupName();
        if (StringUtils.isNotEmpty(wrapper.getMap().get("group_name"))) {
            if (PageMap.map_history.containsKey(group_name)) {
                HashMap<String, Object> map = PageMap.map_history.get(group_name);
                HashMap<String, Object> map_obj = new HashMap<>();
                map_obj.put("obj_pagination", pagination);
                map_obj.put("obj_total", totalItems);
                map_obj.put("obj_per_page", itemsPerPage);

                map.put(wrapper.getMap().get("group_name"), map_obj);

                pagination.setPageFactory((pageIndex) -> createPage(pageIndex, group_name));
            }
        }
    }

    /**
     * 图表选项中点击周期下拉框，根据所选选项显式对应组件
     * 处理周期相关的函数
     */
    private static void handleCycle(ControlWrapper wrapper) {
        //group_name 页面的标识
        //wrapper.getMap().get("group_name")) 页面里面的分组
        //wrapper.getMap().get("member_name"))分组里面的各个组件
        String group_name = wrapper.getGroupName();
        if (StringUtils.isNotEmpty(wrapper.getMap().get("group_name")) && StringUtils.isNotEmpty(wrapper.getMap().get("member_name"))) {
            PageMap.map_history.computeIfPresent(group_name, (key, map_page) -> {
                HashMap<String, Object> map_group = (HashMap<String, Object>) map_page.computeIfAbsent(wrapper.getMap().get("group_name"), k -> new HashMap<>());

                BooleanProperty is_show_property = new SimpleBooleanProperty(Boolean.parseBoolean(wrapper.getMap().get("isShow")));
                HashMap<String, Object> map_item = new HashMap<>();
                map_item.put("control", wrapper.getControl());
                map_item.put("is_show_property", is_show_property);

                map_group.put(wrapper.getMap().get("member_name"), map_item);
                return map_page;
            });
        }
    }

    /**
     * 保存状态，控制对应控件是否显示
     */
//    private static void saveStatus(Map<String, String> map, BooleanProperty is_show_property) {
//        if (StringUtils.isNotEmpty(map.get("group_name")) && StringUtils.isNotEmpty(map.get("group_name_sub")) && StringUtils.isNotEmpty(map.get("member_name"))) {
//            if (!map_cycle.containsKey(map.get("group_name"))) {
//                LinkedHashMap<String, Object> map_sub = new LinkedHashMap<>();
//                HashMap<Object, Object> map_item = new HashMap<>();
//                map_item.put(map.get("member_name"), is_show_property);
//                map_sub.put(map.get("group_name_sub"), map_item);
//                map_cycle.put(map.get("group_name"), map_sub);
//            } else {
//                LinkedHashMap<String, Object> map_sub = (LinkedHashMap<String, Object>) map_cycle.get(map.get("group_name"));
//                if (!map_sub.containsKey(map.get("group_name_sub"))) {
//                    HashMap<Object, Object> map_item = new HashMap<>();
//                    map_item.put(map.get("member_name"), is_show_property);
//                    map_sub.put(map.get("group_name_sub"), map_item);
//                } else {
//                    HashMap<String, Object> map_item = (HashMap<String, Object>) map_sub.get(map.get("group_name_sub"));
//                    map_item.put(map.get("member_name"), is_show_property);
//                }
//            }
//        }
//    }
    private static void openLink(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

