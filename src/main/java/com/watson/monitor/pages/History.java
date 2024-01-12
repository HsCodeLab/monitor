package com.watson.monitor.pages;//package com.watson.monitor.pages;
//
//import com.watson.monitor.utils.JsonUtil;
//import javafx.beans.property.IntegerProperty;
//import javafx.beans.property.SimpleIntegerProperty;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.SnapshotParameters;
//import javafx.scene.chart.*;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.image.WritableImage;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import javafx.stage.FileChooser;
//import org.apache.commons.lang3.StringUtils;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.util.Map;
//
//import static com.watson.monitor.utils.ControlUtil.createComponent;
//import static com.watson.monitor.utils.JsonUtil.readJSON;
//
//public class History extends VBox {
//
//    private static Map<String, Object> map_his = JsonUtil.getMap("/toolbar/history");
//    private static Map<String, Object> map_tab = JsonUtil.getMap("/component/table_top");
//    private static Map<String, Object> map_btm_tab = JsonUtil.getMap("/component/table_btm");
//    private static Map<String, Object> map_cha = JsonUtil.getMap("/component/chart_top");
//    private static Map<String, Object> map_btm_cha = JsonUtil.getMap("/component/chart_btm");
//
//    private VBox content1 = new VBox();
//    private VBox content2 = new VBox();
//
//    private Tab tab1 = new Tab();
//    private Tab tab2 = new Tab();
//
//    private IntegerProperty totalItems = new SimpleIntegerProperty(100);
//    private IntegerProperty itemsPerPage = new SimpleIntegerProperty(10);
//
//
//    public History() {
////        VBox vBox = new VBox();
//        this.setPadding(new Insets(10));
//        this.setSpacing(20);
//
//        String tabbars = (String) map_his.get("tabbars");
//        String[] arr = tabbars.split(",");
//        tab1.setText(arr[0]);
//        tab2.setText(arr[1]);
//
//        TabPane tabPane = new TabPane();
//
//        createTable();
//        createChart();
//
//        // 将选项卡添加到 TabPane 中
//        tabPane.getTabs().addAll(tab1, tab2);
//
//        this.getChildren().add(tabPane);
//    }
//
//    public void createTable() {
//        HBox hBox = new HBox();
//        hBox.setSpacing(10);
//        hBox.setAlignment(Pos.CENTER_LEFT);
//
//        readJSON(map_tab, (key, map) -> {
//            Control control = createComponent(map.get("type"), null, map, null);
//            hBox.getChildren().add(control);
//
//        });
//
//        TableView<com.watson.entity.History> tableView = new TableView<>();
//        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//
//        readJSON(map_his, (key, map) -> {
//            String name = map.get("name");
//            String unit = map.get("unit");
//            if (StringUtils.isNotEmpty(unit)) {
//                name = name + "(" + unit + ")";
//            }
//            TableColumn<com.watson.entity.History, String> col = new TableColumn<>(name);
//            col.setCellValueFactory(new PropertyValueFactory<>(map.get("prop")));
//            tableView.getColumns().add(col);
//        });
////        ObservableList<com.watson.entity.History> data = FXCollections.observableArrayList(com.watson.entity.History.builder().sn("SN-0001").voltageInput(220).voltageOutput(220).frequencyInput(50.0).frequencyOutput(50.0).batteryVoltage(48.0).batteryLevel(80).temperature(37).outputLoadPercent(80).build(), com.watson.entity.History.builder().sn("SN-0002").voltageInput(220).voltageOutput(220).frequencyInput(50.0).frequencyOutput(50.0).batteryVoltage(48.0).batteryLevel(80).temperature(37).outputLoadPercent(80).build(), com.watson.entity.History.builder().sn("SN-0003").voltageInput(220).voltageOutput(220).frequencyInput(50.0).frequencyOutput(50.0).batteryVoltage(48.0).batteryLevel(80).temperature(37).outputLoadPercent(80).build(), com.watson.entity.History.builder().sn("SN-0004").voltageInput(220).voltageOutput(220).frequencyInput(50.0).frequencyOutput(50.0).batteryVoltage(48.0).batteryLevel(80).temperature(37).outputLoadPercent(80).build(), com.watson.entity.History.builder().sn("SN-0005").voltageInput(220).voltageOutput(220).frequencyInput(50.0).frequencyOutput(50.0).batteryVoltage(48.0).batteryLevel(80).temperature(37).outputLoadPercent(80).build(), com.watson.entity.History.builder().sn("SN-0006").voltageInput(220).voltageOutput(220).frequencyInput(50.0).frequencyOutput(50.0).batteryVoltage(48.0).batteryLevel(80).temperature(37).outputLoadPercent(80).build(), com.watson.entity.History.builder().sn("SN-0007").voltageInput(220).voltageOutput(220).frequencyInput(50.0).frequencyOutput(50.0).batteryVoltage(48.0).batteryLevel(80).temperature(37).outputLoadPercent(80).build(), com.watson.entity.History.builder().sn("SN-0008").voltageInput(220).voltageOutput(220).frequencyInput(50.0).frequencyOutput(50.0).batteryVoltage(48.0).batteryLevel(80).temperature(37).outputLoadPercent(80).build(), com.watson.entity.History.builder().sn("SN-0009").voltageInput(220).voltageOutput(220).frequencyInput(50.0).frequencyOutput(50.0).batteryVoltage(48.0).batteryLevel(80).temperature(37).outputLoadPercent(80).build(), com.watson.entity.History.builder().sn("SN-0010").voltageInput(220).voltageOutput(220).frequencyInput(50.0).frequencyOutput(50.0).batteryVoltage(48.0).batteryLevel(80).temperature(37).outputLoadPercent(80).build());
//
//        tableView.setItems(null);
//        content1.setPadding(new Insets(10));
//        content1.setSpacing(20);
//        tab1.setContent(content1);
//
//        Pagination pagination = new Pagination();
//        pagination.pageCountProperty().bind(totalItems.add(itemsPerPage).subtract(1).divide(itemsPerPage));
//        pagination.setCurrentPageIndex(0); // Set initial page index
////        pagination.setPageFactory(pageIndex -> createPage(pageIndex, tableView));
//        HBox hBox1 = new HBox();
//        hBox1.setSpacing(10);
//        hBox1.setAlignment(Pos.CENTER);
//
//        hBox1.getChildren().add(pagination);
//
//        readJSON(map_btm_tab, (key, map) -> {
//            Control control = createComponent(map.get("type"), null, map, null);
//            hBox1.getChildren().add(control);
//            if (control instanceof Button) {
//                Button button = (Button) control;
//                button.setOnAction(e -> exportToExcel());
//            } else if (control instanceof ComboBox<?>) {
//                ComboBox<String> comboBox = (ComboBox<String>) control;
//                comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
//                    System.out.println("Selected option: " + newValue);
//                    itemsPerPage.set(Integer.parseInt(newValue));
//                });
//            }
//        });
//
//        content1.getChildren().add(hBox);
//        content1.getChildren().add(tableView);
//        content1.getChildren().add(hBox1);
//    }
//
//    private NumberAxis xAxis = new NumberAxis();
//    private CategoryAxis xAxis_bar = new CategoryAxis();
//    private NumberAxis yAxis = new NumberAxis();
//    // 创建折线图
//    private LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
//    // 创建柱状图
//    private BarChart<String, Number> barChart = new BarChart<>(xAxis_bar, yAxis);
//
//    private XYChart chart = lineChart;
//
//    public void createChart() {
//        HBox hBox = new HBox();
//        hBox.setSpacing(10);
//        hBox.setAlignment(Pos.CENTER_LEFT);
//
//        readJSON(map_cha, (key, map) -> {
//            if (Boolean.parseBoolean(map.get("isShow"))) {
//                Control control = createComponent(map.get("type"), null, map, null);
//                hBox.getChildren().add(control);
//            }
//        });
//
//        xAxis.setLabel("时间");
//        xAxis_bar.setLabel("时间");
//        yAxis.setLabel("发电量");
//
//        lineChart.setTitle("充电记录");
//        barChart.setTitle("充电记录");
//
//        // 创建数据系列
//        XYChart.Series<Number, Number> series = new XYChart.Series<>();
//        series.setName("历史充电记录");
//
//        // 添加数据点
//        series.getData().add(new XYChart.Data<>(1, 5));
//        series.getData().add(new XYChart.Data<>(2, 10));
//        series.getData().add(new XYChart.Data<>(3, 8));
//        series.getData().add(new XYChart.Data<>(4, 15));
//        series.getData().add(new XYChart.Data<>(5, 12));
//
//        XYChart.Series<String, Number> series_bar = new XYChart.Series<>();
//        series.setName("历史充电记录");
//
//        // 添加数据点
//        series_bar.getData().add(new XYChart.Data<>("2019", 5));
//        series_bar.getData().add(new XYChart.Data<>("2020", 10));
//        series_bar.getData().add(new XYChart.Data<>("2021", 8));
//        series_bar.getData().add(new XYChart.Data<>("2022", 15));
//        series_bar.getData().add(new XYChart.Data<>("2023", 12));
//
//        // 将数据系列添加到折线图中
//        lineChart.getData().add(series);
//        barChart.getData().add(series_bar);
//
//        HBox hBox1 = new HBox();
//        hBox1.setSpacing(10);
//        hBox1.setAlignment(Pos.CENTER);
//
//        readJSON(map_btm_cha, (key, map) -> {
//            Control control = createComponent(map.get("type"), null, map, null);
//            hBox1.getChildren().add(control);
//            if (control instanceof Button) {
//                Button button = (Button) control;
//                button.setOnAction(e -> saveChartAsImage());
//            } else if (control instanceof ComboBox<?>) {
//                ComboBox<String> comboBox = (ComboBox<String>) control;
//                comboBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldIndex, newIndex) -> {
//                    System.out.println("索引变化newIndex=" + newIndex.intValue());
//                    if (newIndex.intValue() == 0) {
//                        content2.getChildren().set(1, lineChart);
//                        chart = lineChart;
//                    } else {
//                        chart = barChart;
//                        content2.getChildren().set(1, barChart);
//                    }
//                });
//            }
//        });
//
//
//        content2.setPadding(new Insets(10));
//        content2.setSpacing(20);
//        content2.getChildren().add(hBox);
//        content2.getChildren().add(lineChart);
//        content2.getChildren().add(hBox1);
//        tab2.setContent(content2);
//    }
//
//    private void exportToExcel() {
//
//    }
//
//    private void saveChartAsImage() {
//        // Display a file chooser dialog for saving
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Save Chart as Image");
//        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG files", "*.png"), new FileChooser.ExtensionFilter("JPEG files", "*.jpeg", "*.jpg"));
//
//        File selectedFile = fileChooser.showSaveDialog(chart.getScene().getWindow());
//
//        if (selectedFile != null) {
//            // Save the chart as the selected image format
//            WritableImage image = chart.snapshot(new SnapshotParameters(), null);
//
//            BufferedImage bufferedImage = new BufferedImage((int) image.getWidth(), (int) image.getHeight(), BufferedImage.TYPE_INT_ARGB);
//            for (int x = 0; x < image.getWidth(); x++) {
//                for (int y = 0; y < image.getHeight(); y++) {
//                    bufferedImage.setRGB(x, y, image.getPixelReader().getArgb(x, y));
//                }
//            }
//
//            try {
//                ImageIO.write(bufferedImage, getFileExtension(selectedFile), selectedFile);
//                System.out.println("Chart saved as image.");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private String getFileExtension(File file) {
//        String fileName = file.getName();
//        int dotIndex = fileName.lastIndexOf(".");
//        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
//            return fileName.substring(dotIndex + 1).toLowerCase();
//        }
//        return ""; // Default to empty extension
//    }
//
//}
