package com.watson.monitor.component;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Getter;

import java.util.Map;

import static com.watson.monitor.component.PageUtil.handlePage;
import static com.watson.monitor.utils.LanUtil.addLanguageListen;
import static com.watson.monitor.utils.LanUtil.getText;

/**
 * 弹窗类型,当JSON中配置type为dialog的时候,就渲染为dialog,配置为page的时候就渲染为page.
 * 读取JSON步骤:
 * 1.读取/pages/pages.json文件,获取有哪些页面.假设有home,about.
 * 2.根据/pages/pages.json里面的value获取/pages/about.json.
 * 3.读取/pages/about.json里面的path,此路径为数据对象的JSON路径.
 * 4.读取/data/about.json文件,里面封装了具体的数据,渲染显示
 */
@Getter
public class Dialog extends Pane implements IPage {
    private javafx.scene.control.Dialog<String> dialog = new javafx.scene.control.Dialog<>();


    public Dialog(Map<String, Object> map) {
        this.setMinWidth(600);
        dialog.setTitle(getText((String) map.get("name")));
        addLanguageListen((String) map.get("name"), dialog);

        dialog.setHeaderText(null);
        // 获取对话框的对话面板(DialogPane)
        DialogPane dialogPane = dialog.getDialogPane();

        ButtonType buttonType_confirm = new ButtonType("确定", ButtonBar.ButtonData.OK_DONE);
        dialogPane.getButtonTypes().addAll(buttonType_confirm);

        //设置左上角小图标
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        stage.getIcons().add(new Image("logo.png"));
        // 设置对话面板的图标
        if (Boolean.parseBoolean((String) map.get("is_icon_show"))) {
            dialogPane.setGraphic(new ImageView(new Image("logo.png")));
        }
        handlePage(map, this);
    }

    public void show() {
        dialog.showAndWait();
    }
}
