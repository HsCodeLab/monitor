package com.hs.monitor.layout;

import com.hs.monitor.utils.JsonUtil;
import com.hs.monitor.wrappers.ControlWrapper;
import com.hs.monitor.wrappers.LayoutWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static com.hs.monitor.utils.ControlUtil.createComponent;
import static com.hs.monitor.utils.JsonUtil.readJSON;
import static com.hs.monitor.utils.LanUtil.addLanguageListen;
import static com.hs.monitor.utils.LanUtil.getText;

public class LayoutHBox extends HBox implements Layout {
    public LayoutHBox(LayoutWrapper wrapper, Map<String, String> map) {

        this.setPadding(new Insets(10));
        this.setSpacing(10);

        this.setAlignment(Pos.CENTER_LEFT);

//        BackgroundFill backgroundFill = new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY);
//        Background background = new Background(backgroundFill);
//        this.setBackground(background);

        Map<String, Object> map_item = JsonUtil.getMap(map.get("path"));

        try {
            readJSON(map_item, (key_inner, map_inner) -> {
                if (StringUtils.isNotEmpty(map_inner.get("name"))) {
                    Label label = new Label(getText(map_inner.get("name")));
                    addLanguageListen(map_inner.get("name"), label);
                    this.getChildren().add(label);
                }
                ControlWrapper controlWrapper = ControlWrapper.builder().groupName(wrapper.getGroupName()).handlerClass(wrapper.getHandlerClass()).map(map_inner).valueProperty(null).build();
                this.getChildren().add(createComponent(map_inner.get("type"), controlWrapper));
                if (StringUtils.isNotEmpty(map_inner.get("unit"))) {
                    this.getChildren().add(new Label(map_inner.get("unit")));
                }
            });
        } catch (Exception e) {
            System.out.println("发生异常时Path=" + map.get("path"));
        }
    }
}
