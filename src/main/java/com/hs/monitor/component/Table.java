package com.hs.monitor.component;

import com.hs.monitor.entity.Warning;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.Date;

/**
 * 页面类型,读取JSON配置文件的时候要判断当前页面是要渲染成页面还是弹窗
 */
public class Table extends VBox implements IPage {
    public Table(String name, String title) {
        render(name, title);
    }

    public void render(String name, String title) {
        this.setPadding(new Insets(10));

        TableView<Warning> tableView = new TableView<>();
        TableColumn<Warning, String> col1 = new TableColumn<>("SN");
        col1.setCellValueFactory(new PropertyValueFactory<>("sn"));

        TableColumn<Warning, Date> col2 = new TableColumn<>("时间");
        col2.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<Warning, String> col3 = new TableColumn<>("警告码");
        col3.setCellValueFactory(new PropertyValueFactory<>("code"));

        TableColumn<Warning, String> col4 = new TableColumn<>("告警信息");
        col4.setCellValueFactory(new PropertyValueFactory<>("info"));

        tableView.getColumns().addAll(col1, col2, col3, col4);

        ObservableList<Warning> data = FXCollections.observableArrayList(
                Warning.builder().sn("SN-0001").time(new Date()).code("001").info("过流").build(),
                Warning.builder().sn("SN-0002").time(new Date()).code("002").info("过压").build(),
                Warning.builder().sn("SN-0003").time(new Date()).code("003").info("漏电自检故障").build(),
                Warning.builder().sn("SN-0004").time(new Date()).code("004").info("欠压").build(),
                Warning.builder().sn("SN-0005").time(new Date()).code("005").info("过流").build(),
                Warning.builder().sn("SN-0006").time(new Date()).code("006").info("过流").build(),
                Warning.builder().sn("SN-0007").time(new Date()).code("007").info("过流").build(),
                Warning.builder().sn("SN-0008").time(new Date()).code("008").info("过流").build(),
                Warning.builder().sn("SN-0009").time(new Date()).code("009").info("过流").build(),
                Warning.builder().sn("SN-0010").time(new Date()).code("010").info("过流").build()
        );

        tableView.setItems(data);
        this.getChildren().add(tableView);
    }
}
