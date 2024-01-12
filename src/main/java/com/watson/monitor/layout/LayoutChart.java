package com.watson.monitor.layout;

import com.watson.monitor.maps.PageMap;
import com.watson.monitor.utils.JsonUtil;
import com.watson.monitor.wrappers.LayoutWrapper;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.chart.*;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.watson.monitor.utils.LanUtil.LAN;
import static com.watson.monitor.utils.LanUtil.getText;

public class LayoutChart extends Pane implements Layout {

    private CategoryAxis xAxis_line = new CategoryAxis();
    private CategoryAxis xAxis_bar = new CategoryAxis();
    private NumberAxis yAxis = new NumberAxis();

    private XYChart.Series<String, Number> series_line = new XYChart.Series<>();
    private XYChart.Series<String, Number> series_bar = new XYChart.Series<>();
    // 创建折线图
    private LineChart<String, Number> lineChart;
    // 创建柱状图
    private BarChart<String, Number> barChart;

    private XYChart chart;

    private LinkedHashMap<String, String> line_map;
    private LinkedHashMap<String, String> bar_map;

//    private IntegerProperty chart_option;


    public LayoutChart(LayoutWrapper wrapper, Map<String, String> map) {

        if (PageMap.map_history.containsKey(wrapper.getGroupName())) {
            IntegerProperty chart_option = new SimpleIntegerProperty(0);
            HashMap<String, Object> map1 = PageMap.map_history.get(wrapper.getGroupName());
            map1.put("chart_option",chart_option);
            chart_option.addListener((observable, oldValue, newValue) -> {
                if (newValue.intValue() == 0) {
                    chart = lineChart;
                } else {
                    chart = barChart;
                }
                this.getChildren().setAll(chart);
            });
        }


        Map<String, Object> map_item = JsonUtil.getMap(map.get("path"));
        createLineChart(map_item);
        createBarChart(map_item);
        chart = lineChart;
        this.getChildren().setAll(chart);
    }

    private void createLineChart(Map<String, Object> map) {
        line_map = (LinkedHashMap<String, String>) map.get("line");

        xAxis_line.setLabel(getText(line_map.get("x_name")));
        yAxis.setLabel(getText(line_map.get("y_name")));

        lineChart = new LineChart<>(xAxis_line, yAxis);

        lineChart.setTitle(getText(line_map.get("chart_name")));
        lineChart.setAxisSortingPolicy(LineChart.SortingPolicy.X_AXIS);

        // 创建数据系列
        series_line.setName(getText(line_map.get("legend_name")));
        fillData(series_line);

        LAN.addListener((observable, oldValue, newValue) -> {
            xAxis_line.setLabel(getText(line_map.get("x_name")));
            yAxis.setLabel(getText(line_map.get("y_name")));
            lineChart.setTitle(getText(line_map.get("chart_name")));
            series_line.setName(getText(line_map.get("legend_name")));
        });

        // 将数据系列添加到折线图中
        lineChart.getData().add(series_line);
        lineChart.prefWidthProperty().bind(this.widthProperty());
    }

    private void createBarChart(Map<String, Object> map) {
        bar_map = (LinkedHashMap<String, String>) map.get("bar");

        xAxis_bar.setLabel(getText(bar_map.get("x_name")));

        yAxis.setLabel(getText(bar_map.get("y_name")));
        barChart = new BarChart<>(xAxis_bar, yAxis);

        barChart.setTitle(getText(bar_map.get("chart_name")));
//        barChart.setAxisSortingPolicy(LineChart.SortingPolicy.X_AXIS);

        // 创建数据系列
        series_bar.setName(getText(bar_map.get("legend_name")));

        fillData(series_bar);

        LAN.addListener((observable, oldValue, newValue) -> {
            xAxis_bar.setLabel(getText(bar_map.get("x_name")));
            yAxis.setLabel(getText(bar_map.get("y_name")));
            barChart.setTitle(getText(bar_map.get("chart_name")));
            series_bar.setName(getText(bar_map.get("legend_name")));
        });

        // 将数据系列添加到折线图中
        barChart.getData().add(series_bar);
        barChart.prefWidthProperty().bind(this.widthProperty());
    }

    private void fillData(XYChart.Series<String, Number> series) {
        // 添加数据点
        series.getData().add(new XYChart.Data<>("2020", 5));
        series.getData().add(new XYChart.Data<>("2021", 10));
        series.getData().add(new XYChart.Data<>("2022", 8));
        series.getData().add(new XYChart.Data<>("2023", 15));
        series.getData().add(new XYChart.Data<>("2024", 12));

        // Add Tooltip to each data point
        for (XYChart.Data<String, Number> data : series.getData()) {
            Tooltip tooltip = new Tooltip(data.getYValue().toString());
            tooltip.setShowDelay(javafx.util.Duration.ZERO); // Set showDelay to zero for immediate display
            Tooltip.install(data.getNode(), tooltip);
        }
    }

}
