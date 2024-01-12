package com.watson.monitor.wrappers;

import lombok.Builder;
import lombok.Getter;

/**
 * 三个wrapper的关系说明
 * page -> layout -> control
 * 页面包含布局(比如HBox),布局包含组件,
 * 参数一层层传递
 */
@Builder
@Getter
public class LayoutWrapper {
    private String groupName;
    private String handlerClass;
}
