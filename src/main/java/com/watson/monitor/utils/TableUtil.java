package com.watson.monitor.utils;

import com.watson.monitor.entity.History;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.HashMap;

import static com.watson.monitor.maps.PageMap.map_history;
import static com.watson.monitor.service.HistoryService.getTableItems;

public class TableUtil {
    public static Pane createPage(int pageIndex, String group_name) {
        VBox vBox = new VBox();

        HashMap<String, Object> map = map_history.get(group_name);
        TableView<History> tableView = (TableView<History>) map.get("table_view");

        HashMap<String, Object> map_pagination = (HashMap<String, Object>) map.get("pagination");

        Pagination pagination = (Pagination) map_pagination.get("obj_pagination");
        IntegerProperty total = (IntegerProperty) map_pagination.get("obj_total");
        IntegerProperty perPage = (IntegerProperty) map_pagination.get("obj_per_page");

//        int total_count = getTableTotal();
//        int fromIndex = pageIndex * perPage.get();
//        int toIndex = Math.min(fromIndex + perPage.get(), total_count);
//        if (fromIndex <= toIndex) {
        tableView.setItems(FXCollections.observableArrayList(getTableItems(pageIndex + 1,perPage.get())));
//        }

//        Optional<Map.Entry<String, Object>> matchedEntry = map_history.entrySet().stream()
//                .filter(entryPagination -> map_table.containsKey(entryPagination.getKey()))
//                .findFirst();
//
//        if (matchedEntry.isPresent()) {
//            Map.Entry<String, Object> entryPagination = matchedEntry.get();
//            HashMap<String, Object> mapObjPagination = (HashMap<String, Object>) entryPagination.getValue();
//            Pagination pagination = (Pagination) mapObjPagination.get("obj_pagination");
//            IntegerProperty total = (IntegerProperty) mapObjPagination.get("obj_total");
//            IntegerProperty perPage = (IntegerProperty) mapObjPagination.get("obj_per_page");
//
//            TableView<Map<String, Object>> tableView = (TableView<Map<String, Object>>) map_table.get(entryPagination.getKey());
//
//            int fromIndex = pageIndex * perPage.get();
//            int toIndex = Math.min(fromIndex + perPage.get(), data_history.size());
//            if (fromIndex <= toIndex) {
//                tableView.setItems(FXCollections.observableArrayList(data_history.subList(fromIndex, toIndex)));
//            }
//        }

        // Return the VBox
        return vBox;
    }
}
