package com.hs.monitor.component;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Map;


public class DialogTool extends Pane {
    private javafx.scene.control.Dialog<String> dialog = new javafx.scene.control.Dialog<>();

//    private static Map<Class<? extends Pane>, PaneHandler> paneHandlers = new HashMap<>();

    public DialogTool() {
        dialog.setTitle("添加");
        dialog.setHeaderText(null);

        ButtonType buttonType_confirm = new ButtonType("确定", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonType_confirm);

        // 获取对话框的对话面板(DialogPane)
        DialogPane dialogPane = dialog.getDialogPane();

        //设置左上角小图标
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        stage.getIcons().add(new Image("logo.png"));

        GridPane gridPane = new GridPane();
        Label label_name = new Label("名称");
        TextField text_name = new TextField();

        Label label_unit = new Label("单位");
        TextField text_unit = new TextField();

        gridPane.add(label_name,0,0);
        gridPane.add(text_name,1,0);
        gridPane.add(label_unit,2,0);
        gridPane.add(text_unit,3,0);

        dialog.getDialogPane().setContent(gridPane);


        // 设置对话面板的图标
//        dialogPane.setGraphic(new ImageView(new Image("logo.png")));

//        paneHandlers.put(GridPane.class, this::addGridPane);
//        paneHandlers.put(HBox.class, this::addHBox);
//        paneHandlers.put(VBox.class, this::addVBox);
//
//        Pane pane = LayoutFactoryPage.createLayout((String) map.get("layout"));
//        PaneHandler handler = paneHandlers.get(pane.getClass());
//        if (handler != null) {
//            handler.handle(map, null);
//        } else {
//            // Handle unknown pane type, or provide a default behavior
//        }

//        //获取到resources目录下data或者protocol的配置
//        Map<String, Object> map_path = JsonUtil.getMap((String)map.get("path"));
//        AtomicInteger i = new AtomicInteger();
//
//        readJSON(map_path, (key1, map1) -> {
//            AtomicInteger col_num = new AtomicInteger();//列号
//            Control control = createComponent(map_types.get(map1.get("type")), map1);
//            if (StringUtils.isNotEmpty(map1.get("name"))) {
//                grid.add(new Label(map1.get("name")), col_num.getAndIncrement(), i.get());
//            }
//            grid.add(control, col_num.getAndIncrement(), i.get());
//            if (StringUtils.isNotEmpty(map1.get("unit"))) {
//                grid.add(new Label(map1.get("unit")), col_num.get(), i.get());
//            }
//            dialog.getDialogPane().setContent(grid);
//            i.getAndIncrement();
//        });
    }

    public void show() {
        dialog.showAndWait();
    }
}
