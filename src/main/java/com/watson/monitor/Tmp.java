package com.watson.monitor;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Tmp  {
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//    @Override
//    public void start(Stage primaryStage) {
//        primaryStage.setTitle("Tree Drag Example");
//
//        TreeItem<TaskNode> rootItem = new TreeItem<TaskNode>(new TaskNode("Tasks"));
//        rootItem.setExpanded(true);
//
//        ObservableList<TreeItem<TaskNode>> children = rootItem.getChildren();
//        children.add(new TreeItem<>(new TaskNode("do laundry")));
//        children.add(new TreeItem<>(new TaskNode("get groceries")));
//        children.add(new TreeItem<>(new TaskNode("drink beer")));
//        children.add(new TreeItem<>(new TaskNode("defrag hard drive")));
//        children.add(new TreeItem<>(new TaskNode("walk dog")));
//        children.add(new TreeItem<>(new TaskNode("buy beer")));
//
//        TreeView<TaskNode> tree = new TreeView<TaskNode>(rootItem);
//        tree.setCellFactory(new TaskCellFactory());
//
//        StackPane root = new StackPane();
//        root.getChildren().add(tree);
//        primaryStage.setScene(new Scene(root, 300, 250));
//        primaryStage.show();
//    }

    private static IntegerProperty ITEMS_PER_PAGE = new SimpleIntegerProperty(10); // 每页显示的数据行数
    private static IntegerProperty TOTAL = new SimpleIntegerProperty(100); // 每页显示的数据行数

//    @Override
//    public void start(Stage primaryStage) {
        // 创建一个假的数据源（实际情况下应该从数据库或其他数据源获取数据）
//        ObservableList<String> data = FXCollections.observableArrayList();
//        for (int i = 1; i <= 1000; i++) {
//            data.add("Data " + i);
//        }
//
//        // 创建 TableView 并设置数据源
//        TableView<String> tableView = new TableView<>();
//        tableView.setItems(data);
//
//        // 创建 Pagination 并设置总页数
//        int totalPageCount = (data.size() + ITEMS_PER_PAGE - 1) / ITEMS_PER_PAGE;
//        Pagination pagination = new Pagination(totalPageCount);
//        pagination.setPageFactory(pageIndex -> createPage(pageIndex, tableView));

//        StackPane root = new StackPane();
////        root.getChildren().addAll(tableView, pagination);
//
//        Scene scene = new Scene(root, 400, 300);

//        primaryStage.setTitle("JavaFX Table Pagination Example");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//
////        (100 + ITEMS_PER_PAGE - 1) / ITEMS_PER_PAGE
//        System.out.println("结果是="+TOTAL.add(ITEMS_PER_PAGE).subtract(1).divide(ITEMS_PER_PAGE));
//    }

//    private StackPane createPage(int pageIndex, TableView<String> tableView) {
////        int fromIndex = pageIndex * ITEMS_PER_PAGE;
////        int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, tableView.getItems().size());
////        tableView.setItems(FXCollections.observableArrayList(tableView.getItems().subList(fromIndex, toIndex)));
////        return new StackPane(tableView);
//    }

    public static void main(String[] args) {
//        launch(args);
//        (100 + ITEMS_PER_PAGE - 1) / ITEMS_PER_PAGE
        System.out.println("结果是="+TOTAL.add(ITEMS_PER_PAGE).subtract(1).divide(ITEMS_PER_PAGE).getValue());

    }


//    @Override
//    public void start(Stage primaryStage) {
//        TreeItem<String> root = new TreeItem<>("Root");
//        TreeItem<String> item1 = new TreeItem<>("Item 1");
//        TreeItem<String> item2 = new TreeItem<>("Item 2");
//        TreeItem<String> item3 = new TreeItem<>("Item 3");
//        root.getChildren().addAll(item1, item2, item3);
//
//        TreeView<String> treeView = new TreeView<>(root);
//        treeView.setCellFactory(tv -> {
//            TreeCell<String> cell = new TreeCell<>() {
//                @Override
//                protected void updateItem(String item, boolean empty) {
//                    super.updateItem(item, empty);
//                    if (empty || item == null) {
//                        setText(null);
//                    } else {
//                        setText(item);
//                    }
//                }
//            };
//
//            cell.setOnDragDetected(event -> {
//                if (cell.getItem() != null) {
//                    Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
//                    ClipboardContent content = new ClipboardContent();
//                    content.putString(cell.getItem());
//                    db.setContent(content);
//                }
//                event.consume();
//            });
//
//            cell.setOnDragOver(event -> {
//                if (event.getGestureSource() != cell && event.getDragboard().hasString()) {
//                    event.acceptTransferModes(TransferMode.MOVE);
//                }
//                event.consume();
//            });
//
//            cell.setOnDragEntered(event -> {
//                if (event.getGestureSource() != cell && event.getDragboard().hasString()) {
//                    cell.setStyle("-fx-background-color: #00ff00;");
//                }
//            });
//
//            cell.setOnDragExited(event -> {
//                cell.setStyle("");
//            });
//
//            cell.setOnDragDropped(event -> {
//                Dragboard db = event.getDragboard();
//                boolean success = false;
//                if (db.hasString()) {
//                    TreeItem<String> draggedItem = cell.getTreeItem();
//                    TreeItem<String> targetItem = cell.getTreeView().getRoot();
//                    if (targetItem != null) {
//                        targetItem.getChildren().remove(draggedItem);
//                        targetItem.getChildren().add(targetItem.getChildren().indexOf(cell.getTreeItem()), draggedItem);
//                        success = true;
//                    }
//                }
//                event.setDropCompleted(success);
//                event.consume();
//            });
//
//            return cell;
//        });
//
//        StackPane rootPane = new StackPane(treeView);
//        Scene scene = new Scene(rootPane, 300, 200);
//        primaryStage.setTitle("TreeView Drag and Drop Example");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }

}