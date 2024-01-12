package com.watson.monitor.wrappers;

import javafx.scene.control.Button;
import javafx.scene.control.Control;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

/**
 * 三个wrapper的关系说明
 * page -> layout -> control
 * 页面包含布局(比如HBox),布局包含组件,
 * 参数一层层传递
 * <p>
 * GridWrapper是LayoutWrapper的特例
 */
@Builder
@Getter
public class GridWrapper {
    private String groupName;
    private String handlerClass;
    private Map<String, String> map;
    private Control control;
    private Button button;
}
