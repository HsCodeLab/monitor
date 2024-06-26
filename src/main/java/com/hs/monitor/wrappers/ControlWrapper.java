package com.hs.monitor.wrappers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * 三个wrapper的关系说明
 * page -> layout -> control
 * 页面包含布局(比如HBox),布局包含组件,
 * 参数一层层传递
 */
@Getter
@Setter
@Builder
@ToString
public class ControlWrapper {
    private String groupName;
    private String handlerClass;
    private Control control;
    private Map<String, String> map;
    private StringProperty valueProperty;
    private BooleanProperty checkedProperty;
}

