package com.hs.monitor.data;

import java.util.*;

import static com.hs.monitor.layout.LayoutTable.map_headers;

public class TableData {
//    public static ObservableList<History> data_history = FXCollections.observableArrayList();

    // Method to create a Map representing a row of data
    private static Map<String, Object> createData(Object... values) {
        Map<String, Object> row = new HashMap<>();

        // Assuming the number of values matches the columns in the table
        List<String> columnNames = getColumnNames(); // Implement this method to get column names
        for (int i = 0; i < columnNames.size(); i++) {
            row.put(columnNames.get(i), values[i]);
        }

        return row;
    }

    private static List<String> getColumnNames() {
        List<String> columnNames = new ArrayList<>();
        map_headers.forEach((key, value) -> {
            LinkedHashMap<String, String> map1 = (LinkedHashMap<String, String>) value;
            String prop = map1.get("prop");
            columnNames.add(prop);
        });
        return columnNames;
    }
}
