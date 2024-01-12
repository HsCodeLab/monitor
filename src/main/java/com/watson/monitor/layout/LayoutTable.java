package com.watson.monitor.layout;

import com.watson.monitor.entity.History;
import com.watson.monitor.maps.PageMap;
import com.watson.monitor.utils.JsonUtil;
import com.watson.monitor.wrappers.LayoutWrapper;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.watson.monitor.service.HistoryService.getTableItems;
import static com.watson.monitor.utils.LanUtil.*;

public class LayoutTable extends Pane implements Layout {

    public static LinkedHashMap<String, Object> map_headers;

    public LayoutTable(LayoutWrapper wrapper, Map<String, String> map) {

        Map<String, Object> map_item = JsonUtil.getMap(map.get("path"));
        map_headers = (LinkedHashMap<String, Object>) map_item.get("headers");
//        TableView<Map<String, Object>> tableView = new TableView<>();
        TableView<History> tableView = new TableView<>();

        //为什么要放到map里面
//        //是为了在点击分页的时候能操作到对应的TableView对象
//        map_table.put((String) map_item.get("group_name"), tableView);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        map_headers.forEach((key, value) -> {
            LinkedHashMap<String, String> map1 = (LinkedHashMap<String, String>) value;
            String name = getText(map1.get("name"));
            String unit = map1.get("unit");
            if (StringUtils.isNotEmpty(unit)) {
                name = name + "(" + unit + ")";
            }
//            TableColumn<Map<String, Object>, String> col = new TableColumn<>(name);
            TableColumn<History, String> col = new TableColumn<>(name);

            LAN.addListener((observable, oldValue, newValue) -> {
                String name1 = getText(map1.get("name"));
                String unit1 = map1.get("unit");
                if (StringUtils.isNotEmpty(unit1)) {
                    name1 = name1 + "(" + unit1 + ")";
                }
                col.setText(name1);
            });

            col.setCellValueFactory(new PropertyValueFactory<>(map1.get("prop")));

//            col.setCellValueFactory(param ->
//                    new SimpleObjectProperty<>(String.valueOf(param.getValue().get(map1.get("prop"))))
//            );

            tableView.getColumns().add(col);
        });

        if (PageMap.map_history.containsKey(wrapper.getGroupName())) {
            HashMap<String, Object> map1 = PageMap.map_history.get(wrapper.getGroupName());
            map1.put("table_view", tableView);
        }

        tableView.setItems(getTableItems(1, 10));


        tableView.prefWidthProperty().bind(this.widthProperty());
        this.getChildren().add(tableView);
    }

}
