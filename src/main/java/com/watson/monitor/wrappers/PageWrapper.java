package com.watson.monitor.wrappers;

import javafx.scene.layout.Pane;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

/**
 * 三个wrapper的关系说明
 * page -> layout -> control
 * 页面包含布局(比如HBox),布局包含组件,
 * 参数一层层传递
 */
@Builder
@Getter
public
class PageWrapper {
    private String groupName;
    private String handlerClass;
    private Map<String, Object> map;
    private Pane container;
}
