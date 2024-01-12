package com.watson.monitor.layout;

import com.watson.monitor.utils.JsonUtil;
import com.watson.monitor.wrappers.ControlWrapper;
import com.watson.monitor.wrappers.LayoutWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static com.watson.monitor.utils.ControlUtil.createComponent;
import static com.watson.monitor.utils.JsonUtil.readJSON;
import static com.watson.monitor.utils.LanUtil.addLanguageListen;
import static com.watson.monitor.utils.LanUtil.getText;

public class LayoutVBox extends VBox implements Layout {
    public LayoutVBox(LayoutWrapper wrapper, Map<String, String> map) {
        this.setPadding(new Insets(10));
        this.setSpacing(10);

        this.setAlignment(Pos.CENTER);

        Map<String, Object> map_item = JsonUtil.getMap(map.get("path"));
        readJSON(map_item, (key_inner, map_inner) -> {
            if (StringUtils.isNotEmpty(map_inner.get("name"))) {
                Label label = new Label(getText(map_inner.get("name")));
                addLanguageListen(map_inner.get("name"),label);
                this.getChildren().add(label);
            }
            ControlWrapper controlWrapper = ControlWrapper.builder().groupName(wrapper.getGroupName()).handlerClass(wrapper.getHandlerClass()).map(map_inner).valueProperty(null).build();
            this.getChildren().add(createComponent(map_inner.get("type"), controlWrapper));
            if (StringUtils.isNotEmpty(map_inner.get("unit"))) {
                this.getChildren().add(new Label(map_inner.get("unit")));
            }
        });
    }
}
